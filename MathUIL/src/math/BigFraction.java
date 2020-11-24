package math;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

/**
 * @author Sam Hooper
 *
 */
public class BigFraction extends Number implements Comparable<BigFraction> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3738055726507507877L;
	
	/**
	 * A constant representing a fraction with value zero. It has a numerator of {@code 0}, a denominator of
	 * {@code 1}, and a positive sign.
	 */
	public static final BigFraction ZERO = new BigFraction(BigInteger.ZERO, BigInteger.ONE, 1);
	public static final BigFraction ONE = BigFraction.of(1, 1);
	public static final BigFraction NEGATIVE_ONE = BigFraction.of(1, -1);
	public static final BigFraction HALF = BigFraction.of(1, 2);
	
	public static BigInteger setSign(BigInteger i, int sign) {
		final BigInteger result;
		if(sign == 1 && BigNumbers.isNegative(i) || sign == -1 && BigNumbers.isPositive(i))
			result = i.negate();
		else
			result = i;
//		System.out.printf("setSign(%s, %d) => %s%n", i, sign, result);
		return result;
		
	}
	
	/**
	 * Returns the sum of {@code a} and {@code b}.
	 */
	public static final BigFraction sum(final BigFraction a, final BigFraction b) {
		if(BigNumbers.isZero(a.num)) return b;
		if(BigNumbers.isZero(b.num)) return a;
		final BigInteger denomGCD = BigNumbers.gcd(a.denom, b.denom); // nonnegative
		BigInteger denomLCM = a.denom.multiply(b.denom).divide(denomGCD); // nonnegative
		BigInteger numSum = setSign(a.num, a.sign).multiply(denomLCM.divide(a.denom)).add(setSign(b.num, b.sign).multiply(denomLCM.divide(b.denom))); // possibly negative
		return BigFraction.of(numSum, denomLCM);
	}

	public static final BigFraction multiply(final BigFraction a, final BigFraction b) {
		if(BigNumbers.isZero(a.num) || BigNumbers.isZero(b.num)) return ZERO;
		BigInteger resultNum = a.num.multiply(b.num), resultDenom = a.denom.multiply(b.denom);
		return BigFraction.of(resultNum, resultDenom, a.sign == b.sign ? 1 : -1);
	}

	public static final BigFraction subtract(final BigFraction a, final BigFraction b) {
		return sum(a, b.negate());
	}

	public static final BigFraction divide(final BigFraction a, final BigFraction b) {
		if(b == ZERO) divideByZero();
		return multiply(a, b.multiplicativeInverse());
	}

	public static int compare(final BigFraction a, final BigFraction b) {
		int result = setSign(a.num.multiply(b.denom), a.sign).compareTo(setSign(a.denom.multiply(b.num), b.sign));
//		System.out.printf("compare(%s, %s) == %d%n", a, b, result);
		return result;
	}

	/**
	 * Returns the smaller of the two given {@code BigFraction}s. If {@code compare(a, b)} returns {@code 0},
	 * {@code a} is returned.
	 * @param a the first {@code BigFraction}
	 * @param b the second {@code BigFraction}
	 * @return the smaller of {@code a} and {@code b}
	 * @see #max(BigFraction, BigFraction)
	 */
	public static BigFraction min(final BigFraction a, final BigFraction b) {
		return compare(a, b) <= 0 ? a : b;
	}

	/**
	 * Returns the greater of the two given {@code BigFraction}s. If {@code compare(a, b)} returns {@code 0},
	 * {@code a} is returned.
	 * @param a the first {@code BigFraction}
	 * @param b the second {@code BigFraction}
	 * @return the greater of {@code a} and {@code b}
	 * @see #min(BigFraction, BigFraction)
	 */
	public static BigFraction max(final BigFraction a, final BigFraction b) {
		return compare(a, b) >= 0 ? a : b;
	}
	
	public static final BigFraction of(final long numerator, final long denominator) {
		return of(BigInteger.valueOf(numerator), BigInteger.valueOf(denominator));
	}
	
	public static final BigFraction of(final BigInteger numerator, final BigInteger denominator) {
		if(BigNumbers.isZero(numerator)) return ZERO;
		if(BigNumbers.isZero(denominator)) zeroDenominator();
		return new BigFraction(numerator, denominator);
	}

	/**
	 * This method returns {@link #ZERO} if {@code numerator} is {@code 0}.
	 * @param numerator a <b>nonnegative</b> value representing the numerator of this fraction
	 * @param denominator a <b>nonnegative</b> value representing the denominator of this fraction
	 * @param signum the sign of this {@code BigFraction}, either {@code 1} or {@code -1}.
	 * @return a fraction with the numerator of {@code numerator}, the denominator of {@code denominator} and a sign
	 * of positive if {@code signum} is {@code 1} or negative is {@code signum} is {@code 1}.
	 */
	private static final BigFraction of(final BigInteger numerator, final BigInteger denominator, final int signum) {
		if(BigNumbers.isZero(numerator)) return ZERO;
		if(BigNumbers.isZero(denominator)) zeroDenominator();
		return new BigFraction(numerator, denominator, signum);
	}
	
	/**
	 * Returns {@code true} if {@code vulgarFraction} is a valid vulgar fraction in {@code String} form, {@code false} otherwise.
	 * If, for any {@code String} <i>s</i>, {@code isValidVulgar(s)} returns {@code true}, {@link #fromVulgar(String) fromVulgar}{@code (s)} will
	 * produce an accurate {@link BigFraction} without producing any exceptions.
	 * @param vulgarFraction
	 * @return
	 */
	public static boolean isValidVulgar(String vulgarFraction) {
		String[] split = vulgarFraction.split("/");
		return split.length == 1 && Utils.isInteger(vulgarFraction) || split.length == 2 && Utils.isInteger(split[0]) && Utils.isInteger(split[1]);
	}
	
	/** Returns a new {@link BigFraction} from the given vulgar fraction expressed as a {@code String}.
	 * Example {@code Strings} include "1/2" or "3/4".
	 * The input {@code String} must contain exactly one {@code /} with an integer on either side of it with extraneous whitespace or other characters. An
	 * exception is thrown if the given {@code String} is not {@link #isValidVulgar(String) valid}.
	 * */
	public static final BigFraction fromVulgar(final String vulgarBigFraction) {
		String[] split = vulgarBigFraction.split("/");
		return split.length == 1 ? BigFraction.of(new BigInteger(vulgarBigFraction), BigInteger.ONE) : BigFraction.of(new BigInteger(split[0]), new BigInteger(split[1]));
	}
	
	/**
	 * Converts a real number, given in string form in radix 10, to a {@code BigFraction}. {@code realNumberAsString}
	 * cannot have more than eight significant digits.
	 * @param realNumberAsString a real number given as a string. Some examples are "12.3", "-.7", ".0". "0.", or
	 *  "+182.49234"
	 * @return a {@code BigFraction} with the same value as {@code realNumberAsString}. The fraction will be fully reduced.
	 */
	public static final BigFraction from(final String realNumberAsString) {
		if(Objects.requireNonNull(realNumberAsString).isBlank()) {
			throw new IllegalArgumentException("Input String is empty or contains only whitespace.");
		}
		final char first = realNumberAsString.charAt(0);
		final int signum = first == '-' ? -1 : 1;
		
		String s = realNumberAsString.substring(first == '-' || first == '+' ? 1 : 0);
		if(s.indexOf('.') == -1) { //handles the no decimal case
			return BigFraction.of(new BigInteger(s), BigInteger.ONE, signum); //use BigFraction.of so that it will return a premade value
			//for zero.
		}
		//cut trailing zeros
		for(int i = s.length() - 1; i >= 0; i--) {
			if(s.charAt(i) != '0') {
				s = s.substring(0, i + 1);
				break;
			}
		}
		//cut leading zeros
		for(int i = 0; i < s.length(); i++) {
			if(s.charAt(i) != '0') {
					s = s.substring(i);
					break;
			}
		}
		if(s.equals(".")) return ZERO; //they passed ".0" or "0.0" or "0."
		int pointIndex = s.indexOf('.');
//		System.out.printf("after trimming: s=%s, first=%c, pointIndex=%d, signum=%d, "
//				+ "realNumberAsString=%s%n", s, first, pointIndex, signum, realNumberAsString);
		final String digitsOnly = s.substring(0, pointIndex) + s.substring(pointIndex + 1);
//		System.out.printf("digitsOnly=%s%n", digitsOnly);
		return BigFraction.of(new BigInteger(digitsOnly), new BigInteger("1" + "0".repeat(digitsOnly.length() - pointIndex)), signum);
		}

	/**
	 * A call to this method is equivalent to the call:
	 * <blockquote><pre>
	 * BigFraction.from(Double.toString(value));
	 * </pre></blockquote>
	 * @param value the {@code double} value to be converted to a fraction
	 * @return a {@code BigFraction} as determined by  {@link #from(String)}.
	 */
	public static final BigFraction from(final double value) {
		return BigFraction.from(Double.toString(value));
	}

	private static void divideByZero() {
		throw new ArithmeticException("A BigFraction cannot be divided by zero.");
	}

	private static void zeroDenominator() {
		throw new ArithmeticException("A BigFraction cannot have a denominator of zero.");
	}

	/**
	 * The numerator of this fraction; it will always be nonnegative
	 */
	private final BigInteger num;
	/**
	 * The numerator of this fraction; it will always be positive
	 */
	private final BigInteger denom;
	/**
	 * The sign of this fraction; it will always be either {@code -1} (negative) or {@code 1} (positive). A fraction
	 * with value zero will have a sign of {@code 1}.
	 */
	private final int sign;
	
	private BigFraction(final BigInteger numerator, final BigInteger denominator) {
		this(numerator, denominator, (BigNumbers.isNegative(numerator)) ^ (BigNumbers.isNegative(denominator)) ? -1 : 1);	
	}
	
	/**
	 * Precondition: {@code denominator is not zero}
	 * @param numerator
	 * @param denominator
	 * @param signum
	 */
	private BigFraction(final BigInteger numerator, final BigInteger denominator, final int signum) {
		final BigInteger na = numerator.abs(), da = denominator.abs();
		final BigInteger gcd = numerator == BigInteger.ZERO ? BigInteger.ONE : BigNumbers.gcd(na, da);
		this.sign = signum;
		num = na.divide(gcd);
		denom = da.divide(gcd);
	}
	public BigFraction add(final BigFraction o) {
		return sum(this, o);
	}
	
	public BigFraction multiply(final BigFraction o) {
		return multiply(this, o);
	}
	
	public BigFraction subtract(final BigFraction o) {
		return subtract(this, o);
	}
	
	public BigFraction divide(final BigFraction o) {
		return divide(this, o);
	}
	
	public BigFraction pow(final int power) {
		int uPow = Math.abs(power);
		BigInteger oNum = setSign(getNumerator(), sign).pow(uPow);
		BigInteger oDenom = getDenominator().pow(uPow);
		if(power < 0) {
			BigInteger temp = oNum;
			oNum = oDenom;
			oDenom = temp;
		}
		return BigFraction.of(oNum, oDenom);
	}
	public BigFraction negate() {
		return BigFraction.of(num, denom, -sign);
	}
	
	public BigFraction multiplicativeInverse() {
		return BigFraction.of(denom, num, sign);
	}
	public final BigDecimal toBigDecimal() {
		return new BigDecimal(num).divide(new BigDecimal(denom));
	}
	
	public boolean isNonnegative() {
		return sign == 1;
	}
	
	public boolean isNegative() {
		return sign == -1;
	}
	
	public BigInteger getNumerator() {
		return num;
	}
	
	public BigInteger getDenominator() {
		return denom;
	}
	
	public boolean isZero() {
		return this == ZERO;
	}
	
	public boolean isInteger() {
		return getDenominator().compareTo(BigInteger.ONE) == 0;
	}
	@Override
	public int hashCode() {
		return Objects.hash(denom, num, sign);
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj) {
			return true;
		}
		if(obj == null) {
			return false;
		}
		if(getClass() != obj.getClass()) {
			return false;
		}
		BigFraction other = (BigFraction) obj;
		return denom.equals(other.denom) && num.equals(other.num) && sign == other.sign;
	}

	@Override
	public String toString() {
		if(denom.compareTo(BigInteger.ONE) == 0)
			return String.format("%s%d", sign == -1 ? "-" : "", num);
		return String.format("%s%d/%d", sign == -1 ? "-" : "", num, denom);
	}

	@Override
	public int compareTo(BigFraction o) {
		return compare(this, o);
	}
	
	@Override
	public int intValue() {
		return toBigDecimal().intValue();
	}

	@Override
	public long longValue() {
		return toBigDecimal().longValue();
	}

	@Override
	public float floatValue() {
		return toBigDecimal().floatValue();
	}

	@Override
	public double doubleValue() {
		return toBigDecimal().doubleValue();
	}
	
}
