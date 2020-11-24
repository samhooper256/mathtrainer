package suppliers.other;

import static problems.Problem.*;
import static suppliers.NamedBooleanRef.*;
import static suppliers.NamedIntRange.*;

import math.*;
import problems.*;
import suppliers.*;

/**
 * @author Sam Hooper
 *
 */
public class AdditiveAndMultiplicativeInversesSupplier extends SettingsProblemSupplier {
	
	private static final RangeStore VALUES = RangeStore.of(1, 20, 1, 12);
	private final NamedIntRange values = of(VALUES, "Value(s) of number(s)");
	
	public AdditiveAndMultiplicativeInversesSupplier() {
		settings(values);
	}

	@Override
	public Problem get() {
		BigFraction frac = BigFraction.of(intInclusive(values), intInclusive(values));
		if(Math.random() < 0.5)
			return MultiValued.of(String.format("What is the additive inverse of %s:", Prettifier.ensureMath(Prettifier.frac(frac)))).addResult(frac.negate());
		else
			return MultiValued.of(String.format("What is the multiplicative inverse of %s:", Prettifier.ensureMath(Prettifier.frac(frac)))).addResult(frac.multiplicativeInverse());
	}
	
	
}
