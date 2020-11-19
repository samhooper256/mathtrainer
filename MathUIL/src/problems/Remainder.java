package problems;

import math.*;

/**
 * @author Sam Hooper
 *
 */
public class Remainder extends ComplexValued {
	
	private final String display;
	
	/**
	 * @param result
	 */
	public Remainder(long operand, long modulus) {
		super(new Complex(operand % modulus));
		display = ExpressionPrettifier.pretty(operand + "/" + modulus) + " has a remainder of: ";
	}
	
	public Remainder(String expression, long modulus) {
		super(Evaluator.evaluateAsLongOrThrow(expression) % modulus);
		display = ExpressionPrettifier.pretty(expression + "/" + modulus) + " has a remainder of " + (result().longValueExact() % modulus);
	}

	@Override
	public String displayString() {
		return display;
	}

}
