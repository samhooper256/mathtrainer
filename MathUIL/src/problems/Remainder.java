package problems;

import java.math.BigDecimal;

import math.*;

/**
 * @author Sam Hooper
 *
 */
public class Remainder extends ComplexValued {
	
	/**
	 * 
	 */
	private static final String REMAINDER_OF_STRING = " has a remainder of:";
	private final String display;
	
	public Remainder(long operand, long divisor) {
		super(new Complex(operand % divisor));
		display = ExpressionPrettifier.pretty(operand + "/" + divisor) + REMAINDER_OF_STRING;
	}
	
	public Remainder(BigDecimal operand, long divisor) {
		this(new Complex(operand), divisor);
	}
	
	public Remainder(Complex operand, long divisor) {
		this(operand, divisor, false);
	}
	
	public Remainder(Complex operand, long divisor, boolean addParenthesis) {
		super(operand.remainder(divisor));
		String operandString = operand.toString();
		if(addParenthesis)
			operandString = "(" + operandString + ")";
		display = ExpressionPrettifier.pretty(operandString + "/" + divisor) + REMAINDER_OF_STRING;
	}
	
	
	/**
	 * This does <b>NOT</b> add parentheses around {@code expression}.
	 */
	public Remainder(String expression, long divisor) {
		super(Evaluator.evaluateAsBigDecimal(expression).remainder(BigDecimal.valueOf(divisor)));
		display = ExpressionPrettifier.pretty(expression + "/" + divisor) + REMAINDER_OF_STRING;
	}

	@Override
	public String displayString() {
		return display;
	}

}
