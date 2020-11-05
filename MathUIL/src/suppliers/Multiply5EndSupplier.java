package suppliers;

import java.util.List;

import base.*;
import problems.*;
import utils.*;

/**
 * Produces {@link IntegerMultiplication} {@link Problem Problems} with two terms. One is any integer and the other is an integer ending in 5.
 * @author Sam Hooper
 *
 */
public class Multiply5EndSupplier extends SettingsProblemSupplier {
	
	private static final int MIN_DIGITS5 = 1, MAX_DIGITS5 = 3, MIN_DIGITS = 1, MAX_DIGITS = 3, LOW_DIGITS5 = 2, HIGH_DIGITS5 = 2, LOW_DIGITS = 1, HIGH_DIGITS = 3;
	private final NamedSetting<IntRange> digits5, digitsN;
	
	public Multiply5EndSupplier() {
		this(LOW_DIGITS5, HIGH_DIGITS5, LOW_DIGITS, HIGH_DIGITS);
	}
	
	public Multiply5EndSupplier(final int low5, int high5, int lowN, int highN) {
		digits5 = NamedSetting.of(new IntRange(MIN_DIGITS5, MAX_DIGITS5, low5, high5), "Digits in term ending in 5");
		digitsN = NamedSetting.of(new IntRange(MIN_DIGITS, MAX_DIGITS, lowN, highN), "Digits in term not ending in 5");
		settings = List.of(digits5, digitsN);
	}

	@Override
	public Problem get() {
		int term5 = 10 * Problem.intWithDigits(lowDigits5() - 1, highDigits5() - 1) + 5;
		int term = Problem.intWithDigits(lowDigitsN(), highDigitsN());
		return IntegerMultiplication.fromTerms(Problem.shuffled(term, term5));
	}
	
	public int lowDigits5() {
		return digits5.ref().getLow();
	}
	
	public int highDigits5() {
		return digits5.ref().getHigh();
	}
	
	public int lowDigitsN() {
		return digitsN.ref().getLow();
	}
	
	public int highDigitsN() {
		return digitsN.ref().getHigh();
	}
	
}
