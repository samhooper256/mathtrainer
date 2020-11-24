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
	
	public static boolean isZero(BigDecimal bi) {
		return bi.compareTo(BigDecimal.ZERO) == 0;
	}
	
	public static boolean isNegative(BigDecimal bd) {
		return bd.compareTo(BigDecimal.ZERO) < 0;
	}
	
	public static boolean isPositive(BigDecimal bd) {
		return bd.compareTo(BigDecimal.ZERO) > 0;
	}
	
	public static BigInteger gcd(BigInteger a, BigInteger b) {
		if(isZero(a) || isZero(b))
			throw new IllegalArgumentException("numbers cannot be zero");
		if(b.compareTo(a) > 0) {
			BigInteger temp = a;
			a = b;
			b = temp;
		}
		while(a.compareTo(b) != 0) {
			if(a.compareTo(b) > 0)
				a = a.subtract(b);
			else
				b = b.subtract(a);
		}
		return a;
	}
}
