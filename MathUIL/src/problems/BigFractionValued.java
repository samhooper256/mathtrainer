package problems;

import math.*;

/**
 * @author Sam Hooper
 *
 */
public interface BigFractionValued extends Problem {
	
	public static BigFractionValued of(final String htmlFormattedText, final BigFraction result) {
		return of(htmlFormattedText, result, 1.5);
	}
	
	public static BigFractionValued of(final String htmlFormattedText, final BigFraction result, final double estimatedDisplayLines) {
		return new BigFractionValued() {
			
			@Override
			public String displayString() {
				return htmlFormattedText;
			}
			
			@Override
			public BigFraction answerAsFraction() {
				return result;
			}

			@Override
			public double estimatedDisplayLines() {
				return estimatedDisplayLines;
			}
			
		};
	}
	
	BigFraction answerAsFraction();

	@Override
	default boolean isCorrect(String input) {
		return BigFraction.isValidVulgar(input) && BigFraction.fromVulgar(input).equals(answerAsFraction());
	}

	@Override
	default String answerAsString() {
		return answerAsFraction().toString();
	}
	
}
