package problems;

import java.math.*;
import java.util.*;

import math.*;
import suppliers.*;
import utils.refs.IntRange;

/**
 * <p>A {@link Problem} where the user must evaluate a simple mathematical expression. This class is capable of representing expressions
 * containing addition (+), subtraction (-), negation/negative numbers (-), multiplication(*), division(/), exponentiation (^), <b>only</b>. The
 * expression may not have variables. It cannot contain more complicated things like derivatives, integrals, matrices, vectors, etc.</p>
 * @author Sam Hooper
 *
 */
public class SimpleExpression extends ComplexValued {
	
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
		return of(termRange.ref(), minDigits, maxDigits, ops);
	}
	
	private final String display;
	
	/**
	 * Creates a new {@link SimpleExpression} where the user must evaluate the given expression.
	 * @param expression the expression that this {@link SimpleExpression} must represent. The expression must use +,-,*,/, and ^ symbols
	 * to represent mathematical operators.
	 */
	public SimpleExpression(String expression) {
		super(new Complex(Evaluator.evaluateAsBigDecimalExact(expression)));
		display = Problem.prettyExpression(expression);
	}

	@Override
	public String displayString() {
		return display;
	}
	
}
