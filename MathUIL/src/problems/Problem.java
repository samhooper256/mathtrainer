package problems;

import java.math.*;
import java.util.*;
import java.util.function.*;
import java.util.regex.Pattern;

import math.Complex;
import suppliers.*;
import utils.*;
import utils.refs.IntRange;

/**
 * <p>A problem that the user can attempt to solve. Any {@link Problem} that will be displayed to the user must have a
 * {@link ProblemSupplier} that supplies it.</p>
 * 
 * <p><b>All {@code Problems} are immutable.</b></p>
 * 
 * @author Sam Hooper
 *
 */
public interface Problem {
	
	public static final IntSupplier DIGIT_SUPPLIER = () -> (int) (Math.random() * 10);
	
	public static final Random RAND = new Random();
	
	public static boolean within(final BigDecimal bound, final BigDecimal target, final BigDecimal guess) {
		final BigDecimal boundPercent = target.multiply(bound).abs();
		final BigDecimal diff = target.subtract(guess).abs();
		return diff.compareTo(boundPercent) <= 0;
	}
	
	public static int intInclusive(int min, int max) {
		if(min > max)
			throw new IllegalArgumentException(String.format("min > max (%d < %d)", min, max));
		return (int) (Math.random() * (max - min + 1)  + min);
	}
	
	public static long longInclusive(long min, long max) {
		if(min > max)
			throw new IllegalArgumentException(String.format("min > max (%d < %d)", min, max));
		return (long) (Math.random() * (max - min + 1)  + min);
	}
	
	public static int intInclusive(IntRange range) {
		return intInclusive(range.getLow(), range.getHigh());
	}
	
	public static int intInclusive(NamedSetting<IntRange> range) {
		return intInclusive(range.ref().getLow(), range.ref().getHigh());
	}
	
	public static int intWithDigits(int minDigitsInclusive, int maxDigitsInclusive) {
		return intWithDigits(Problem.intInclusive(minDigitsInclusive, maxDigitsInclusive));
	}
	
	public static int intWithDigits(NamedSetting<IntRange> range) {
		return intWithDigits(range.ref().getLow(), range.ref().getHigh());
	}
	
	public static int intWithDigits(final int digits) {
		int num = 0;
		for(int i = 0, add = 1; i < digits; i++, add *= 10)
			num += DIGIT_SUPPLIER.getAsInt() * add;
		return num;
	}
	
	/**
	 * Returns {@code 0} if the given {@link List} is empty.
	 */
	public static int sum(List<Integer> terms) {
		int sum = 0;
		for(Integer i : terms)
			sum += i.intValue();
		return sum;
	}
	
	/**
	 * Returns {@code 0} if the given {@link IntList} is empty.
	 */
	public static long sum(IntList terms) {
		int sum = 0;
		for(int i = 0; i < terms.size(); i++)
			sum += terms.get(i);
		return sum;
	}
	
	/**
	 * Returns {@code 1} if the given {@link List} is empty.
	 */
	public static long product(List<Integer> terms) {
		int product = 1;
		for(Integer i : terms)
			product *= i.intValue();
		return product;
	}
	
	/**
	 * Returns {@code 1} if the given {@link IntList} is empty.
	 */
	public static long product(IntList terms) {
		int product = 1;
		for(int i = 0; i < terms.size(); i++)
			product *= terms.get(i);
		return product;
	}
	
	public static String makeExpr(final int terms, final NamedSetting<IntRange> digitRange, final List<String> operators) {
		return makeExpr(terms, digitRange.ref(), operators);
	}
	
	public static String makeExpr(final int terms, final IntRange digitRange, final List<String> operators) {
		return makeExpr(terms, digitRange.getLow(), digitRange.getHigh(), operators);
	}
	
	public static String makeExpr(final int terms, final int minDigits, final int maxDigits, final List<String> operators) {
		if(terms <= 1)
			throw new IllegalArgumentException("Invalid number of terms: " + terms);
		StringBuilder result = new StringBuilder();
		for(int i = 0; i < terms - 1; i++) {
			result.append(Problem.intWithDigits(Problem.intInclusive(minDigits, maxDigits)));
			result.append(' ').append(operators.get((int) (Math.random() * operators.size()))).append(' ');
		}
		result.append(Problem.intWithDigits(Problem.intInclusive(minDigits, maxDigits)));
		return result.toString();
	}
	
	Pattern WHITESPACE = Pattern.compile("\\s+");
	
	public static String prettyExpression(final String expression) {
		return Prettifier.pretty(expression);
	}
	
	public static String prettyComplex(final Complex c) {
		StringBuilder sb = new StringBuilder();
		sb.append(prettyBigDecimal(c.realPart()));
		if(c.imaginaryPart().compareTo(BigDecimal.ZERO) == 0)
			return sb.toString();
		sb.append(" + ").append(prettyBigDecimal(c.imaginaryPart()));
		return sb.toString();
	}
	
	public static String prettyBigDecimal(final BigDecimal decimal) {
		return decimal.toPlainString();
	}
	
	/**
	 * Returns a random permutation of the given two {@code int}s.
	 */
	public static int[] shuffled(final int a, final int b) {
		return Math.random() < 0.5 ? new int[] {a, b} : new int[] {b, a};
	}
	/**
	 * Returns a {@link String} containg the text that represents this problem. This text will be displayed to the user.
	 * The text may use HTML tags to format its text. The display string should not be wrapped in an {@code html}, {@code body},
	 * or {@code p} tag.
	 */
	String displayString();
	
	/**
	 * Returns {@code true} if the input, given in the form of a {@link String}, is correct. There may be more than one correct
	 * answer to this {@link Problem}. This method is guaranteed to return true for at least one {@link String}.
	 */
	boolean isCorrect(String input);
	
	/**
	 * Returns the answer to this {@link Problem} as a {@link String}. If there are multiple correct answers to this {@link Problem},
	 * this method only returns one of them. This method returns the same {@link String} every time it is invoked.
	 */
	String answerAsString();
	
	/**
	 * Returns {@code true} if this problem only requires an approximate answer (within 5 percent of the actual answer, inclusive), {@code false}
	 * otherwise. The default implementation simply returns {@code false}.
	 */
	default boolean isApproximateResult() {
		return false;
	}
	
	/** Returns the percentage of the actual answer that a given answer must be in order to be counted correct. The default implementation returns {@code 0.05}.
	 * If this {@link Problem} is not {@link #isApproximateResult() an approximate result}, then the behavior of this method is undefined. Note that, for
	 * example, {@code 0.05} means within 5%, not within 0.05%.
	 * <p>The default implementation is equivalent to:
	 * <pre><code>return approximationPercentAsBigDecimal().doubleValue()</pre></code>
	 * </p>
	 * @return
	 */
	default double approximationPercent() {
		return approximationPercentAsBigDecimal().doubleValue();
	}
	
	/**
	 * Returns the {@link #approximationPercent()} as a {@code String}. This method should be called in preference to {@link #approximationPercent()}
	 * when possible because it may give a more precise value than can be stored in a {@code double}. The default implementation throws an
	 * {@link UnsupportedOperationException}.
	 */
	default BigDecimal approximationPercentAsBigDecimal() {
		throw new UnsupportedOperationException();
	}
	
	/** Returns an estimate of the number of lines of text that would be needed to display this {@link Problem}. The default implementation returns
	 * {@code 1.0}.*/
	default double estimatedDisplayLines() {
		return 1.0;
	}
	
}
