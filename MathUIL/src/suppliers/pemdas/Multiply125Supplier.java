package suppliers.pemdas;

import java.util.List;

import problems.*;
import suppliers.*;
import utils.*;
import utils.refs.*;

/**
 * @author Sam Hooper
 *
 */
public class Multiply125Supplier extends SettingsProblemSupplier {
	
	private static final int MIN_DIGITS = 1, MAX_DIGITS = 4, LOW_DIGITS = 1, HIGH_DIGITS = 3;
	private static final double CHANCE_375 = 0.30;
	private static final boolean DEFAULT_INCLUDE375 = true;
	private final NamedSetting<IntRange> digits;
	private final NamedSetting<BooleanRef> include375;
	
	public Multiply125Supplier() {
		this(LOW_DIGITS, HIGH_DIGITS);
	}
	
	public Multiply125Supplier(final int lowDigits, final int highDigits) {
		digits = NamedSetting.of(new IntRange(MIN_DIGITS, MAX_DIGITS, lowDigits, highDigits), "Digits in non-125 term");
		include375 = NamedSetting.of(new BooleanRef(DEFAULT_INCLUDE375), "Include 375");
		settings = List.of(include375, digits);
	}

	@Override
	public Problem get() {
		int term = Problem.intWithDigits(lowDigits(), highDigits());
		int x = include375() && Math.random() < CHANCE_375 ? 375 : 125;
		return SimpleExpression.multiplyTerms(Problem.shuffled(x, term));
	}
	
	public int lowDigits() {
		return digits.ref().getLow();
	}
	
	public int highDigits() {
		return digits.ref().getHigh();
	}
	
	public boolean include375() {
		return include375.ref().get();
	}
	
	@Override
	public String getName() {
		return "Multiply by 125 and like";
	}
	
}

