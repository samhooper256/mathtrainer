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
	
	private static final Font DEFAULT_PROBLEM_FONT = Font.font("Times New Roman", FontWeight.BOLD, 16);
	
	private final FixedQueue<Double> times;
	private final Supplier<? extends Problem> problemSupplier;
	private final Label problemLabel, answerLabel;
	private final TextField field;
	private final HBox buttonBox;
	private final Button submit, clear, showAnswer;
	private final CheckBox clearOnWrongAnswer, deleteText;
	
	private Problem currentProblem;
	
	public ProblemPane(final Supplier<? extends Problem> problemSupplier) {
		this.times = new FixedQueue<>(100);
		this.problemSupplier = Objects.requireNonNull(problemSupplier);
		problemLabel = Labels.of(DEFAULT_PROBLEM_FONT); //gets text in the generateProblem method
		answerLabel = new Label();
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
		generateProblemAndUpdateLabel();
		getChildren().add(vBox);
	}
	
	private void acceptInput() {
		acceptInput(field.getText().strip());
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
	}
	
	/**
	 * Returns {@code true} if the given string is a valid number and the correct answer to the {@link #currentProblem}, {@code false} otherwise.
	 */
	private boolean validate(final String inputString) {
		return currentProblem.isCorrect(inputString);
	}
	
	private void setupNextProblem() {
		clearInputField();
		field.setBorder(null);
		clearAnswerLabel();
		generateProblemAndUpdateLabel();
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
	
	public Supplier<? extends Problem> getSupplier() {
		return problemSupplier;
	}
}
