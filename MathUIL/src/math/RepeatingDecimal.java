package math;

import java.math.*;

/**
 * A class for representing repeating decimals. It has a {@link #getTerminatingPart() non-repeating (terminating) part} represented by
 * a {@link BigDecimal} and a {@link #getRepeatingPart() repeating part} represented by a {@link String} of digits. {@code RepeatingDecimals} are immutable.
 * @author Sam Hooper
 *
 */
public class RepeatingDecimal {
	/*
	public static void main(String[] args) {
		System.out.println(new RepeatingDecimal(BigDecimal.ZERO, "3").toBigFraction());
		System.out.println(new RepeatingDecimal(BigDecimal.ONE, "3").toBigFraction());
		System.out.println(new RepeatingDecimal("1.2", "3").toBigFraction());
		System.out.println(new RepeatingDecimal("333", "3").toBigFraction());
		System.out.println(new RepeatingDecimal(".333", "3").toBigFraction());
		System.out.println(new RepeatingDecimal("10", "4").toBigFraction());
	}
	*/
	
	private final BigDecimal terminatingPart;
	private final String repeatingPart;
	
	/**
	 * The {@code repeatingPart} must not be {@link String#isBlank() blank}.
	 */
	public RepeatingDecimal(final BigDecimal terminatingPart, final String repeatingPart) {
		if(repeatingPart.isBlank())
			throw new IllegalArgumentException("Repeating part must not be blank");
		this.terminatingPart = terminatingPart;
		this.repeatingPart = repeatingPart;
	}
	
	/**
	 * Equivalent to {@code new RepeatingDecimal(new BigDecimal(terminatingPart), repeatingPart)}.
	 */
	public RepeatingDecimal(final String terminatingPart, final String repeatingPart) {
		this(new BigDecimal(terminatingPart), repeatingPart);
	}
	
	public BigFraction toBigFraction() {
		final String plain = getTerminatingPart().toPlainString();
		int dotIndex = plain.indexOf('.');
		BigFraction terminating = BigFraction.from(plain);
		BigFraction repeating = BigFraction.of(new BigInteger(getRepeatingPart()),
				new BigInteger("9".repeat(getRepeatingPart().length()) + "0".repeat(dotIndex < 0 ? 0 : plain.length() - dotIndex - 1)));
		return terminating.add(repeating);
	}

	public BigDecimal getTerminatingPart() {
		return terminatingPart;
	}

	public String getRepeatingPart() {
		return repeatingPart;
	}
	
	@Override
	public String toString() {
		return toString(3);
	}
	
	/**
	 * Returns a {@code String} representation of this {@link RepeatingDecimal} consisting of the {@link #getTerminatingPart() terminating part}
	 * following by {@code repetitions} repetitions of the {@link #getRepeatingPart() repeating part}.
	 */
	public String toString(final int repetitions) {
		return getTerminatingPart() + getRepeatingPart().repeat(repetitions);
	}
	
}
