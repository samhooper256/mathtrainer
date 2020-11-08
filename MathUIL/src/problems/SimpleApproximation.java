package problems;

import java.math.*;
import java.util.*;

import math.Evaluator;

/**
 * @author Sam Hooper
 *
 */
public class SimpleApproximation implements Problem {
	
	private final BigDecimal result;
	private final String display;
	
	public SimpleApproximation(String expression) {
		result = Evaluator.evaluateAsBigDecimal(expression);
		display = Problem.prettyExpression(expression);
	}
	
	public SimpleApproximation(final int terms, final int minDigits, final int maxDigits, final List<String> operators) {
		this(Problem.makeExpr(terms, minDigits, maxDigits, operators));
	}

	@Override
	public String displayString() {
		return display;
	}

	@Override
	public boolean isCorrect(String input) {
		return Problem.isBigDecimal(input) && Problem.within5(result, new BigDecimal(input));
	}

	@Override
	public String answerAsString() {
		return Problem.prettyBigDecimal(result);
	}
	
}
