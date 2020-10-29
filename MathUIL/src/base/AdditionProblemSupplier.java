package base;

import java.util.List;

import utils.IntRange;

/**
 * @author Sam Hooper
 *
 */
public class AdditionProblemSupplier implements ProblemSupplier {
	
	public static final int MIN_DIGITS = 1, MAX_DIGITS = 5, MIN_TERMS = 2, MAX_TERMS = 5;
	public static final int DEFAULT_MIN_TERMS = 2, DEFAULT_MAX_TERMS = 4, DEFAULT_MIN_DIGITS = 2, DEFAULT_MAX_DIGITS = 4;
	
	private final IntRange termRange, digitRange;
	private final List<Object> settings;
	
	public AdditionProblemSupplier() {
		this(DEFAULT_MIN_TERMS, DEFAULT_MAX_TERMS, DEFAULT_MIN_DIGITS, DEFAULT_MAX_DIGITS);
	}
	
	public AdditionProblemSupplier(int minTerms, int maxTerms, int minDigits, int maxDigits) {
		this.termRange = new IntRange(MIN_TERMS, MAX_TERMS, minTerms, maxTerms);
		this.digitRange = new IntRange(MIN_DIGITS, MAX_DIGITS, minDigits, maxDigits);
		this.settings = List.of(termRange, digitRange);
	}
	@Override
	public AdditionProblem get() {
		return new AdditionProblem((int) (Math.random() * (maxTerms() + 1 - minTerms()) + minTerms()), minDigits(), maxDigits());
	}
	
	public int minTerms() {
		return termRange.getLow();
	}
	
	public int maxTerms() {
		return termRange.getHigh();
	}
	
	public int minDigits() {
		return digitRange.getLow();
	}
	
	public int maxDigits() {
		return digitRange.getHigh();
	}
	
	public List<Object> settings() {
		return settings;
	}
}

