package base;

import java.util.*;

import fxutils.*;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.web.WebView;
import problems.*;
import suppliers.*;
import utils.*;

/**
 * @author Sam Hooper
 *
 */
//TODO The correct answer to approximation problems should show as a range of values (not a single one)
//TODO Approximation questions should somehow indicate what percent your answer must be within
//TODO Approx problems shouldn't show so many decimals!
public class ProblemPane extends Pane {
	
	private static final Border FIELD_RED_BORDER = Borders.of(Color.RED);
	private static final Border FIELD_EMPTY_BORDER = Borders.of(Color.TRANSPARENT);

	private static final String PROBLEM_VIEW_CSS_FILENAME = "problemview.css";
	
	
	private static final String DEFAULT_LAST_TIME_TEXT = "Last Time: N/A", DEFAULT_AVERAGE_TIME_TEXT = "Average Time: N/A",
			DEFAULT_AVERAGE_ACCURACY_TEXT = "Average Accuracy: N/A";
	private static final char CLEAR_CHAR = 'c', SHOW_SKILL_CHAR = 's', SHOW_ANSWER_CHAR = 'a';
	private static final String SUBMIT_TEXT = "Submit",
			CLEAR_TEXT = String.format("Clear (%C)", CLEAR_CHAR),
			SHOW_SKILL_TEXT = String.format("Show Skill (%C)", SHOW_SKILL_CHAR),
			HIDE_SKILL_TEXT = String.format("Hide Skill (%C)", SHOW_SKILL_CHAR),
			SHOW_ANSWER_TEXT = String.format("Show Answer (%C)", SHOW_ANSWER_CHAR),
			HIDE_ANSWER_TEXT = String.format("Hide Answer (%C)", SHOW_ANSWER_CHAR);
	private static final Image APPROX_IMAGE = Images.getImage("approx.png");
	
	
	private final FixedDoubleQueue times;
	private final FixedBooleanQueue accuracies;
	private final CompositeProblemSupplier compositeSupplier;
	private final WebView problemView;
	private final Label answerLabel, lastTimeLabel, averageTimeLabel, averageAccuracyLabel, skillLabel;
	/** The {@link TextField} where the user will type their answer. */
	private final TextField field;
	private final HBox buttonBox;
	private final Button submit, clear, showSkill, showAnswer, resetResults;
	private final CheckBox deleteText, markWrongIfCleared, markWrongIfShownAnswer, clearOnWrongAnswer;
	private final ImageWrap approxWrap;
	private final StackPane root, problemViewWrap;
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
	/** Number of the user's most recent problem attempts whose times and accuracies will be kept in temporary storage (and displayed to the user). */
	private int resultsTracked = 100;
	private boolean hasShownAnswer;
	private Problem currentProblem;
	private ProblemSupplier currentProblemSupplier;
	
	public ProblemPane(final CompositeProblemSupplier problemSupplier) {
		compositeSupplier = Objects.requireNonNull(problemSupplier);
		times = new FixedDoubleQueue(resultsTracked);
		accuracies = new FixedBooleanQueue(resultsTracked);
		
		root = new StackPane();
		answerLabel = new Label();
		lastTimeLabel = new Label(DEFAULT_LAST_TIME_TEXT);
		averageTimeLabel = new Label(DEFAULT_AVERAGE_TIME_TEXT);
		averageAccuracyLabel = new Label(DEFAULT_AVERAGE_ACCURACY_TEXT);
		resetResults = Buttons.of("Reset", this::resetResults);
		submit = Buttons.of(SUBMIT_TEXT, () -> acceptInput());
		showAnswer = Buttons.of(SHOW_ANSWER_TEXT, this::showAnswerButtonAction);
		clear = Buttons.of(CLEAR_TEXT, this::clearButtonAction);
		showSkill = Buttons.of(SHOW_SKILL_TEXT, this::showSkillButtonAction);
		buttonBox = new HBox(4, submit, clear, showSkill, showAnswer, answerLabel);
		field = new TextField();
		problemView = new WebView();
		problemViewWrap = new StackPane(problemView);
		skillLabel = new Label();
		deleteText = new CheckBox("Can delete text");
		markWrongIfCleared = new CheckBox("Mark wrong if cleared or deleted");
		markWrongIfShownAnswer = new CheckBox("Mark wrong if shown answer");
		clearOnWrongAnswer = new CheckBox("Clear on wrong answer");
		supplierClasses = new HashSet<>();
		approxWrap = new ImageWrap(APPROX_IMAGE, 0, 0);
		initInputField();
		initProblemView();
		initCompositeSupplier();
		initOptions();
		finishInit();
		freshProblem();
	}

	private void initOptions() {
		skillLabel.setWrapText(true);
		skillLabel.setVisible(false);
		deleteText.setSelected(true);
		markWrongIfCleared.setSelected(true);
		markWrongIfShownAnswer.setSelected(true);
	}
	
	private void initProblemView() {
		problemView.getEngine().setUserStyleSheetLocation(getClass().getResource(PROBLEM_VIEW_CSS_FILENAME).toString());
		problemViewWrap.prefWidthProperty().bind(field.widthProperty());
		setOnKeyPressed(this::paneKeyHandler);
	}
	
	private void finishInit() {
		buttonBox.setAlignment(Pos.CENTER);
		VBox vBox = new VBox(10, problemViewWrap, field, buttonBox, deleteText, markWrongIfCleared, markWrongIfShownAnswer, clearOnWrongAnswer, skillLabel);
		vBox.setAlignment(Pos.CENTER);
		HBox resultsBox = new HBox(10, lastTimeLabel, averageTimeLabel, averageAccuracyLabel, resetResults);
		AnchorPane anchor = new AnchorPane(resultsBox);
		anchor.setPickOnBounds(false);
		AnchorPane.setBottomAnchor(resultsBox, 10d);
		AnchorPane.setLeftAnchor(resultsBox, 10d);
		AnchorPane.setRightAnchor(resultsBox, 10d);
		
		root.getChildren().addAll(vBox, anchor);
		root.prefWidthProperty().bind(this.widthProperty());
		root.prefHeightProperty().bind(this.heightProperty());
		getChildren().add(root);
		
		StackPane approxStack = new StackPane(approxWrap);
		approxStack.prefHeightProperty().bind(field.heightProperty());
		approxStack.prefWidthProperty().bind(approxStack.prefHeightProperty());
		approxStack.layoutXProperty().bind(field.layoutXProperty().subtract(approxStack.widthProperty()));
		approxStack.layoutYProperty().bind(field.layoutYProperty());
		approxWrap.setVisible(false);
		getChildren().add(approxStack);
	}

	private void initCompositeSupplier() {
		for(ProblemSupplier ps : compositeSupplier.suppliers())
			supplierClasses.add(ps.getClass());
		compositeSupplier.suppliers().addAddListener(ps -> supplierClasses.add(ps.getClass()));
		compositeSupplier.suppliers().addRemoveListener(ps -> supplierClasses.remove(ps.getClass()));
	}

	

	private void initInputField() {
		field.setBorder(FIELD_EMPTY_BORDER);
		field.maxWidthProperty().bind(root.widthProperty().divide(2));
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
			if(c >= 'a' && c <= 'z')
				keyEvent.consume();
			if(c == CLEAR_CHAR)
				clearButtonAction();
			else if(c == SHOW_ANSWER_CHAR)
				showAnswerButtonAction();
			else if(c == SHOW_SKILL_CHAR)
				showSkillButtonAction();
		});
	}

	/**
	 * Sets the elapsed time spent on the current {@link Problem} to zero (from which it immediately starts increasing as time passes).
	 */
	public void resetCurrentProblemTimer() {
		startTime = System.nanoTime();
	}
	
	public CompositeProblemSupplier getCompositeSupplier() {
		return compositeSupplier;
	}

	/**
	 * Adds the given {@link ProblemSupplier} to this {@link ProblemPane ProblemPane's} {@link #getCompositeSupplier() composite supplier}.
	 * Throws an exception if it cannot be added because a {@link ProblemSupplier} of the given supplier's class is already present.
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
	 * Accepts the input that is currently in the {@link #field}. Does nothing if the input {@link String#isBlank() is blank}; otherwise,
	 * passes the {@link String#strip() stripped} version of the input {@code String} to {@link #acceptInput(String)}.
	 */
	private void acceptInput() {
		final String input = field.getText().strip();
		if(input.isBlank())
			return;
		acceptInput(input);
	}
	
	/**
	 * Accepts the given input, {@link #setupNextProblem() setting up the next problem} if the input is a correct answer 
	 * or {@link #wrongAnswerSubmitted() processing it as a wrong answer} otherwise.
	 */
	private void acceptInput(final String inputString) {
		if(isCorrectAnswerToCurrentProblem(inputString))
			setupNextProblem();
		else
			wrongAnswerSubmitted();
	}
	/**
	 * Called when a wrong answer was submitted
	 */
	private void wrongAnswerSubmitted() {
		field.setBorder(FIELD_RED_BORDER);
		if(isClearOnWrongAnswer())
			clearInputField();
		wrongAnswers++;
	}
	
	/**
	 * Returns {@code true} if the given string is a correct answer to the {@link #currentProblem}, {@code false} otherwise.
	 */
	private boolean isCorrectAnswerToCurrentProblem(final String inputString) {
		return currentProblem.isCorrect(inputString);
	}
	
	/**
	 * Sets up this {@link ProblemPane} with a new {@link Problem}, which involves updating times and accuracies, displaying the new problem to the user,
	 * and clearing the input field. Updates {@link #startTime}.
	 */
	private void setupNextProblem() {
//		System.out.printf("enter setupNextProblem()%n");
		updateResults();
		clearInputField();
		field.setBorder(FIELD_EMPTY_BORDER);
		hideAnswerIfShowing();
		freshProblem();
//		System.out.printf("exit setupNextProblem()%n");
	}
	
	/**
	 * Generates and displays a new {@link Problem}. Does not {@link #updateResults() update results} from a previous problem (if there has been one).
	 */
	private void freshProblem() {
//		System.out.printf("enter freshProblem()%n");
		generateProblemAndUpdateLabel();
		updateSkillText();
		wrongAnswers = 0;
		hasDeletedText = false;
		hasShownAnswer = false;
//		System.out.printf("fresh is approx ? %b%n", currentProblem.isApproximateResult());
		approxWrap.setVisible(currentProblem instanceof NumericProblem && ((NumericProblem) currentProblem).isApproximateResult());
		resetCurrentProblemTimer();
//		System.out.printf("]exit freshProblem()%n");
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
//		System.out.printf("enter generateProblemAndUpdateLabel%n");
		generateProblem();
		updateLabel();
//		System.out.printf("]exit generateProblemAndUpdateLabel%n");
	}

	private void generateProblem() {
		currentProblemSupplier = compositeSupplier.getRandomSupplier();
		currentProblem = currentProblemSupplier.get();
	}

	private void updateLabel() {
		assert currentProblem.displayString() != null;
		problemView.getEngine().loadContent("<html><body style=\"display: flex; align-items: flex-end; flex-wrap: wrap;\">"
				+ "<div style=\"width: 100%;\">" + currentProblem.displayString() + "</div></body></html>");
		problemViewWrap.setPrefHeight(300);
	}
	
	private void showSkillButtonAction() {
		toggleSkillShowing();
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
	
	/**
	 * This is the method called for the {@link #showAnswer} button, whether or not it is currently display {@link #SHOW_ANSWER_TEXT} or {@link #HIDE_ANSWER_TEXT}.
	 */
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
	
	private void resetResults() {
		lastTimeLabel.setText(DEFAULT_LAST_TIME_TEXT);
		averageAccuracyLabel.setText(DEFAULT_AVERAGE_ACCURACY_TEXT);
		averageTimeLabel.setText(DEFAULT_AVERAGE_TIME_TEXT);
		times.clear();
		accuracies.clear();
		resetCurrentProblemTimer();
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
	
	/** Returns the number of the user's most resent results this {@link ProblemPane} is tracking. */
	public int resultsTracked() {
		return resultsTracked;
	}
	
	public void setResultsTracked(final int tracked) {
		times.changeCapacityTo(tracked);
		accuracies.changeCapacityTo(tracked);
		if(times.isEmpty())
			averageTimeLabel.setText(DEFAULT_AVERAGE_TIME_TEXT);
		if(accuracies.isEmpty())
			averageAccuracyLabel.setText(DEFAULT_AVERAGE_ACCURACY_TEXT);
	}
}

