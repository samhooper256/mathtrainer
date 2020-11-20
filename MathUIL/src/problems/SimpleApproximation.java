package problems;

import java.math.*;
import java.util.*;

import math.*;

/**
 * @author Sam Hooper
 *
 */
public class SimpleApproximation implements Problem {
	
	private static final BigDecimal DEFAULT_PERCENT = new BigDecimal("0.05");
	private final BigDecimal result;
	private final String display;
	private final BigDecimal percent;
	
	public SimpleApproximation(String htmlFormattedText, final BigDecimal result) {
		this(DEFAULT_PERCENT, htmlFormattedText, result);
	}
	
	public SimpleApproximation(final BigDecimal approximationPercent, String htmlFormattedText, final BigDecimal result) {
		this.result = result;
		display = htmlFormattedText;
		percent = approximationPercent;
	}
	
	public SimpleApproximation(String expression) {
		this(DEFAULT_PERCENT, expression);
	}
	
	public SimpleApproximation(final BigDecimal approximationPercent, String expression) {
		result = Evaluator.evaluateAsBigDecimal(expression);
		display = Problem.prettyExpression(expression);
		percent = approximationPercent;
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
		return Utils.isBigDecimal(input) && Problem.within(approximationPercentAsBigDecimal(), result, new BigDecimal(input));
	}

	@Override
	public String answerAsString() {
		return Problem.prettyBigDecimal(result);
	}

	@Override
	public boolean isApproximateResult() {
		return true;
	}

	@Override
	public BigDecimal approximationPercentAsBigDecimal() {
		return percent;
	}

	
	
	
	
}
