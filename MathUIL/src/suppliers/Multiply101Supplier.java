package suppliers;

import java.util.List;

import base.NamedSetting;
import problems.IntegerMultiplication;
import problems.Problem;
import utils.IntRange;

/**
 * @author Sam Hooper
 *
 */
public class Multiply101Supplier extends SettingsProblemSupplier {
	
	private static final int MIN_DIGITS101 = 3, MAX_DIGITS101 = 4, MIN_DIGITS = 1, MAX_DIGITS = 5, LOW_DIGITS101 = 3, HIGH_DIGITS101 = 4, LOW_DIGITS = 1, HIGH_DIGITS = 4;
	
	private final NamedSetting<IntRange> digits101, digitsN;
	
	public Multiply101Supplier() {
		this(LOW_DIGITS101, HIGH_DIGITS101, LOW_DIGITS, HIGH_DIGITS);
	}
	
	public Multiply101Supplier(int low101, int high101, int low, int high) {
		digits101 = NamedSetting.of(new IntRange(MIN_DIGITS101, MAX_DIGITS101, low101, high101), "Digits in 1...1 term");
		digitsN = NamedSetting.of(new IntRange(MIN_DIGITS, MAX_DIGITS, low, high), "Digits in other term");
		settings = List.of(digits101, digitsN);
	}
	
	@Override
	public Problem get() {
		int term101 = Integer.parseInt("1" + "0".repeat(Problem.intInclusive(lowDigits101() - 2, highDigits101() - 2)) + "1");
		int term = Problem.intWithDigits(lowDigitsN(), highDigitsN());
		return IntegerMultiplication.fromTerms(Problem.shuffled(term, term101));
	}

	public int lowDigits101() {
		return digits101.ref().getLow();
	}
	
	public int highDigits101() {
		return digits101.ref().getHigh();
	}
	
	public int lowDigitsN() {
		return digitsN.ref().getLow();
	}
	
	public int highDigitsN() {
		return digitsN.ref().getHigh();
	}
}
