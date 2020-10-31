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

/**
 * @author Sam Hooper
 *
 */
public class ProblemPane extends StackPane {
	
	/**
	 * Number of the user's most recent problem attempts whose times will be kept in temporary storage.
	 */
	private static final int RESULTS_TRACKED = 100;

	private static final Font DEFAULT_PROBLEM_FONT = Font.font("Times New Roman", FontWeight.BOLD, 16);
	
	private final FixedDoubleQueue times = new FixedDoubleQueue(RESULTS_TRACKED);
	private final FixedBooleanQueue accuracies = new FixedBooleanQueue(RESULTS_TRACKED);
	private final CompositeProblemSupplier problemSupplier;
	private final Label problemLabel, answerLabel, lastTimeLabel, averageTimeLabel, averageAccuracyLabel;
	private final TextField field;
	private final HBox buttonBox;
	private final Button submit, clear, showAnswer;
	private final CheckBox clearOnWrongAnswer, deleteText;
	private double startTime = -1;
	private int wrongAnswers;
	private Problem currentProblem;
	
	public ProblemPane(final CompositeProblemSupplier problemSupplier) {
		this.problemSupplier = Objects.requireNonNull(problemSupplier);
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
		
		
		generateProblemAndUpdateLabel();
		this.wrongAnswers = 0;
		startTime = System.nanoTime();
	}
	
	public void updateAccuracies() {
		accuracies.addFirst(wrongAnswers == 0);
		averageAccuracyLabel.setText(String.format("Last %d Accuracy: %.1f%%", accuracies.size, accuracies.truthProportion() * 100));
	}

	public CompositeProblemSupplier getSupplier() {
		return problemSupplier;
	}

	/**
	 * Does nothing if the input {@link String#isBlank() is blank}.
	 */
	private void acceptInput() {
		final String input = field.getText().strip();
		if(input.isBlank())
			return;
		acceptInput(input);
	}

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
	 * Returns {@code true} if the given string is a valid number and the correct answer to the {@link #currentProblem}, {@code false} otherwise.
	 */
	private boolean validate(final String inputString) {
		return currentProblem.isCorrect(inputString);
	}
	
	/**
	 * Updates {@link #startTime}.
	 */
	private void setupNextProblem() {
		updateResults();
		clearInputField();
		field.setBorder(null);
		clearAnswerLabel();
		generateProblemAndUpdateLabel();
		wrongAnswers = 0;
		startTime = System.nanoTime();
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
