package math;

import java.math.*;
import java.util.*;

import utils.IntList;

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
	
	
	
	private static long goodMask = 0xC840C04048404040L;
//	{
//	    for (int i=0; i<64; ++i) goodMask |= Long.MIN_VALUE >>> (i*i);
//	}
	
	//Code and comments from: https://stackoverflow.com/a/18686659/11788023
	public static boolean isSquare(long x) {
	    // This tests if the 6 least significant bits are right.
	    // Moving the to be tested bit to the highest position saves us masking.
	    if (goodMask << x >= 0) return false;
	    final int numberOfTrailingZeros = Long.numberOfTrailingZeros(x);
	    // Each square ends with an even number of zeros.
	    if ((numberOfTrailingZeros & 1) != 0) return false;
	    x >>= numberOfTrailingZeros;
	    // Now x is either 0 or odd.
	    // In binary each odd square ends with 001.
	    // Postpone the sign test until now; handle zero in the branch.
	    if ((x&7) != 1 | x <= 0) return x == 0;
	    // Do it in the classical way.
	    // The correctness is not trivial as the conversion from long to double is lossy!
	    final long tst = (long) Math.sqrt(x);
	    return tst * tst == x;
	}

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
	
	public static boolean isInteger(BigDecimal bd) {
		 return bd.stripTrailingZeros().scale() <= 0;
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
	//	System.out.printf("entered isComplexInRectangularForm%n");
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
	
	public static IntList factorsUnsorted(final int n) {
		IntList facs = new IntList();
		final int sqrt = (int) Math.sqrt(n);
		for(int i = 1; i <= sqrt; i++) {
			if(n % i == 0) {
				facs.add(i);
				if(i != n / i) {
					facs.add(n / i);
				}
			}
		}
		return facs;
	}
	
}
