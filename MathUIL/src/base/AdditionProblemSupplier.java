package base;

import java.util.function.*;

/**
 * @author Sam Hooper
 *
 */
public class AdditionProblemSupplier implements Supplier<AdditionProblem> {
	
	public static final int DEFAULT_MIN_TERMS = 2, DEFAULT_MAX_TERMS = 4, DEFAULT_MIN_DIGITS = 2, DEFAULT_MAX_DIGITS = 4;
	
	private int minTerms, maxTerms, minDigits, maxDigits;
	public AdditionProblemSupplier() {
		this(DEFAULT_MIN_TERMS, DEFAULT_MAX_TERMS, DEFAULT_MIN_DIGITS, DEFAULT_MAX_DIGITS);
	}
	
	public AdditionProblemSupplier(int minTerms, int maxTerms, int minDigits, int maxDigits) {
		this.minTerms = minTerms;
		this.maxTerms = maxTerms;
		this.minDigits = minDigits;
		this.maxDigits = maxDigits;
	}
	@Override
	public AdditionProblem get() {
		return new AdditionProblem((int) (Math.random() * (maxTerms + 1 - minTerms) + minTerms), minDigits, maxDigits);
	}
}
