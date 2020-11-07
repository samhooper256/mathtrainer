package base;

import java.util.*;
import java.util.function.*;

import fxutils.*;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.text.*;
import javafx.scene.web.WebView;
import problems.*;
import suppliers.*;
import utils.*;

/**
 * @author Sam Hooper
 *
 */
public class ProblemPane extends StackPane {
	
	private static final String PROBLEM_VIEW_CSS_FILENAME = "problemview.css";
	/**
	 * Number of the user's most recent problem attempts whose times and accuracies will be kept
	 * in temporary storage (and displayed to the user).
	 */
	private static final int RESULTS_TRACKED = 100;
	private static final String SHOW_SKILL_TEXT = "Show Skill", HIDE_SKILL_TEXT = "Hide Skill", SHOW_ANSWER_TEXT = "Show Answer", HIDE_ANSWER_TEXT = "Hide Answer",
			CLEAR_TEXT = "Clear (C)";
	private static final char CLEAR_CHAR = 'c';
	private final FixedDoubleQueue times;
	private final FixedBooleanQueue accuracies;
	private final CompositeProblemSupplier compositeSupplier;
	private final WebView problemView;
	private final Label answerLabel, lastTimeLabel, averageTimeLabel, averageAccuracyLabel, skillLabel;
	/**
	 * The {@link TextField} where the user will type their answer.
	 */
	private final TextField field;
	private final HBox buttonBox;
	private final Button submit, clear, showSkill, showAnswer;
	private final CheckBox deleteText, markWrongIfCleared, markWrongIfShownAnswer, clearOnWrongAnswer;
	private final Set<Class<? extends ProblemSupplier>> supplierClasses;
	
	private double startTime;
	/**
	 * The number of wrong answers the user has submitted for the current {@link Problem}.
	 */
	private int wrongAnswers;
	/**
	 * {@code true} if text had been deleted (either by using the backspace or delete keys or by using the "clear" button).
	 */
	private boolean hasDeletedText;
	/**
	 * {@code true} if the user has shown the answer to the {@link #currentProblem} (using the {@link #showAnswer} Button.
	 */
	private boolean hasShownAnswer;
	private Problem currentProblem;
	private ProblemSupplier currentProblemSupplier;
	
	public ProblemPane(final CompositeProblemSupplier problemSupplier) {
		this.compositeSupplier = Objects.requireNonNull(problemSupplier);
		this.times = new FixedDoubleQueue(RESULTS_TRACKED);
		this.accuracies = new FixedBooleanQueue(RESULTS_TRACKED);
		
		
		answerLabel = new Label();
		this.lastTimeLabel = new Label();
		this.averageTimeLabel = new Label();
		this.averageAccuracyLabel = new Label();
		submit = Buttons.of("Submit", () -> acceptInput());
		showAnswer = Buttons.of(SHOW_ANSWER_TEXT, this::showAnswerButtonAction);
		clear = Buttons.of(CLEAR_TEXT, this::clearButtonAction);
		this.showSkill = Buttons.of(SHOW_SKILL_TEXT, this::toggleSkillShowing);
		buttonBox = new HBox(4, submit, clear, showSkill, showAnswer, answerLabel);
		buttonBox.setAlignment(Pos.CENTER);
		field = new TextField();
		field.maxWidthProperty().bind(this.widthProperty().divide(2));
		field.addEventFilter(KeyEvent.KEY_PRESSED, keyEvent -> {
			switch(keyEvent.getCode()) {
				case BACK_SPACE, DELETE -> {
					if(!this.canDelete())
						keyEvent.consume();
					else
						hasDeletedText = true;
				}
			}
		});
		field.addEventFilter(KeyEvent.KEY_TYPED, keyEvent -> {
			if(keyEvent.getCharacter().length() > 1) {
				keyEvent.consume(); return;
			}
			char c = keyEvent.getCharacter().charAt(0);
			if(c == CLEAR_CHAR) {
				keyEvent.consume();
				clearButtonAction();
			}
		});
		problemView = new WebView();
		problemView.getEngine().setUserStyleSheetLocation(getClass().getResource(PROBLEM_VIEW_CSS_FILENAME).toString());
		problemView.prefWidthProperty().bind(field.widthProperty());
		problemView.prefHeightProperty().bind(field.heightProperty().multiply(1.2));
		setOnKeyPressed(keyEvent -> {
			paneKeyHandler(keyEvent);
		});
		skillLabel = new Label();
		skillLabel.setWrapText(true);
		skillLabel.setVisible(false);
		deleteText = new CheckBox("Can delete text");
		markWrongIfCleared = new CheckBox("Mark wrong if cleared or deleted");
		markWrongIfCleared.setSelected(true);
		markWrongIfShownAnswer = new CheckBox("Mark wrong if shown answer");
		markWrongIfShownAnswer.setSelected(true);
		clearOnWrongAnswer = new CheckBox("Clear on wrong answer");
		VBox vBox = new VBox(10, problemView, field, buttonBox, deleteText, markWrongIfCleared, markWrongIfShownAnswer, clearOnWrongAnswer, skillLabel);
		vBox.setAlignment(Pos.CENTER);
		
		HBox resultsBox = new HBox(10, lastTimeLabel, averageTimeLabel, averageAccuracyLabel);
		AnchorPane anchor = new AnchorPane(resultsBox);
		anchor.setMouseTransparent(true);
		AnchorPane.setBottomAnchor(resultsBox, 10d);
		AnchorPane.setLeftAnchor(resultsBox, 10d);
		AnchorPane.setRightAnchor(resultsBox, 10d);
		
		getChildren().addAll(vBox, anchor);
		
		this.supplierClasses = new HashSet<>();
		for(ProblemSupplier ps : problemSupplier.suppliers()) {
			this.supplierClasses.add(ps.getClass());
		}
		
		problemSupplier.suppliers().addAddListener(ps -> {
			this.supplierClasses.add(ps.getClass());
		});
		problemSupplier.suppliers().addRemoveListener(ps -> {
			this.supplierClasses.remove(ps.getClass());
		});
		generateProblemAndUpdateLabel();
		this.wrongAnswers = 0;
		updateSkillText();
		
		startTime = System.nanoTime();
	}

	/**
	 * Sets the elapsed time spent on the current {@link Problem} to zero (from which it immediately starts increasing as time passes).
	 */
	public void resetCurrentProblemTimer() {
		startTime = System.nanoTime();
	}
	
	public CompositeProblemSupplier getSupplier() {
		return compositeSupplier;
	}

	/**
	 * Adds the given {@link ProblemSupplier} to this {@link ProblemPane ProblemPane's} list of {@link ProblemSupplier ProblemSuppliers}
	 * that will generate its {@link Problem Problems}. Throws an exception if it cannot be added because a {@link ProblemSupplier} of
	 * the given supplier's class is already present.
	 * @throws IllegalStateException if the given {@link ProblemSupplier} cannot be added.
	 */
	public void addSupplierOrThrow(ProblemSupplier supplier) {
		if(!addSupplier(supplier))
			throw new IllegalStateException("Cannot add supplier: " + supplier);
	}

	/**
	 * Returns {@code true} if the given {@link ProblemSupplier} has been added as one of the suppliers that will generate this {@link ProblemPane ProblemPane's}
	 * {@link Problem Problems}, {@code false} otherwise. Note that {@link ProblemPane ProblemPanes} do not allow for more than one
	 * {@link ProblemSupplier ProblemSuppliers} of the same {@link Class}.
	 */
	private boolean addSupplier(ProblemSupplier supplier) {
		boolean added = supplierClasses.add(supplier.getClass());
		if(added)
			compositeSupplier.addSupplier(supplier);
		return added;
	}

	public boolean hasSupplierOfClass(Class<? extends ProblemSupplier> clazz) {
		return supplierClasses.contains(clazz);
	}
	
	private void paneKeyHandler(KeyEvent keyEvent) {
		switch(keyEvent.getCode()) {
			case ENTER -> acceptInput();
		}
	}

	private void updateAccuracies() {
		accuracies.addFirst(wrongAnswers == 0 && (!hasDeletedText || !isMarkWrongIfCleared()) && (!hasShownAnswer || !isMarkWrongIfShownAnswer()));
		averageAccuracyLabel.setText(String.format("Last %d Accuracy: %.1f%%", accuracies.size(), accuracies.truthProportion() * 100));
	}

	/**
	 * Accepts the input that is currently in the {@link #field}. Does nothing if the input {@link String#isBlank() is blank}.
	 */
	private void acceptInput() {
		final String input = field.getText().strip();
		if(input.isBlank())
			return;
		acceptInput(input);
	}
	
	/**
	 * Accepts the given input, {@link #setupNextProblem() setting up the next problem} if the input is a correct answer 
	 * or {@link #wrongAnswer() processing it as a wrong answer} otherwise.
	 */
	private void acceptInput(final String inputString) {
		if(validate(inputString))
			setupNextProblem();
		else
			wrongAnswer();
	}
	/**
	 * Called when a wrong answer was submitted
	 */
	private void wrongAnswer() {
		field.setBorder(Borders.of(Color.RED));
		if(isClearOnWrongAnswer())
			clearInputField();
		wrongAnswers++;
	}
	
	/**
	 * Returns {@code true} if the given string is a correct answer to the {@link #currentProblem}, {@code false} otherwise.
	 */
	private boolean validate(final String inputString) {
		return currentProblem.isCorrect(inputString);
	}
	
	/**
	 * Sets up this {@link ProblemPane} with a new {@link Problem}, which involves updating times and accuracies, displaying the new problem to the user,
	 * and clearing the input field. Updates {@link #startTime}.
	 */
	private void setupNextProblem() {
		updateResults();
		clearInputField();
		field.setBorder(null);
		hideAnswerIfShowing();
		generateProblemAndUpdateLabel();
		updateSkillText();
		wrongAnswers = 0;
		hasDeletedText = false;
		hasShownAnswer = false;
		resetCurrentProblemTimer();
	}
	
	private void hideAnswerIfShowing() {
		if(isAnswerShowing())
			toggleShowAnswer();
	}
	private void updateResults() {
		updateTimes();
		updateAccuracies();
	}

	private void updateTimes() {
		final double time;
		setLastTime(time = System.nanoTime() - startTime);
		times.addFirst(time);
		averageTimeLabel.setText(String.format("Last %d Average: %s", times.size(), secString(times.average())));
	}
	
	private void clearButtonAction() {
		clearInputField();
		hasDeletedText = true;
	}
	
	private void clearInputField() {
		field.clear();
	}
	
	private void generateProblemAndUpdateLabel() {
		generateProblem();
		updateLabel();
	}

	private void generateProblem() {
		currentProblemSupplier = compositeSupplier.getRandomSupplier();
		currentProblem = currentProblemSupplier.get();
	}

	private void updateLabel() {
		assert currentProblem.displayString() != null;
		problemView.getEngine().loadContent(currentProblem.displayString());
	}
	
	private void toggleSkillShowing() {
		if(isSkillShowing()) {
			skillLabel.setVisible(false);
			showSkill.setText(SHOW_SKILL_TEXT);
		}
		else {
			skillLabel.setVisible(true);
			showSkill.setText(HIDE_SKILL_TEXT);			
		}
		
	}

	private void updateSkillText() {
		skillLabel.setText(ProblemSuppliers.nameOf(currentProblemSupplier));
	}
	
	private void showAnswerButtonAction() {
		toggleShowAnswer();
		if(isAnswerShowing())
			hasShownAnswer = true;
	}
	
	private void toggleShowAnswer() {
		if(isAnswerShowing()) {
			clearAnswerLabel();
			showAnswer.setText(SHOW_ANSWER_TEXT);
		}
		else {
			answerLabel.setText(currentProblem.answerAsString());
			showAnswer.setText(HIDE_ANSWER_TEXT);
		}
	}
	
	private void clearAnswerLabel() {
		answerLabel.setText("");
	}
	
	private void setLastTime(double timeInNanos) {
		lastTimeLabel.setText("Last Time: " + secString(timeInNanos));
	}

	private String secString(double timeInNanos) {
		return String.format("%.3fs", timeInNanos / 1_000_000_000);
	}

	private boolean canDelete() {
		return deleteText.isSelected();
	}

	private boolean isAnswerShowing() {
		return !showAnswer.getText().equals(SHOW_ANSWER_TEXT);
	}

	private boolean isClearOnWrongAnswer() {
		return clearOnWrongAnswer.isSelected();
	}
	
	private boolean isMarkWrongIfCleared() {
		return markWrongIfCleared.isSelected(); 
	}
	
	private boolean isMarkWrongIfShownAnswer() {
		return markWrongIfShownAnswer.isSelected();
	}

	private boolean isSkillShowing() {
		return !showSkill.getText().equals(SHOW_SKILL_TEXT);
	}
}

