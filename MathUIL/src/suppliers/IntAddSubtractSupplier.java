package suppliers;

import java.util.*;

import problems.*;
import utils.*;

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
//		System.out.printf("(enter) IntAddSubtractSupplier constructor with args=(lowTerms=%d, highTerms=%d, lowDigits=%d, highDigits=%d)%n", lowTerms, highTerms, lowDigits, highDigits);
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

