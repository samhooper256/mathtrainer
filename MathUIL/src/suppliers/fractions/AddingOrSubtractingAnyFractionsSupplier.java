package suppliers.fractions;

import static problems.Problem.*;
import static suppliers.NamedBooleanRef.*;
import static suppliers.NamedIntRange.*;

import java.util.List;

import math.BigFraction;
import problems.*;
import suppliers.*;

/**
 * @author Sam Hooper
 *
 */
public class AddingOrSubtractingAnyFractionsSupplier extends SettingsProblemSupplier {
	
	private static final RangeStore NUM1 = RangeStore.of(1, 50, 1, 20), NUM2 = RangeStore.of(1, 50, 1, 20),
			DENOM1 = RangeStore.of(1, 50, 1, 20), DENOM2 = RangeStore.of(1, 50, 1, 20);
	
	private final NamedIntRange num1 = of(NUM1, "First numerator"), num2 = of(NUM2, "Second numerator"),
			denom1 = of(DENOM1, "First denominator"), denom2 = of(DENOM2, "First denominator");
	
	public AddingOrSubtractingAnyFractionsSupplier() {
		addAllSettings(num1, denom1, num2, denom2);
	}

	@Override
	public Problem get() {
		int a = intInclusive(num1), b = intInclusive(num2), c = intInclusive(denom1), d = intInclusive(denom2);
		BigFraction f1 = BigFraction.of(a, b), f2 = BigFraction.of(c, d);
		if(Math.random() <= 0.5)
			return FracSupUtils.subtractReformed(f1, f2);
		else
			return FracSupUtils.addScrambled(f1, f2);
	}
	
	
	
}
