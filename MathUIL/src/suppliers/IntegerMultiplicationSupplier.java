package suppliers;

import problems.IntegerMultiplication;

/**
 * @author Sam Hooper
 *
 */
public class IntegerMultiplicationSupplier extends SingleOpSupplier {
	
	public static final int MIN_DIGITS = 1, MAX_DIGITS = 5, MIN_TERMS = 2, MAX_TERMS = 5;
	public static final int DEFAULT_MIN_TERMS = 2, DEFAULT_MAX_TERMS = 2, DEFAULT_MIN_DIGITS = 1, DEFAULT_MAX_DIGITS = 3;
	
	public IntegerMultiplicationSupplier() {
		this(DEFAULT_MIN_TERMS, DEFAULT_MAX_TERMS, DEFAULT_MIN_DIGITS, DEFAULT_MAX_DIGITS);
	}
	
	public IntegerMultiplicationSupplier(int lowTerms, int highTerms, int lowDigits, int highDigits) {
		super(MIN_TERMS, MAX_TERMS, lowTerms, highTerms, MIN_DIGITS, MAX_DIGITS, lowDigits, highDigits);
	}
	
	@Override
	public IntegerMultiplication get() {
		return new IntegerMultiplication((int) (Math.random() * (maxTerms() + 1 - minTerms()) + minTerms()), minDigits(), maxDigits());
	}
	
}
