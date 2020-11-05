package suppliers;

import java.util.*;

import base.NamedSetting;
import problems.*;
import utils.*;

/**
 * @author Sam Hooper
 *
 */
public class IntAddSubtractSupplier extends SettingsProblemSupplier {
	
	public static final int MIN_DIGITS = 1, MAX_DIGITS = 5, MIN_TERMS = 2, MAX_TERMS = 5;
	public static final int DEFAULT_MIN_TERMS = 2, DEFAULT_MAX_TERMS = 4, DEFAULT_MIN_DIGITS = 2, DEFAULT_MAX_DIGITS = 4;
	
	private final NamedSetting<IntRange> termRange, digitRange;
	
	public IntAddSubtractSupplier() {
		this(DEFAULT_MIN_TERMS, DEFAULT_MAX_TERMS, DEFAULT_MIN_DIGITS, DEFAULT_MAX_DIGITS);
	}
	
	public IntAddSubtractSupplier(int lowTerms, int highTerms, int lowDigits, int highDigits) {
		this.termRange = NamedSetting.of(new IntRange(MIN_TERMS, MAX_TERMS, lowTerms, highTerms), "Terms");
		this.digitRange = NamedSetting.of(new IntRange(MIN_DIGITS, MAX_DIGITS, lowDigits, highDigits), "Digits");
		settings = List.of(termRange, digitRange);
	}
	
	@Override
	public IntAddSubtract get() {
		return new IntAddSubtract((int) (Math.random() * (highTerms() + 1 - lowTerms()) + lowTerms()), lowDigits(), highDigits());
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

