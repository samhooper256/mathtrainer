package suppliers.pemdas;

import java.util.*;

import problems.*;
import suppliers.*;
import utils.*;
import utils.refs.IntRange;

/**
 * @author Sam Hooper
 *
 */
public class IntAddSubtractSupplier extends SettingsProblemSupplier {
	
	public static final int MIN_DIGITS = 1, MAX_DIGITS = 5, MIN_TERMS = 2, MAX_TERMS = 5;
	public static final int LOW_TERMS = 2, HIGH_TERMS = 4, LOW_DIGITS = 2, HIGH_DIGITS = 4;
	
	private final NamedSetting<IntRange> termRange, digitRange;
	
	public IntAddSubtractSupplier() {
		this(LOW_TERMS, HIGH_TERMS, LOW_DIGITS, HIGH_DIGITS);
	}
	
	public IntAddSubtractSupplier(int lowTerms, int highTerms, int lowDigits, int highDigits) {
		this.termRange = NamedSetting.of(new IntRange(MIN_TERMS, MAX_TERMS, lowTerms, highTerms), "Terms");
		this.digitRange = NamedSetting.of(new IntRange(MIN_DIGITS, MAX_DIGITS, lowDigits, highDigits), "Digits");
		settings = List.of(termRange, digitRange);
	}
	
	@Override
	public SimpleExpression get() {
		return SimpleExpression.of(termRange, lowDigits(), highDigits(), "+", "-");
	}

	public int lowTerms() {
		return termRange.ref().getLow();
	}
	
	public int highTerms() {
		return termRange.ref().getHigh();
	}
	
	public int lowDigits() {
		return digitRange.ref().getLow();
	}
	
	public int highDigits() {
		return digitRange.ref().getHigh();
	}
}

