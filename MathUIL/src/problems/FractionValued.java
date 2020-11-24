package problems;

import math.Fraction;

/**
 * @author Sam Hooper
 *
 */
public interface FractionValued extends Problem {
	
	public static FractionValued of(final String htmlFormattedText, final Fraction result) {
		return of(htmlFormattedText, result, 1.5);
	}
	
	public static FractionValued of(final String htmlFormattedText, final Fraction result, final double estimatedDisplayLines) {
		return new FractionValued() {
			
			@Override
			public String displayString() {
				return htmlFormattedText;
			}
			
			@Override
			public Fraction answerAsFraction() {
				return result;
			}

			@Override
			public double estimatedDisplayLines() {
				return estimatedDisplayLines;
			}
			
		};
	}
	
	Fraction answerAsFraction();

	@Override
	default boolean isCorrect(String input) {
		return Fraction.isValidVulgar(input) && Fraction.fromVulgar(input).equals(answerAsFraction());
	}

	@Override
	default String answerAsString() {
		return answerAsFraction().toString();
	}
	
}
