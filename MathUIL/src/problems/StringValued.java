package problems;

import math.Fraction;

/**
 * @author Sam Hooper
 *
 */
public interface StringValued extends Problem {
	public static StringValued of(final String htmlFormattedText, final String answer) {
		return of(htmlFormattedText, answer, 1.0);
	}
	
	public static StringValued of(final String htmlFormattedText, final String answer, final double estimatedDisplayLines) {
		return new StringValued() {
			
			@Override
			public String displayString() {
				return htmlFormattedText;
			}

			@Override
			public double estimatedDisplayLines() {
				return estimatedDisplayLines;
			}

			@Override
			public String answerAsString() {
				return answer;
			}
			
		};
	}

	@Override
	default boolean isCorrect(String input) {
		return input.equals(answerAsString());
	}
	
}
