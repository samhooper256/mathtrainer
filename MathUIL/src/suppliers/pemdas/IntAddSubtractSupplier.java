package suppliers.pemdas;

import static suppliers.NamedIntRange.of;

import problems.*;
import suppliers.*;

/**
 * @author Sam Hooper
 *
 */
public class IntAddSubtractSupplier extends SettingsProblemSupplier {
	
	private static final RangeStore TERMS = RangeStore.of(2, 5, 2, 2), DIGITS = RangeStore.of(2, 5, 2, 2);
	
	private final NamedIntRange termRange, digitRange;
	
	public IntAddSubtractSupplier() {
		addAllSettings(termRange = of(TERMS, "Terms"), digitRange = of(DIGITS, "Digits"));
	}
	
	@Override
	public SimpleExpression get() {
		return SimpleExpression.of(termRange, lowDigits(), highDigits(), "+", "-");
	}
	
	public int lowDigits() {
		return digitRange.low();
	}
	
	public int highDigits() {
		return digitRange.high();
	}

	@Override
	public String getName() {
		return "Integer Addition & Subtraction";
	}
	
}

