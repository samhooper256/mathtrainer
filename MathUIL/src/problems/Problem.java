package problems;

import java.math.*;
import java.util.*;
import java.util.function.*;
import java.util.regex.Pattern;

import math.Complex;
import suppliers.*;
import utils.*;

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
	
	public static int magnitude(final int n) {
		int abs = Math.abs(n);
		if(abs >= 1_000_000_000) return 10;
		if(abs >= 100_000_000) return 9;
		if(abs >= 10_000_000) return 8;
		if(abs >= 1_000_000) return 7;
		if(abs >= 100_000) return 6;
		if(abs >= 10_000) return 5;
		if(abs >= 1_000) return 4;
		if(abs >= 100) return 3;
		if(abs >= 10) return 2;
		return 1;
	}
	public static boolean isInteger(final String s) {
		if(s.length() == 0)
			return false;
		int i = s.charAt(0) == '-' ? 1 : 0;
		if(s.length() - i == 0)
			return false;
		for(int j = i; j < s.length(); j++)
			if(s.charAt(j) < '0' || s.charAt(j) > '9')
				return false;
		return true;
	}
	
	/**
	 * Returns {@code true} if the given {@link String} represents a valid real number in decimal form, {@code false} otherwise.
	 * returns {@code false} for {@link String Strings} that end in a decimal point.
	 * @param s
	 * @return
	 */
	public static boolean isBigDecimal(final String s) {
		if(s.isBlank())
			return false;
		final int start = s.charAt(0) == '-' ?  1 : 0;
		if(start == s.length())
			return false;
		int point = -1;
		if(s.charAt(start) == '.') {
			if(s.length() == start + 1)
				return false;
			point = start;
		}
		else {
			point = s.indexOf('.', 1);
		}
		
		if(point == s.length() - 1)
			return false;
		if(point == -1)
			point = s.length();
		for(int i = start; i < point; i++)
			if(s.charAt(i) < '0' || s.charAt(i) > '9')
				return false;
		for(int i = point + 1; i < s.length(); i++)
			if(s.charAt(i) < '0' || s.charAt(i) > '9')
				return false;
		
		return true;
	}
	
	public static boolean isComplexInRectangularForm(final String s) {
//		System.out.printf("entered isComplexInRectangularForm%n");
		if(s.length() == 0)
			return false;
		int pIndex = s.indexOf('+');
		if(pIndex < 0)
			return isBigDecimal(s);
		if(pIndex == 0)
			return false;
		if(!s.endsWith("i"))
			return false;
		return isBigDecimal(s.substring(0, pIndex)) && isBigDecimal(s.substring(pIndex + 1, s.length() - 1));
	}
	
	public static boolean within5(final BigDecimal target, final BigDecimal guess) {
		final BigDecimal fivePercent = target.multiply(new BigDecimal("0.05")).abs();
		final BigDecimal diff = target.subtract(guess).abs();
		return diff.compareTo(fivePercent) <= 0;
	}
	
	public static int intInclusive(int min, int max) {
		if(min > max)
			throw new IllegalArgumentException(String.format("min > max (%d < %d)", min, max));
		return (int) (Math.random() * (max - min + 1)  + min);
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
		return ExpressionPrettifier.pretty(expression);
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
	
	/** Returns an estimate of the number of lines of text that would be needed to display this {@link Problem}. The default implementation returns
	 * {@code 1.0}.*/
	default double estimatedDisplayLines() {
		return 1.0;
	}
	
}
