package base;

import java.util.*;
import java.util.function.*;

import fxutils.*;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.text.*;
import problems.*;
import suppliers.*;
import utils.*;

/**
 * @author Sam Hooper
 *
 */
public class ProblemPane extends StackPane {
	
	/**
	 * Number of the user's most recent problem attempts whose times and accuracies will be kept
	 * in temporary storage (and displayed to the user).
	 */
	private static final int RESULTS_TRACKED = 100;
	private static final Font DEFAULT_PROBLEM_FONT = Font.font("Times New Roman", FontWeight.BOLD, 16);
	
	private final FixedDoubleQueue times;
	private final FixedBooleanQueue accuracies;
	private final CompositeProblemSupplier problemSupplier;
	private final Label problemLabel, answerLabel, lastTimeLabel, averageTimeLabel, averageAccuracyLabel;
	/**
	 * The {@link TextField} where the user will type their answer.
	 */
	private final TextField field;
	private final HBox buttonBox;
	private final Button submit, clear, showAnswer;
	private final CheckBox clearOnWrongAnswer, deleteText;
	private final Set<Class<? extends ProblemSupplier>> supplierClasses;
	
	private double startTime;
	/**
	 * The number of wrong answers the user has submitted for the current {@link Problem}.
	 */
	private int wrongAnswers;
	private Problem currentProblem;
	
	public ProblemPane(final CompositeProblemSupplier problemSupplier) {
		this.problemSupplier = Objects.requireNonNull(problemSupplier);
		this.times = new FixedDoubleQueue(RESULTS_TRACKED);
		this.accuracies = new FixedBooleanQueue(RESULTS_TRACKED);
		
		problemLabel = Labels.of(DEFAULT_PROBLEM_FONT); //gets text in the generateProblem method
		answerLabel = new Label();
		this.lastTimeLabel = new Label();
		this.averageTimeLabel = new Label();
		this.averageAccuracyLabel = new Label();
		submit = Buttons.of("Submit", () -> acceptInput());
		showAnswer = Buttons.of("Show Answer", this::showAnswer);
		clear = Buttons.of("Clear", this::clearInputField);
		buttonBox = new HBox(4, submit, clear, showAnswer, answerLabel);
		buttonBox.setAlignment(Pos.CENTER);
		field = new TextField();
		field.maxWidthProperty().bind(this.widthProperty().divide(2));
		field.addEventFilter(KeyEvent.KEY_PRESSED, keyEvent -> {
			if(!this.canDelete()) {
				switch(keyEvent.getCode()) {
					case BACK_SPACE, DELETE -> keyEvent.consume();
				}
			}
		});
		setOnKeyPressed(keyEvent -> {
			switch(keyEvent.getCode()) {
				case ENTER -> acceptInput();
			}
		});
		deleteText = new CheckBox("Can delete text");
		deleteText.setSelected(true);
		VBox vBox = new VBox(10, problemLabel, field, buttonBox, deleteText, clearOnWrongAnswer = new CheckBox("Clear on wrong answer"));
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
		startTime = System.nanoTime();
	}
	
	/**
	 * Sets the elapsed time spent on the current {@link Problem} to zero (from which it immediately starts increasing as time passes).
	 */
	public void resetCurrentProblemTimer() {
		startTime = System.nanoTime();
	}
	
	public CompositeProblemSupplier getSupplier() {
		return problemSupplier;
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

	public boolean hasSupplierOfClass(Class<? extends ProblemSupplier> clazz) {
		return supplierClasses.contains(clazz);
	}

	/**
	 * Returns {@code true} if the given {@link ProblemSupplier} has been added as one of the suppliers that will generate this {@link ProblemPane ProblemPane's}
	 * {@link Problem Problems}, {@code false} otherwise. Note that {@link ProblemPane ProblemPanes} do not allow for more than one
	 * {@link ProblemSupplier ProblemSuppliers} of the same {@link Class}.
	 */
	private boolean addSupplier(ProblemSupplier supplier) {
		boolean added = supplierClasses.add(supplier.getClass());
		if(added)
			problemSupplier.addSupplier(supplier);
		return added;
	}

	private void updateAccuracies() {
		accuracies.addFirst(wrongAnswers == 0);
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
		clearAnswerLabel();
		generateProblemAndUpdateLabel();
		wrongAnswers = 0;
		resetCurrentProblemTimer();
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
	
	private void clearInputField() {
		field.clear();
	}
	
	private void generateProblemAndUpdateLabel() {
		generateProblem();
		updateLabel();
	}

	private void generateProblem() {
		currentProblem = problemSupplier.get();
	}

	private void updateLabel() {
		problemLabel.setText(currentProblem.displayString());
	}
	
	private boolean isClearOnWrongAnswer() {
		return clearOnWrongAnswer.isSelected();
	}
	
	private boolean canDelete() {
		return deleteText.isSelected();
	}
	
	private void showAnswer() {
		answerLabel.setText(currentProblem.answerAsString());
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
}
