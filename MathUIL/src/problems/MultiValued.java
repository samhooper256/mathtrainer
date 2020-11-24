package problems;

import java.util.*;

import math.*;

/**
 * A {@link Problem} with multiple correct answers. Correct answers can be added via the various {@code addResult} methods.
 * A {@link #isCorrect(String) correct} answer must match <i>one or more</i> of the {@link #allAnswers() answers} to this {@code Problem}.
 * {@link #answerAsString()} returns a {@code String} containing all the correct answers, separated by {@code ", "}.
 * @author Sam Hooper
 *
 */
public class MultiValued implements Problem {
	
	
	public static MultiValued of(final String htmlFormattedText) {
		return new MultiValued(htmlFormattedText);
	}
	
	private final String display;
	private final Map<Object, Verifier> resultMap;
	
	/**
	 * A functional interface that provides a method to verify that some result, given as a {@code String}, is correct.
	 */
	public interface Verifier {
		boolean isValid(String input);
	}
	
	private MultiValued(final String htmlFormattedDisplayText) {
		this.display = htmlFormattedDisplayText;
		this.resultMap = new LinkedHashMap<>();
	}
	
	/**
	 * Returns {@code this}.
	 * @throws NullPointerException if {@code result} is {@code null}.
	 */
	public MultiValued addResult(final Complex result) {
		Objects.requireNonNull(result);
		resultMap.put(result, input -> Utils.isComplexInRectangularForm(input) && new Complex(input).equals(result));
		return this;
	}
	
	/**
	 * Returns {@code this}.
	 * @throws NullPointerException if {@code result} is {@code null}.
	 */
	public MultiValued addResult(final BigFraction result) {
		Objects.requireNonNull(result);
		resultMap.put(result, input -> BigFraction.isValidVulgar(input) && BigFraction.fromVulgar(input).equals(result));
		return this;
	}
	
	/**
	 * Returns {@code this}.
	 * @param result that {@code String} that will be displayed to the user as part of the {@link #answerAsString()}.
	 * @param verifier a function that will verify whether a user's guess "matches" (however that may be defined) {@code result}.
	 * @throws NullPointerException if {@code result} or {@code verifier} is {@code null}.
	 */
	public MultiValued addResult(final String result, Verifier verifier) {
		Objects.requireNonNull(result);
		Objects.requireNonNull(verifier);
		resultMap.put(result, verifier);
		return this;
	}
	
	/**
	 * Returns a {@link Set} containing all the correct answers to this {@link MultiValued} {@link Problem}.
	 */
	public Set<Object> allAnswers() {
		return resultMap.keySet();
	}
	
	@Override
	public String displayString() {
		return display;
	}

	@Override
	public boolean isCorrect(String input) {
		for(Verifier v : resultMap.values())
			if(v.isValid(input))
				return true;
		return false;
	}

	@Override
	public String answerAsString() {
		StringJoiner j = new StringJoiner(", ");
		for(Object o : resultMap.keySet())
			j.add(o.toString());
		return j.toString();
	}
	
	
}
