package problems;

import math.*;

/**
 * @author Sam Hooper
 *
 */
public class Remainder extends ComplexValued {
	
	private final String display;
	
	public Remainder(long operand, long modulus) {
		super(new Complex(operand % modulus));
		display = ExpressionPrettifier.pretty(operand + "/" + modulus) + " has a remainder of: ";
	}
	
	/**
	 * This adds parentheses around {@code expression}.
	 */
	public Remainder(String expression, long modulus) {
		super(Evaluator.evaluateAsLongOrThrow(expression) % modulus);
		display = ExpressionPrettifier.pretty("(" + expression + ")/" + modulus) + " has a remainder of: ";
	}

	@Override
	public String displayString() {
		return display;
	}

}
