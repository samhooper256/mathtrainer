package math;

import java.math.*;
import java.util.*;
import java.util.stream.IntStream;

import utils.*;

/**
 * @author Sam Hooper
 *
 */
public class Utils {
	/*
	public static void main(String[] args) {
//		System.out.println(primeFactorization(12));
//		System.out.println(convertBase("1000", 10, 8));
		System.out.printf("%s%n", toBase10Fraction(".163", 7));
		System.out.printf("%s%n", log(BigInteger.valueOf(3), BigInteger.valueOf(1)));
		System.out.printf("%s%n", log(BigInteger.valueOf(3), BigInteger.valueOf(3)));
		System.out.printf("%s%n", log(BigInteger.valueOf(3), BigInteger.valueOf(9)));
		System.out.printf("%s%n", log(BigInteger.valueOf(3), BigInteger.valueOf(27)));
		System.out.printf("%n");
		System.out.printf("%s%n", toDecimal(BigFraction.fromVulgar("4/3"), 3));
		System.out.printf("%s%n", toDecimal(BigFraction.fromVulgar("1/169"), 13));
		System.out.printf("%s%n", toDecimal(BigFraction.fromVulgar("-1/169"), 13));
		System.out.printf("%s%n", toDecimal(BigFraction.fromVulgar("0"), 16));
	}
	*/
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
	
	public static final int MIN_RADIX = 2, MAX_RADIX = 16;
	public static final List<Character> RADIX_CHARS = List.of('0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F');
	
	private static final BigDecimal PI_INTERMEDIATE = new BigDecimal("3.14159265358979");
	private static final BigDecimal E_INTERMEDIATE = new BigDecimal("2.71828182845905");
	private static final int MAX_INTEGER_POWER_AS_INT = 999999999;
	private static final BigDecimal MAX_INTEGER_POWER = new BigDecimal("999999999");
	
	public static final MathContext INTERMEDIATE_CONTEXT = new MathContext(14);
	public static final MathContext RESULT_CONTEXT = new MathContext(10);
	
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
	
	/** {@code exponent} must be >= 0. Returns 1 for 0^0*/
	public static long pow(final long base, final long exponent) {
		if(exponent < 0)
			throw new IllegalArgumentException("exponent must be greater than or equal to 0");
		long result = 1;
		for(int i = 0; i < exponent; i++)
			result = result * base;
		return result;
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
	
	/**
	 * <p>Returns the number of digits in {@code n}.</p>
	 * <p>{@code magnitude(n)} is equivalent to {@code magnitude(Math.abs(n))} (in other words, the sign of the number does not affect
	 * the result).</p>
	 */
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

	public static boolean isint(final String s) {
		return isInteger(s) && s.length() <= 10 || (s.startsWith("-") && s.length() <= 11); //TODO
	}
	
	public static boolean isInteger(final String s) {
		return isInteger(s, 10);
	}
	
	/**
	 * {@code radix} must be between {@link #MIN_RADIX} and {@link #MAX_RADIX} (inclusive).
	 * @param s
	 * @param radix
	 * @return
	 */
	public static boolean isInteger(String s, final int radix) {
		if(radix < MIN_RADIX || radix > MAX_RADIX)
			throw new IllegalArgumentException("Radix " + radix + " is unsupported");
		if(s.isEmpty())
			return false;
		int i = s.charAt(0) == '-' ? 1 : 0;
		if(i == s.length())
			return false;
		s = s.toUpperCase();
		List<Character> subList = RADIX_CHARS.subList(0, radix);
		for(int j = i; j < s.length(); j++)
			if(!subList.contains(s.charAt(j)))
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
	
	/**
	 * Returns an {@link IntList} containing the factors of {@code n}. The elements in the returned list are unique but are in no particular order.
	 * @throws IllegalArgumentException if {@code n <= 0}.
	 */
	public static IntList factorsUnsorted(final int n) {
		if(n <= 0)
			throw new IllegalArgumentException("n must be greater than 0");
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
	
	/**
	 * Returns a {@link SortedMap} where the keys are the prime factors of {@code n} and the values are the powers of those factors.
	 * For example, {@code primeFactorization(12)} would produce the map:
	 * <pre><code>{2=2, 3=1}</code></pre>
	 * to represent 2^2 * 3^1.  
	 * @param n
	 * @return
	 */
	public static SortedMap<Integer, Integer> primeFactorization(int n) {
		SortedMap<Integer, Integer> map = new TreeMap<>();
        while (n % 2 == 0) {
        	map.put(2, map.getOrDefault(2, 0) + 1);
            n /= 2; 
        }
  
		for (int i = 3; i <= (int) Math.sqrt(n); i += 2)  { 
            while (n % i == 0) { 
                map.put(i, map.getOrDefault(i, 0) + 1);
                n /= i; 
            } 
        }
        if(n > 2)
        	map.put(n, 1);
        return map;
    }
	
	public static IntStream digits(int n) {
		int[] arr = new int[magnitude(n)];
		for(int i = arr.length - 1; i >= 0; i--) {
			arr[i] = n % 10;
			n /= 10;
		}
		return Arrays.stream(arr);
	}
	
	private static final ArrayList<BigInteger> MEMOIZED_FACTORIALS = new ArrayList<>(16);
	
	static {
		MEMOIZED_FACTORIALS.add(BigInteger.ONE);
		MEMOIZED_FACTORIALS.add(BigInteger.ONE);
		MEMOIZED_FACTORIALS.add(BigInteger.TWO);
	}
	/**
	 * Returns n factorial, or <i>n!</i>
	 */
	public static BigInteger factorial(int n) {
		while(MEMOIZED_FACTORIALS.size() <= n)
			MEMOIZED_FACTORIALS.add(BigInteger.valueOf(MEMOIZED_FACTORIALS.size()).multiply(MEMOIZED_FACTORIALS.get(MEMOIZED_FACTORIALS.size() - 1)));
		return MEMOIZED_FACTORIALS.get(n);
	}
	
	public static BigInteger nPr(final int n, final int r) {
		if(n < 0 || r < 0 || r > n)
			throw new IllegalArgumentException(String.format("Invalid arguments: n=%d, r=%d", n, r));
		return factorial(n).divide(factorial(n - r));
	}
	
	public static BigInteger nCr(final int n, final int r) {
		return nPr(n, r).divide(factorial(r));
	}
	
	/**
	 * {@code number} is assumed to be {@link #isInteger(String, int) an integer}. Any letters in the returned {@code String} will
	 * be capitalized.
	 */
	public static String convertBase(String number, int startRadix, int endRadix) {
		return new BigInteger(number, startRadix).toString(endRadix).toUpperCase();
	}
	
	/**
	 * {@code number} may have a radix point (that is, it need not be an integer). The radix point is assumed to be the period
	 * character ('.').
	 * @param number
	 * @param radix
	 * @return
	 */
	public static BigFraction toBase10Fraction(String number, int radix) {
		int dotIndex = number.indexOf('.');
		if(dotIndex < 0)
			return BigFraction.of(new BigInteger(number, radix), BigInteger.ONE);
		number = Strings.stripTrailing(number, '0'); //we know it has a decimal point, so this is okay.
		BigFraction integral = dotIndex == 0 ? BigFraction.ZERO : BigFraction.of(new BigInteger(number.substring(0, dotIndex), radix), BigInteger.ONE);
		if(dotIndex == number.length() - 1)
			return integral;
		String afterDot = number.substring(dotIndex + 1);
		BigFraction frac = BigFraction.of(new BigInteger(afterDot, radix), BigInteger.valueOf(radix).pow(afterDot.length()));
		return integral.add(frac);
	}
	
	/**
	 * Converts the given {@link BigFraction} to a {@code String} in the given radix. {@code endRadix} must be between {@link #MIN_RADIX} and
	 * {@link #MAX_RADIX} (inclusive). {@code fraction} must be equivalent to a fraction whose denominator is an integer power of {@code endRadix},
	 * or an {@link IllegalArgumentException} is thrown.
	 * @return
	 */
	public static String toDecimal(final BigFraction fraction, final int endRadix) {
		if(endRadix < MIN_RADIX || endRadix > MAX_RADIX)
			throw new IllegalArgumentException("Unsupported radix: " + endRadix);
		if(fraction.isZero())
			return "0";
		BigInteger oldNum = fraction.getNumerator();
		BigInteger oldDenom = fraction.getDenominator();
		BigInteger endRadixAsBigInteger = BigInteger.valueOf(endRadix);
		BigInteger newDenom = oldDenom;
		int multiple = 1;
		while(!isIntegerPower(newDenom, endRadixAsBigInteger) && multiple < endRadix) {
			newDenom = newDenom.add(oldDenom);
			multiple++;
		}
		if(!isIntegerPower(newDenom, endRadixAsBigInteger))
			throw new UnsupportedOperationException("Only fractions with a denominator that is an integer power of the radix are supported");
		BigInteger newNum = oldNum.multiply(BigInteger.valueOf(multiple));
		String numToRadix = newNum.toString(endRadix);
		int log = log(BigInteger.valueOf(endRadix), newDenom);
		if(numToRadix.length() - log < 0)
			numToRadix = "0".repeat(log - numToRadix.length()) + numToRadix;
		return (fraction.isNegative() ? "-" : "") + new StringBuilder(numToRadix).insert(numToRadix.length() - log, '.').toString();
	}
	
	/**
	 * Returns log<sub>{@code b}</sub>{@code (a)} if it is an {@code int}, throws an {@link ArithmeticException} otherwise. {@code a} must be positive
	 * and {@code b} must be greater than one, or an {@link ArithmeticException} is thrown.
	 */
	public static int log(BigInteger b, BigInteger a) {
		if(!BigNumbers.isPositive(a) || !BigNumbers.isPositive(b))
			throw new ArithmeticException();
		if(b.compareTo(BigInteger.ONE) == 0)
			throw new ArithmeticException();
		if(a.compareTo(BigInteger.ONE) == 0)
			return 0;
		
		BigInteger pow = b;
		int multiple = 1;
		while(true) {
			int comp = pow.compareTo(a);
			if(comp > 0)
				throw new ArithmeticException();
			if(comp == 0)
				return multiple;
			else {
				pow = pow.multiply(b);
				multiple++;
				if(multiple < 0) //it overflowed
					throw new ArithmeticException();
			}
		}
	}
	
	/**
	 * Returns {@code true} if {@code x} is an integer power of {@code y}, {@code false} otherwise.
	 * @param x
	 * @param y
	 * @return
	 */
	public static boolean isIntegerPower(BigInteger x, BigInteger y) {
		if(BigNumbers.isNegative(x) || BigNumbers.isNegative(y))
			throw new IllegalArgumentException("BigIntegers must not be negative");
		if(BigNumbers.isZero(y))
			return BigNumbers.isZero(x) || x.compareTo(BigInteger.ONE) == 0;
		else if(BigNumbers.isZero(x))
			return false;
		while(true) {
			BigInteger[] divMod = x.divideAndRemainder(y);
			if(BigNumbers.isZero(divMod[1]))
				x = divMod[0];
			else
				break;
		}
		return x.compareTo(BigInteger.ONE) == 0;
	}
}
