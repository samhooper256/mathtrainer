package suppliers;

import java.util.*;

import base.NamedSetting;
import problems.*;
import utils.*;

/**
 * @author Sam Hooper
 *
 */
public class AdditionProblemSupplier extends SingleOpSupplier {
	
	public static final int MIN_DIGITS = 1, MAX_DIGITS = 5, MIN_TERMS = 2, MAX_TERMS = 5;
	public static final int DEFAULT_MIN_TERMS = 2, DEFAULT_MAX_TERMS = 4, DEFAULT_MIN_DIGITS = 2, DEFAULT_MAX_DIGITS = 4;
	
	public AdditionProblemSupplier() {
		this(DEFAULT_MIN_TERMS, DEFAULT_MAX_TERMS, DEFAULT_MIN_DIGITS, DEFAULT_MAX_DIGITS);
	}
	
	public AdditionProblemSupplier(int lowTerms, int highTerms, int lowDigits, int highDigits) {
		super(MIN_TERMS, MAX_TERMS, lowTerms, highTerms, MIN_DIGITS, MAX_DIGITS, lowDigits, highDigits);
	}
	
	@Override
	public AdditionProblem get() {
		return new AdditionProblem((int) (Math.random() * (maxTerms() + 1 - minTerms()) + minTerms()), minDigits(), maxDigits());
	}
	
}

