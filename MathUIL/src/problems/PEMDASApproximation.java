package problems;

import java.math.*;
import java.util.*;

import math.Evaluator;

/**
 * @author Sam Hooper
 *
 */
public class PEMDASApproximation implements Problem {
	
	private final BigDecimal result;
	private final String display;
	
	public PEMDASApproximation(String expression) {
		result = Evaluator.evaluateAsBigDecimal(expression);
		display = Problem.prettyExpression(expression);
		System.out.printf("NEW PEMDASPROB: result=%f, display=%s%n", result, display);
	}
	
	public PEMDASApproximation(final int terms, final int minDigits, final int maxDigits, final List<String> operators) {
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
		return result.setScale(2, RoundingMode.HALF_UP).toPlainString();
	}
	
}
