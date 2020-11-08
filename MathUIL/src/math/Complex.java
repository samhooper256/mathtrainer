package math;

import java.math.*;
import java.util.Objects;

/**
 * <p>A complex number, represented in the rectangular form <i>a+bi</i>, where <i>a</i> is the real part and <i>bi</i> is the imaginary part.</p>
 * @author Sam Hooper
 *
 */
public class Complex {
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
	public Complex(final double a, final double b) {
		this(BigDecimal.valueOf(a), BigDecimal.valueOf(b));
	}
	
	/**
	 * Takes a {@link String} either of the form "a+bi" (where a and b are valid {@link BigDecimal BigDecimals}) or "a" where
	 * a is a valid {@link BigDecimal}.
	 * @param abi
	 */
	public Complex(final String abi) {
		int p = abi.indexOf('+');
		if(p >= 0) {
			a = new BigDecimal(abi.substring(0, p));
			b = new BigDecimal(abi.substring(p + 1, abi.length() - 1));
		}
		else {
			a = new BigDecimal(abi);
			b = BigDecimal.ZERO;
		}
	}

	@Override
	public String toString() {
		if(b.compareTo(BigDecimal.ZERO) == 0)
			return String.format("%f", a);
		return String.format("%f+%fi", a, b);
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

	public BigDecimal realPart() {
		return a;
	}
	
	public BigDecimal imaginaryPart() {
		return b;
	}
	
	
}
