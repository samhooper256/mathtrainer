package math;

import java.math.*;
import java.util.Objects;

/**
 * @author Sam Hooper
 *
 */
public class MixedNumber extends Number {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2635763404057187658L;
	
	private final BigFraction fraction;
	private final BigInteger integer;
	
	public static MixedNumber of(final BigInteger integer, final BigFraction fraction) {
		Objects.requireNonNull(integer);
		Objects.requireNonNull(fraction);
		if(fraction.isNegative())
			throw new IllegalArgumentException("The BigFraction of a MixedNumber must not be negative");
		if(!fraction.isZero() && fraction.getDenominator().compareTo(BigInteger.ONE) == 0)
			throw new IllegalArgumentException("Cannot create MixedNumber if the BigFraction's denominator is one");
		return new MixedNumber(integer, fraction);
	}
	
	public static MixedNumber of(final long integer, final BigFraction fraction) {
		return of(BigInteger.valueOf(integer), fraction);
	}

	public static MixedNumber of(final BigFraction fraction) {
		BigInteger[] divRem = fraction.getNumerator().divideAndRemainder(fraction.getDenominator());
		return of(divRem[0], BigFraction.of(divRem[1], fraction.getDenominator()));
	}
	
	public static MixedNumber of(final long integer) {
		return of(BigInteger.valueOf(integer), BigFraction.ZERO);
	}
	
	/**
	 * This is a "dumb" constructor. It just takes the values and assigns them to the instance variables. It doesn't check for {@code null}s or invalid
	 * values.
	 */
	private MixedNumber(final BigInteger integer, final BigFraction fraction) {
		this.integer = integer;
		this.fraction = fraction;
	}
	
	public MixedNumber add(final MixedNumber augend) {
		return MixedNumber.of(toImproperFraction().add(augend.toImproperFraction()));
	}
	
	public MixedNumber subtract(final MixedNumber subtrahend) {
		return MixedNumber.of(toImproperFraction().subtract(subtrahend.toImproperFraction()));
	}
	
	public MixedNumber multiply(final MixedNumber multiplicand) {
		return MixedNumber.of(toImproperFraction().multiply(multiplicand.toImproperFraction()));
	}
	
	public MixedNumber divide(final MixedNumber divisor) {
		return MixedNumber.of(toImproperFraction().divide(divisor.toImproperFraction()));
	}
	
	public BigFraction getFractionalPart() {
		return fraction;
	}
	
	public BigInteger getIntegralPart() {
		return integer;
	}
	
	public BigDecimal toBigDecimal() {
		return new BigDecimal(integer).add(fraction.toBigDecimal());
	}
	
	public BigFraction toImproperFraction() {
		return BigFraction.of(integer.multiply(fraction.getDenominator()).add(fraction.getNumerator()), fraction.getDenominator());
	}
	
	@Override
	public String toString() {
		return "MixedNumber[integer=" + integer + ", fraction=" + fraction + "]";
	}

	@Override
	public int intValue() {
		return (int) doubleValue();
	}

	@Override
	public long longValue() {
		return (long) doubleValue();
	}

	@Override
	public float floatValue() {
		return (float) doubleValue();
	}

	@Override
	public double doubleValue() {
		return integer.doubleValue() + fraction.doubleValue();
	}
	
}
