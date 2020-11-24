package math;

import java.math.*;

/**
 * @author Sam Hooper
 *
 */
public class BigNumbers {
	
	private BigNumbers() {}
	
	public static boolean isInteger(BigDecimal bd) {
		 return bd.stripTrailingZeros().scale() <= 0;
	}
	
	public static boolean isNegative(BigInteger bi) {
		return bi.compareTo(BigInteger.ZERO) < 0;
	}
	
	public static boolean isPositive(BigInteger bi) {
		return bi.compareTo(BigInteger.ZERO) > 0;
	}
	
	public static boolean isZero(BigInteger bi) {
		return bi.compareTo(BigInteger.ZERO) == 0;
	}
	
	public static BigInteger gcd(BigInteger a, BigInteger b) {
		System.out.printf("enter BigNumbers::gcd(a=%s, b=%s)%n", a, b);
		if(isZero(a) || isZero(b))
			throw new IllegalArgumentException("numbers cannot be zero");
		if(isNegative(a) || isNegative(b))
			throw new IllegalArgumentException("cannot find gcd if numbers are negative");
		while(!isZero(b)) {
			BigInteger t = b;
			b = a.mod(b);
			a = t;
		}
		return a;
	}
}
