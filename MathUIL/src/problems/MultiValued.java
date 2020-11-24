package problems;

import java.math.*;
import java.util.*;

import math.*;

/**
 * A {@link Problem} with multiple correct answers. Correct answers can be added via the various {@code addResult} methods.
 * A {@link #isCorrect(String) correct} answer must match <i>one or more</i> of the {@link #allAnswers() answers} to this {@code Problem}.
 * {@link #answerAsString()} returns a {@code String} containing all the correct answers, separated by {@code ", "}. By default, {@code MultiValued}
 * {@code Problems} are not {@link #isApproximateResult() approximations}, but that can be changed via {@link #setApproximate(boolean)}.
 * @author Sam Hooper
 *
 */
public class MultiValued implements Problem {
	
	public static final BigDecimal DEFAULT_APPROXIMATION_PERCENT = new BigDecimal("0.05");
	
	public static MultiValued of(final String htmlFormattedText) {
		return of(htmlFormattedText, 1.0);
	}
	public static MultiValued of(final String htmlFormattedText, final double estimatedDisplayLinesNeeded) {
		return new MultiValued(htmlFormattedText, estimatedDisplayLinesNeeded);
	}
	
	private final String display;
	private final Map<Object, Verifier> resultMap;
	private double lines;
	private boolean isApproximate;
	private BigDecimal approximationPercent;
	
	/**
	 * A functional interface that provides a method to verify that some result, given as a {@code String}, is correct.
	 */
	public interface Verifier {
		boolean isValid(String input);
	}
	
	private MultiValued(final String htmlFormattedDisplayText, final double estimatedDisplayLinesNeeded) {
		this.display = htmlFormattedDisplayText;
		this.resultMap = new LinkedHashMap<>();
		this.lines = estimatedDisplayLinesNeeded;
		this.isApproximate = false;
		this.approximationPercent = DEFAULT_APPROXIMATION_PERCENT;
	}
	
	/**
	 * Returns {@code this}.
	 * @throws NullPointerException if {@code result} is {@code null}.
	 */
	public MultiValued addResult(final Complex result) {
		Objects.requireNonNull(result);
		resultMap.put(result, input -> {
			if(isApproximateResult()) {
				if(result.hasImaginaryPart())
					throw new UnsupportedOperationException("Approximation problems whose results have imaginary parts are not supported by MulitValued");
				return Utils.isBigDecimal(input) && Problem.within(approximationPercent, result.bigDecimalValueExact(), new BigDecimal(input));
			}
			else
				return Utils.isComplexInRectangularForm(input) && new Complex(input).equals(result);
		});
		return this;
	}
	
	/**
	 * Returns {@code this}.
	 * @throws NullPointerException if {@code result} is {@code null}.
	 */
	public MultiValued addResult(final BigFraction result) {
		Objects.requireNonNull(result);
		resultMap.put(result, input -> {
			if(isApproximateResult())
				throw new UnsupportedOperationException("Approximations with fractional answers are not supported");
			else
				return BigFraction.isValidVulgar(input) && BigFraction.fromVulgar(input).equals(result);
		});
		return this;
	}
	
	/**
	 * Returns {@code this}.
	 * @throws NullPointerException if {@code result} is {@code null}.
	 */
	public MultiValued addResult(final MixedNumber result) {
		Objects.requireNonNull(result);
		resultMap.put(result, input -> {
			if(isApproximateResult())
				throw new UnsupportedOperationException("Approximations with fractional answers are not supported");
			else {
				String[] split = input.split(" +");
				if(split.length != 2)
					return false;
				return Utils.isInteger(split[0]) && new BigInteger(split[0]).equals(result.getIntegralPart()) && BigFraction.isValidVulgar(split[1]) &&
						BigFraction.fromVulgar(split[1]).equals(result.getFractionalPart());
			}
		});
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
	 * The guess must be {@link String#equals(Object) equal} to {@code result}. Returns {@code this}.
	 * @param result that {@code String} that will be displayed to the user as part of the {@link #answerAsString()}.
	 * @throws NullPointerException if {@code result} or {@code verifier} is {@code null}.
	 */
	public MultiValued addResult(final String result) {
		Objects.requireNonNull(result);
		resultMap.put(result, str -> result.equals(str));
		return this;
	}
	
	public MultiValued setLines(final double newEstimatedDisplayLinesNeeded) {
		this.lines = newEstimatedDisplayLinesNeeded;
		return this;
	}
	
	public MultiValued setApproximate(final boolean isApproximate) {
		this.isApproximate = isApproximate;
		return this;
	}
	
	/**
	 * {@code percent} should be between {@code 0} and {@code 1}.
	 * @param percent
	 * @return
	 */
	public MultiValued setApproximationPercent(final BigDecimal percent) {
		if(BigNumbers.isNegative(percent))
			throw new IllegalArgumentException("Percent cannot be negative");
		this.approximationPercent = percent;
		return this;
	}
	
	@Override
	public double estimatedDisplayLines() {
		return lines;
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
	
	@Override
	public boolean isApproximateResult() {
		return isApproximate;
	}
	@Override
	public BigDecimal approximationPercentAsBigDecimal() {
		return approximationPercent;
	}
	
	
}
