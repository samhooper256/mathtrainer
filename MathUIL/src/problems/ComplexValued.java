package problems;

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
	
	/**
	 * @return the answer to this {@link ComplexValued} {@link Problem}.
	 */
	public Complex result() {
		return result;
	}
	
	@Override
	public boolean isCorrect(String input) {
//		System.out.printf("entered ComplexVaued isCorrect%n");
		final boolean result = Problem.isComplexInRectangularForm(input) && new Complex(input).equals(this.result);
//		System.out.printf("]exit ComplexValued isCorrect%n");
		return result;
	}
	
	@Override
	public String answerAsString() {
		return Problem.prettyComplex(result);
	}
}
