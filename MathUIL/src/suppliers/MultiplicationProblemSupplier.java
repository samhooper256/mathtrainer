package suppliers;

import problems.MultiplicationProblem;

/**
 * @author Sam Hooper
 *
 */
public class MultiplicationProblemSupplier extends SingleOpSupplier {
	
	public static final int MIN_DIGITS = 1, MAX_DIGITS = 5, MIN_TERMS = 2, MAX_TERMS = 5;
	public static final int DEFAULT_MIN_TERMS = 2, DEFAULT_MAX_TERMS = 2, DEFAULT_MIN_DIGITS = 1, DEFAULT_MAX_DIGITS = 3;
	
	public MultiplicationProblemSupplier() {
		this(DEFAULT_MIN_TERMS, DEFAULT_MAX_TERMS, DEFAULT_MIN_DIGITS, DEFAULT_MAX_DIGITS);
	}
	
	public MultiplicationProblemSupplier(int lowTerms, int highTerms, int lowDigits, int highDigits) {
		super(MIN_TERMS, MAX_TERMS, lowTerms, highTerms, MIN_DIGITS, MAX_DIGITS, lowDigits, highDigits);
	}
	
	@Override
	public MultiplicationProblem get() {
		return new MultiplicationProblem((int) (Math.random() * (maxTerms() + 1 - minTerms()) + minTerms()), minDigits(), maxDigits());
	}
	
}
