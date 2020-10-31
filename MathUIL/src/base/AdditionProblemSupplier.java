package base;

import java.util.List;

import utils.*;

/**
 * @author Sam Hooper
 *
 */
public class AdditionProblemSupplier implements NamedProblemSupplier {
	
	public static final int MIN_DIGITS = 1, MAX_DIGITS = 5, MIN_TERMS = 2, MAX_TERMS = 5;
	public static final int DEFAULT_MIN_TERMS = 2, DEFAULT_MAX_TERMS = 4, DEFAULT_MIN_DIGITS = 2, DEFAULT_MAX_DIGITS = 4;
	
	private final NamedSetting<IntRange> termRange, digitRange;
	private final List<Ref> settings;
	
	public AdditionProblemSupplier() {
		this(DEFAULT_MIN_TERMS, DEFAULT_MAX_TERMS, DEFAULT_MIN_DIGITS, DEFAULT_MAX_DIGITS);
	}
	
	public AdditionProblemSupplier(int minTerms, int maxTerms, int minDigits, int maxDigits) {
		this.termRange = NamedSetting.of(new IntRange(MIN_TERMS, MAX_TERMS, minTerms, maxTerms), "Terms");
		this.digitRange = NamedSetting.of(new IntRange(MIN_DIGITS, MAX_DIGITS, minDigits, maxDigits), "Digits");
		this.settings = List.of(termRange, digitRange);
	}
	
	@Override
	public AdditionProblem get() {
		return new AdditionProblem((int) (Math.random() * (maxTerms() + 1 - minTerms()) + minTerms()), minDigits(), maxDigits());
	}
	
	public int minTerms() {
		return termRange.ref().getLow();
	}
	
	public int maxTerms() {
		return termRange.ref().getHigh();
	}
	
	public int minDigits() {
		return digitRange.ref().getLow();
	}
	
	public int maxDigits() {
		return digitRange.ref().getHigh();
	}
	
	@Override
	public List<Ref> settings() {
		return settings;
	}

	@Override
	public String getName() {
		return "Addition Problems";
	}
}

