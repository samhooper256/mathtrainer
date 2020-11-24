package suppliers.fractions;

import static problems.Problem.*;
import static suppliers.NamedIntRange.*;
import static suppliers.NamedIntRange.of;

import math.*;
import problems.*;
import suppliers.*;

/**
 * @author Sam Hooper
 *
 */
public class MultiplyingMixedNumbersWithSameFractionSupplier extends SettingsProblemSupplier {
	
	private static final RangeStore WHOLE = RangeStore.of(1, 50, 1, 25), DENOM = RangeStore.of(2, 20, 2, 10);
	
	private final NamedIntRange whole = of(WHOLE, "Values of whole numbers"), denom = of(DENOM, "Denominator of fractional part");
	
	public MultiplyingMixedNumbersWithSameFractionSupplier() {
		settings(whole, denom);
	}

	@Override
	public Problem get() {
		int w1 = intInclusive(whole), w2 = intInclusive(whole), den = intInclusive(denom.low(), Math.max(denom.high(), denom.low() + 1)), num = intInclusive(1, den - 1);
		return FracSupUtils.multiplyScrambled(MixedNumber.of(w1, BigFraction.of(num, den)), MixedNumber.of(w2, BigFraction.of(num, den)));
	}
	
	
}
