package problems;

import java.math.*;

import math.Evaluator;

/**
 * @author Sam Hooper
 *
 */
public class AnyExpression implements Problem {
	
	private final BigDecimal result;
	private final String display;
	
	public AnyExpression(String expression) {
		result = Evaluator.evaluateAsBigDecimal(expression);
		display = Problem.prettyExpression(expression);
	}

	@Override
	public String displayString() {
		return display;
	}

	@Override
	public boolean isCorrect(String input) {
		return Problem.isBigDecimal(input) && new BigDecimal(input).compareTo(result) == 0;
	}

	@Override
	public String answerAsString() {
		return Problem.prettyBigDecimal(result);
	}
	
	
}
