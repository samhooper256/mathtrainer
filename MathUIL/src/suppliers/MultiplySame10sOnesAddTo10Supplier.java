package suppliers;

import java.util.List;

import problems.*;
import utils.*;

/**
 * @author Sam Hooper
 *
 */
public class MultiplySame10sOnesAddTo10Supplier extends SettingsProblemSupplier {
	private static final int MIN_TENS = 1, MAX_TENS = 25, LOW_TENS = 1, HIGH_TENS = 20;
	
	private final NamedSetting<IntRange> tens;
	
	public MultiplySame10sOnesAddTo10Supplier() {
		this(LOW_TENS, HIGH_TENS);
	}
	
	public MultiplySame10sOnesAddTo10Supplier(int lowTens, int highTens) {
		tens = NamedSetting.of(new IntRange(MIN_TENS, MAX_TENS, lowTens, highTens), "\"Tens\" Digit");
		settings = List.of(tens);
	}

	@Override
	public Problem get() {
		int tens = 10 * Problem.intInclusive(lowTens(), highTens());
		int diff = Problem.intInclusive(0, 10);
		return SimpleExpression.multiplyTerms(Problem.shuffled(tens + diff, tens + (10 - diff)));
	}
	
	public int lowTens() {
		return tens.ref().getLow();
	}
	
	public int highTens() {
		return tens.ref().getHigh();
	}
	
}
