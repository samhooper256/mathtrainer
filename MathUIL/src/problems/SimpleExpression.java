package problems;

import java.math.*;
import java.util.*;

import math.Evaluator;
import suppliers.*;
import utils.IntRange;

/**
 * @author Sam Hooper
 *
 */
public class SimpleExpression implements Problem {
	
	public static SimpleExpression multiplyTerms(int... terms) {
		StringJoiner j = new StringJoiner("*");
		for(int term : terms)
			j.add(String.valueOf(term));
		return new SimpleExpression(j.toString());
	}
	public static SimpleExpression of(int minTerms, int maxTerms, int minDigits, int maxDigits, String... ops) {
		if(ops.length == 0)
			throw new IllegalArgumentException("Must have at least one operator. ops.length == 0");
		final int terms = Problem.intInclusive(minTerms, maxTerms);
		StringBuilder sb = new StringBuilder();
		for(int term = 1; term < terms; term++)
			sb.append(Problem.intWithDigits(Problem.intInclusive(minDigits, maxDigits))).append(ops[Problem.intInclusive(0, ops.length - 1)]);
		sb.append(Problem.intWithDigits(Problem.intInclusive(minDigits, maxDigits)));
		return new SimpleExpression(sb.toString());
	}
	
	public static SimpleExpression of(IntRange termRange, int minDigits, int maxDigits, String... ops) {
		return of(termRange.getLow(), termRange.getHigh(), minDigits, maxDigits, ops);
	}

	public static SimpleExpression of(NamedSetting<IntRange> termRange, int minDigits, int maxDigits, String... ops) {
		return of(termRange, minDigits, maxDigits, ops);
	}
	
	private final BigDecimal result;
	private final String display;
	
	public SimpleExpression(String expression) {
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
