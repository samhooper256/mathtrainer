package problems;

import java.math.BigDecimal;

import math.*;
/**
 * @author Sam Hooper
 *
 */
public abstract class ComplexValued implements Problem {
	
	/**
	 * Returns a new {@link ComplexValued} that will be displayed as the given formatted html text directly.
	 * The {@link #result() result} is the given {@link Complex} value.
	 */
	public static ComplexValued of(final String htmlFormattedText, final Complex result) {
		
		return new ComplexValued(result) {
			@Override
			public String displayString() {
				return htmlFormattedText;
			}
			
		};
		
	}
	
	private final Complex result;
	
	public ComplexValued(final Complex result) {
		this.result = result;
	}
	
	public ComplexValued(final BigDecimal result) {
		this.result = new Complex(result);
	}
	
	public ComplexValued(final long result) {
		this.result = new Complex(result);
	}
	
	/**
	 * @return the answer to this {@link ComplexValued} {@link Problem}.
	 */
	public Complex result() {
		return result;
	}
	
	@Override
	public boolean isCorrect(String input) {
		final boolean result = Utils.isComplexInRectangularForm(input) && new Complex(input).equals(this.result);
		return result;
	}
	
	@Override
	public String answerAsString() {
		return Problem.prettyComplex(result);
	}

	@Override
	public String toString() {
		return displayString();
	}
	
}
