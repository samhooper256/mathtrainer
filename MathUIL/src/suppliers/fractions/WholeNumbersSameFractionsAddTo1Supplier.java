package suppliers.fractions;

import static problems.Problem.*;
import static suppliers.NamedIntRange.*;

import math.*;
import problems.*;
import suppliers.*;

/**
 * @author Sam Hooper
 *
 */
public class WholeNumbersSameFractionsAddTo1Supplier extends SettingsProblemSupplier {
	
	private static final RangeStore WHOLE = RangeStore.of(1, 25, 1, 20), DENOM = RangeStore.of(2, 20, 2, 10);
	private final NamedIntRange whole = of(WHOLE, "Value of whole number part"), denom = of(DENOM, "Denominator of fractional part");
	
	public WholeNumbersSameFractionsAddTo1Supplier() {
		addAllSettings(whole, denom);
	}

	@Override
	public Problem get() {
		int w = intInclusive(whole), den = intInclusive(denom.low(), Math.max(denom.high(), denom.low() + 1)), num1 = intInclusive(1, den - 1);
		return FracSupUtils.multiplyScrambled(MixedNumber.of(w, BigFraction.of(num1, den)), MixedNumber.of(w, BigFraction.of(den - num1, den)));
	}

	@Override
	public String getName() {
		return "Multiplying fractions with same whole part and fractional parts adding to 1";
	}
	
}
