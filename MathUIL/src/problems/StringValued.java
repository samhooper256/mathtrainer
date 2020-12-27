package problems;

import java.util.Objects;

/**
 * @author Sam Hooper
 *
 */
public interface StringValued extends Problem {
	
	public static StringValued of(final String htmlFormattedText, final String answer) {
		Objects.requireNonNull(htmlFormattedText);
		Objects.requireNonNull(answer);
		return new StringValued() {
			
			@Override
			public String displayString() {
				return htmlFormattedText;
			}

			@Override
			public String answerAsString() {
				return answer;
			}
			
		};
	}

	@Override
	default boolean isCorrect(String input) {
		return Objects.equals(input, answerAsString());
	}
	
}
