package math;

import java.math.*;
import java.util.Objects;

import problems.Prettifier;

/**
 * <p>A complex number, represented in the rectangular form <i>a+bi</i>, where <i>a</i> is the real part and <i>bi</i> is the imaginary part. {@code Complex}
 * objects are immutable.</p>
 * @author Sam Hooper
 *
 */
public class Complex {
	
	public static final Complex ZERO = new Complex(0);
	public static final Complex ONE = new Complex(1);
	
	private static final MathContext POWER_CONTEXT = new MathContext(32);
	
	/**
	 * The "a" in "a + bi"
	 */
	private final BigDecimal a;
	/**
	 * The "b" in "a + bi"
	 */
	private final BigDecimal b;
	
	/**
	 * The "a" and "b" parameters are those in the rectangular form "a + bi"
	 */
	public Complex(final BigDecimal a, final BigDecimal b) {
		this.a = Objects.requireNonNull(a);
		this.b = Objects.requireNonNull(b);
	}
	
	/**
	 * A {@link Complex} with only a real part.
	 */
	public Complex(final BigDecimal a) {
		this(a, BigDecimal.ZERO);
	}
	
	/**
	 * The "a" and "b" parameters are those in the rectangular form "a + bi"
	 */
	public Complex(final BigInteger a, final BigInteger b) {
		this.a = Objects.requireNonNull(new BigDecimal(a));
		this.b = Objects.requireNonNull(new BigDecimal(b));
	}
	
	/**
	 * A {@link Complex} with only a real part.
	 */
	public Complex(final BigInteger a) {
		this(new BigDecimal(a), BigDecimal.ZERO);
	}
	
	/**
	 * The "a" and "b" parameters are those in the rectangular form "a + bi"
	 */
	public Complex(final double a, final double b) {
		this(BigDecimal.valueOf(a), BigDecimal.valueOf(b));
	}
	
	/**
	 * The "a" parameter is the one in the rectangular form "a + bi". The imaginary part is {@code 0}.
	 */
	public Complex(final double a) {
		this(a, 0);
	}
	
	/**
	 * The "a" and "b" parameters are those in the rectangular form "a + bi"
	 */
	public Complex(final long a, final long b) {
		this(BigDecimal.valueOf(a), BigDecimal.valueOf(b));
	}
	
	/**
	 * The "a" parameter is the one in the rectangular form "a + bi". The imaginary part is {@code 0}.
	 */
	public Complex(final long a) {
		this(a, 0);
	}
	
	/**
	 * Takes a {@link String} of the form "a+bi" (where a and b are valid {@link BigDecimal BigDecimals}), or of the form "a" where
	 * a is a valid {@code BigDecimal}, or of the form "bi" where "b" is a valid {@code BigDecimal}. The given {@code String}
	 * must not have any whitespace or other extraneous characters.
	 * @param abi
	 */
	public Complex(final String abi) {
//		System.out.printf("entered Complex(abi=%s)%n", abi);
		int p = abi.indexOf('+');
		if(p >= 0) {
			a = new BigDecimal(abi.substring(0, p));
			b = new BigDecimal(abi.substring(p + 1, abi.length() - 1));
		}
		else {
			if(abi.endsWith("i")) {
				if(abi.length() == 1) {
					a = BigDecimal.ZERO;
					b = BigDecimal.ONE;
				}
				else {
					a = BigDecimal.ZERO;
					b = new BigDecimal(abi.substring(0, abi.length() - 1));
				}
			}
			else {
				a = new BigDecimal(abi);
				b = BigDecimal.ZERO;
			}
		}
	}

	@Override
	public String toString() {
		if(!hasImaginaryPart())
			return String.format("%s", Prettifier.stripTrailingZeros(realPart()));
		else if(!hasRealPart())
			return String.format("%si", Prettifier.stripTrailingZeros(imaginaryPart()));
		return String.format("%s+%si", Prettifier.stripTrailingZeros(realPart()), imaginaryPart().compareTo(BigDecimal.ONE) == 0 ? "" : Prettifier.stripTrailingZeros(imaginaryPart()));
	}
	
	
	@Override
	public int hashCode() {
		return Objects.hash(a, b);
	}
	
	/**
	 * Returns {@code true} if the two {@link Complex} objects are equal in value, {@code false} otherwise.
	 */
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
		Complex other = (Complex) obj;
		return a.compareTo(other.a) == 0 && b.compareTo(other.b) == 0;
	}
	
	public Complex add(Complex augend) {
		return new Complex(realPart().add(augend.realPart()), imaginaryPart().add(augend.imaginaryPart()));
	}
	
	public Complex add(Complex augend, MathContext mc) {
		return new Complex(realPart().add(augend.realPart(), mc), imaginaryPart().add(augend.imaginaryPart(), mc));
	}
	
	public Complex subtract(final int subtrahend) {
		if(subtrahend == 0)
			return this;
		return new Complex(realPart().subtract(BigDecimal.valueOf(subtrahend)), imaginaryPart());
	}
	
	public Complex subtract(Complex subtrahend) {
		return new Complex(realPart().subtract(subtrahend.realPart()), imaginaryPart().subtract(subtrahend.imaginaryPart()));
	}
	
	public Complex subtract(Complex subtrahend, MathContext mc) {
		return new Complex(realPart().subtract(subtrahend.realPart(), mc), imaginaryPart().subtract(subtrahend.imaginaryPart(), mc));
	}
	
	
	/**
	 * Returns the quotient of {@code this} and the given complex number.
	 */
	public Complex divide(Complex divisor, MathContext mc) {
		Complex dCon = divisor.conjugate(mc);
		BigDecimal div = divisor.abs2AsBigDecimal(mc);
		return multiply(dCon, mc).divide(div, mc);
	}
	
	/**
	 * Returns the quotient of {@code this} and the given real number.
	 */
	public Complex divide(BigDecimal divisor, MathContext mc) {
		return new Complex(realPart().divide(divisor, mc), imaginaryPart().divide(divisor, mc));
	}
	
	public Complex multiply(int multiplicand) {
		final BigDecimal multiplicandAsBigDecimal = BigDecimal.valueOf(multiplicand);
		return new Complex(realPart().multiply(multiplicandAsBigDecimal), imaginaryPart().multiply(multiplicandAsBigDecimal));
	}
	
	public Complex multiply(int multiplicand, MathContext mc) {
		final BigDecimal multiplicandAsBigDecimal = BigDecimal.valueOf(multiplicand).round(mc);
		return new Complex(realPart().multiply(multiplicandAsBigDecimal, mc), imaginaryPart().multiply(multiplicandAsBigDecimal, mc));
	}
	
	public Complex multiply(Complex multiplicand) {
		BigDecimal real = realPart().multiply(multiplicand.realPart()).subtract(imaginaryPart().multiply(multiplicand.imaginaryPart()));
		BigDecimal im = realPart().multiply(multiplicand.imaginaryPart()).add(imaginaryPart().multiply(multiplicand.realPart()));
		return new Complex(real, im);
	}
	
	public Complex multiply(Complex multiplicand, MathContext mc) {
		BigDecimal real = realPart().multiply(multiplicand.realPart(), mc).subtract(imaginaryPart().multiply(multiplicand.imaginaryPart(), mc), mc);
		BigDecimal im = realPart().multiply(multiplicand.imaginaryPart(), mc).add(imaginaryPart().multiply(multiplicand.realPart(), mc), mc);
		return new Complex(real, im);
	}
	
	public Complex negate() {
		return new Complex(realPart().negate(), imaginaryPart().negate());
	}
	
	public Complex negate(MathContext mc) {
		return new Complex(realPart().negate(mc), imaginaryPart().negate(mc));
	}
	
	/**
	 * Returns the complex conjugate of {@code this}.
	 */
	public Complex conjugate() {
		return new Complex(realPart(), imaginaryPart().negate());
	}
	
	/**
	 * Returns the complex conjugate of {@code this}.
	 */
	public Complex conjugate(MathContext mc) {
		return new Complex(realPart(), imaginaryPart().negate(mc));
	}
	
	/** Returns the absolute value of this complex number. */
	public Complex abs(MathContext mc) {
		return new Complex(absAsBigDecimal(mc));
	}
	
	/** Returns the absolute value of this complex number. */
	private BigDecimal absAsBigDecimal(MathContext mc) {
		return (realPart().pow(2, mc).add(imaginaryPart().pow(2, mc))).sqrt(mc);
	}
	
	/** Returns the square of the absolute value of this complex number. */
	public Complex abs2(MathContext mc) {
		return new Complex(abs2AsBigDecimal(mc));
	}
	
	/** Returns the square of the absolute value of this complex number. */
	public BigDecimal abs2AsBigDecimal(MathContext mc) {
		return realPart().multiply(realPart(), mc).add(imaginaryPart().multiply(imaginaryPart(), mc), mc);
	}
	
	/** Throws an exception if the exponentiation cannot be done. */
	public Complex pow(Complex power, MathContext mc) {
		if(power.hasExactIntValue())
			return power(this, power.intValueExact(), mc);
		else if(!hasImaginaryPart() && !power.hasImaginaryPart())
			return new Complex(Utils.pow(realPart(), power.realPart()));
		throw new IllegalArgumentException("Cannot take power.");
	}
	
	/** Throws an exception if the exponentiation cannot be done. */
	public Complex pow(Complex power) {
		if(power.hasExactIntValue())
			return pow(power.intValueExact());
		else if(!hasImaginaryPart() && !power.hasImaginaryPart())
			return new Complex(Utils.pow(realPart(), power.realPart()));
		throw new IllegalArgumentException("Cannot take power.");
	}
	
	public Complex pow(int power) {
		return power(this, power);
	}
	
	private static Complex power(Complex c, int n, MathContext mc) {
		if(n < 0)
			return Complex.ONE.divide(power(c, -n, mc), mc);
	    if (n == 0)
	        return Complex.ONE;
	    if (n == 1) 
	        return c;
	    Complex sq = power(c, n / 2, mc); 
	    if (n % 2 == 0) 
	        return sq.multiply(sq, mc); 
	    return c.multiply(sq.multiply(sq, mc), mc); 
	}
	
	private static Complex power(Complex c, int n) {
		if(n < 0)
			return Complex.ONE.divide(power(c, -n), POWER_CONTEXT);
	    if (n == 0)
	        return Complex.ONE;
	    if (n == 1) 
	        return c;
	    Complex sq = power(c, n / 2); 
	    if (n % 2 == 0) 
	        return sq.multiply(sq); 
	    return c.multiply(sq.multiply(sq)); 
	} 
	
	/**
	 * Returns a {@link Complex} with the {@link #realPart()} and {@link #imaginaryPart()} rounded using the given {@link MathContext}
	 * via {@link BigDecimal#round(MathContext)}.
	 */
	public Complex round(MathContext resultContext) {
		return new Complex(realPart().round(resultContext), imaginaryPart().round(resultContext));
	}
	
	public BigDecimal realPart() {
		return a;
	}
	
	public boolean hasRealPart() {
		return realPart().compareTo(BigDecimal.ZERO) != 0;
	}
	
	public BigDecimal imaginaryPart() {
		return b;
	}
	
	public boolean hasImaginaryPart() {
		return imaginaryPart().compareTo(BigDecimal.ZERO) != 0;
	}
	
	public boolean isZero() {
		return realPart().compareTo(BigDecimal.ZERO) == 0 && imaginaryPart().compareTo(BigDecimal.ZERO) == 0;
	}
	
	/**
	 * Returns {@code (this % divisor)}. {@code this} and {@code divisor} must not {@link #hasImaginaryPart() have an imaginary part}. The
	 * returned {@link Complex} will not have an imaginary part.
	 * 
	 * The remainder is given as described in {@link BigDecimal#remainder(BigDecimal)}, which is <i>not</i> equivalent to the modulo operation.
	 */
	public Complex remainder(final Complex divisor) {
		if(divisor.hasImaginaryPart())
			throw new IllegalArgumentException("Cannot find the remainder when the divisor has an imaginary part");
		return remainder(divisor.realPart());
	}
	
	public Complex remainder(final BigDecimal divisor) {
		if(hasImaginaryPart())
			throw new IllegalArgumentException("Cannot find the remainder when the dividend has an imaginary part");
		return new Complex(realPart().remainder(divisor));
	}
	
	public Complex remainder(final long divisor) {
		return remainder(BigDecimal.valueOf(divisor));
	}
	
	public boolean hasExactIntValue() {
		return !hasImaginaryPart() && BigNumbers.isInteger(a);
	}
	
	public int intValueExact() {
		if(hasImaginaryPart())
			throw new ArithmeticException("This complex number has an imaginary part, so it does not have an exact int value.");
		return realPart().intValueExact();
	}
	
	public long longValueExact() {
		if(hasImaginaryPart())
			throw new ArithmeticException("This complex number has an imaginary part, so it does not have an exact long value.");
		return realPart().longValueExact();
	}
	
	public BigDecimal bigDecimalValueExact() {
		if(hasImaginaryPart())
			throw new ArithmeticException("This complex number has an imaginary part, so it does not have an exact BigDecimal value.");
		return realPart();
	}

	
	
}
