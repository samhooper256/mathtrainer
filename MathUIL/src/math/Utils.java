package math;

import java.math.*;

/**
 * @author Sam Hooper
 *
 */
public class Utils {
	
	private Utils() {}
	
	public static final String PI_STRING = "3.141592653589793238462643383279502884197169399375105820974944592307816406286208998628034825342117067982148086513282306647093844609550582231725359408128481117450284102701938521105559644622";
	/** <i>pi</i> rounded to 10 digits after the decimal.*/
	public static final BigDecimal PI = new BigDecimal("3.1415926536");
	/** Euler's number <i>e</i> rounded to 10 digits after the decimal.*/
	public static final BigDecimal E = new BigDecimal("2.7182818285");
	/** The golden ratio <i>phi</i> rounded to 10 digits after the decimal. */
	public static final BigDecimal PHI = new BigDecimal("1.6180339887");
	/**<i>pi</i> raised to the power of Euler's number <i>e</i>, rounded to 10 digits after the decimal. */
	public static final BigDecimal PI_TO_E = new BigDecimal("22.4591577184");
	/**Euler's number <i>e</i> raised to <i>pi</i>, rounded to 10 digits after the decimal. */
	public static final BigDecimal E_TO_PI = new BigDecimal("23.1406926328");
	
	private static final BigDecimal PI_INTERMEDIATE = new BigDecimal("3.14159265358979");
	private static final BigDecimal E_INTERMEDIATE = new BigDecimal("2.71828182845905");
	private static final int MAX_INTEGER_POWER_AS_INT = 999999999;
	private static final BigDecimal MAX_INTEGER_POWER = new BigDecimal("999999999");
	private static final MathContext INTERMEDIATE_CONTEXT = new MathContext(14);
	private static final MathContext RESULT_CONTEXT = new MathContext(10);
	
	/** Returns {@code base} raised to the power of {@code exponent}. Returns {@link BigDecimal#ONE} if {@code (exponent.compareTo(BigDecimal.ZERO) == 0)}
	 * regardless of what {@code base} is. The result is rounded to ten decimal places.*/
	public static BigDecimal pow(final BigDecimal base, final BigDecimal exponent) {
		if(exponent.compareTo(BigDecimal.ZERO) == 0)
			return BigDecimal.ONE;
		BigDecimal positiveExponent = exponent.abs(INTERMEDIATE_CONTEXT);
		BigDecimal baseToPositiveExponent;
		if(positiveExponent.compareTo(MAX_INTEGER_POWER) <= 0) {
			BigDecimal[] divMod = positiveExponent.divideAndRemainder(BigDecimal.ONE);
			int integralPartAsInt = divMod[0].intValueExact();
			BigDecimal remainder = divMod[1];
			baseToPositiveExponent = base.pow(integralPartAsInt).multiply(BigDecimal.valueOf(Math.pow(base.doubleValue(), remainder.doubleValue())));
		}
		else {
			return new BigDecimal(Math.pow(base.doubleValue(), exponent.doubleValue()), RESULT_CONTEXT);
		}
		if(isNegative(exponent))
			return BigDecimal.ONE.divide(baseToPositiveExponent);
		return baseToPositiveExponent;
	}
	
	public static BigDecimal piTo(final int nonNegativeExponent) {
		if(nonNegativeExponent > MAX_INTEGER_POWER_AS_INT)
			throw new IllegalArgumentException("exponent is too large");
		return PI_INTERMEDIATE.pow(nonNegativeExponent, RESULT_CONTEXT);
	}
	
	public static BigDecimal eTo(final int nonNegativeExponent) {
		if(nonNegativeExponent > MAX_INTEGER_POWER_AS_INT)
			throw new IllegalArgumentException("exponent is too large");
		return E_INTERMEDIATE.pow(nonNegativeExponent, RESULT_CONTEXT);
	}
	
	public static boolean isPositive(final BigDecimal n) {
		return n.compareTo(BigDecimal.ZERO) > 0;
	}
	
	public static boolean isNonPositive(final BigDecimal n) {
		return !isPositive(n);
	}
	
	public static boolean isNegative(final BigDecimal n) {
		return n.compareTo(BigDecimal.ZERO) < 0;
	}
	
	public static boolean isNonNegative(final BigDecimal n) {
		return !isNegative(n);
	}
	
	public static int gcd(int a, int b) {
		if(a == 0 || b == 0)
			throw new IllegalArgumentException("numbers cannot be zero");
		if(b > a) {
			int temp = a;
			a = b;
			b = temp;
		}
		while(a != b) {
			if(a > b)
				a = a - b;
			else
				b = b - a;
		}
		return a;
	}
	
	public static int lcm(int a, int b) {
		return a * b / gcd(a, b);
	}
	
	
}
