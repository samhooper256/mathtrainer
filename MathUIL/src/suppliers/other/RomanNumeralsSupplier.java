package suppliers.other;

import static suppliers.NamedIntRange.*;

import math.*;
import problems.*;
import suppliers.*;

/**
 * @author Sam Hooper
 *
 */
public class RomanNumeralsSupplier extends SettingsProblemSupplier {
	private static final RangeStore VALUE = RangeStore.of(RomanNumerals.MIN_VALUE, RomanNumerals.MAX_VALUE);
	private final NamedIntRange value = of(VALUE, "Value");
	
	public RomanNumeralsSupplier() {
		addAllSettings(value);
	}

	@Override
	public Problem get() {
		int intValue = Problem.intInclusive(value);
		if(Math.random() <= 0.5)
			return ComplexValued.of(String.format("%s as an Arabic number is:", RomanNumerals.toRomanNumerals(intValue)), new Complex(intValue));
		return Builder.ofString(String.format("%d as a Roman Numeral is:", intValue), RomanNumerals.toRomanNumerals(intValue));
		
	}
	
}
