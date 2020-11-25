package problems;

import java.math.*;
import java.util.*;

import math.*;
import utils.Strings;

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
	
	/**
	 * <p>Adds a result to this {@link Problem} that requires the user's guess to be accurate up to {@code numDigits} decimal places of the given
	 * {@code String}. If the guess is accurate up to {@code numDigits} digits after the decimal but has further characters after that, it is still
	 * counted correct. The part of the guess before the decimal point must be {@link String#equals(Object) equal} the part of the given {@code String}
	 * before the decimal point after leading zeros have been ignored.</p>
	 * <p>Returns {@code this}.</p>
	 * @param s the given {@code String} that the guess must match up to {@code numDigits} decimal places. Must contain a decimal point ('.').
	 * @throws NullPointerException if the {@code String} is {@code null}.
	 * @throws IllegalArgumentException if {@code (numDigits <= 0)}.
	 * @throws IllegalArgumentException if the {@code String} does not contain a decimal point ('.').
	 */
	public MultiValued addMinimumDigitsAfterDecimalResult(String s, final int numDigits) {
		Objects.requireNonNull(s);
		if(numDigits <= 0)
			throw new IllegalArgumentException("numDigits <= 0");
		final int dotIndex = s.indexOf('.');
		if(dotIndex < 0)
			throw new IllegalArgumentException("The given String has no decimal point");
		final String result;
		if(s.length() - dotIndex  - 1 < numDigits)
			result = s + "0".repeat(dotIndex + numDigits - s.length() + 1);
		else
			result = s;
		String beforeDecimal = Strings.stripLeading(result.substring(0, dotIndex), '0');
		String decimalDigits = Strings.stripTrailing(result.substring(dotIndex + 1, dotIndex + numDigits + 1), '0');
		resultMap.put(s, str -> {
			if(isApproximateResult())
				throw new UnsupportedOperationException("Minimum digits after decimal answers are not supported for approximate result Problems");
			else {
				final int strDot = str.indexOf('.');
				if(strDot < 0)
					return decimalDigits.length() == 0 &&  Strings.stripLeading(str, '0').equals(beforeDecimal);
				else
					return Strings.stripLeading(str.substring(0, strDot), '0').equals(beforeDecimal) && str.substring(strDot + 1).startsWith(decimalDigits);
			}
		});
		return this;
	}
	
	/**
	 * <p>{@code number} must be real number greater than or equal to zero.
	 * {@code baseOfNumber} must be between {@link Utils#MIN_RADIX} and {@link Utils#MAX_RADIX} (inclusive).
	 * This method does not verify that {@code number} is a valid number in the specified radix.</p>
	 * <p>Returns {@code this}.</p>
	 * @param number
	 * @param baseOfNumber
	 * @throws NullPointerException if {@code number} is {@code null}
	 */
	public MultiValued addBaseResult(String number, final int baseOfNumber) {
		Objects.requireNonNull(number);
		if(baseOfNumber < Utils.MIN_RADIX || baseOfNumber > Utils.MAX_RADIX)
			throw new IllegalArgumentException("Invalid radix: " + baseOfNumber);
		number = Strings.stripLeading(number, '0');
		if(number.contains("."))
			number = Strings.stripTrailing(number, '0');
		final String result = number;
		resultMap.put(result, str -> {
			if(isApproximateResult())
				throw new UnsupportedOperationException("Base questions with approximate answers are not supported");
			else {
				String formattedAnswer = Strings.stripLeading(str, '0');
				if(formattedAnswer.contains("."))
					formattedAnswer = Strings.stripTrailing(formattedAnswer, '0');
				return formattedAnswer.equalsIgnoreCase(result);
			}
		});
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
