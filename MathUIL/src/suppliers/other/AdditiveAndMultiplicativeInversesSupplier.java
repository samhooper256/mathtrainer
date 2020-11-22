package suppliers.other;

import static problems.Problem.*;
import static suppliers.NamedBooleanRef.*;
import static suppliers.NamedIntRange.*;

import math.Fraction;
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
		Fraction frac = Fraction.of(intInclusive(values), intInclusive(values));
		if(Math.random() < 0.5)
			return FractionValued.of(String.format("What is the additive inverse of %s:", Prettifier.ensureMath(Prettifier.frac(frac))), frac.negate());
		return FractionValued.of(String.format("What is the multiplicative inverse of %s:", Prettifier.ensureMath(Prettifier.frac(frac))), frac.multiplicativeInverse());
	}
	
	
}
