package suppliers.fractions;

import static problems.Problem.*;
import static suppliers.NamedIntRange.*;

import math.BigFraction;
import problems.*;
import suppliers.*;

/**
 * Fraction multiplication of the form a*(a+n)/(a+2n)
 * @author Sam Hooper
 *
 */
public class MultiplyingAAndNSupplier extends SettingsProblemSupplier {
	private static final RangeStore A_VALUE = RangeStore.of(1, 30), N_VALUE = RangeStore.of(-10, 10, -5, 5);
	private final NamedIntRange aValue = of(A_VALUE, "Value of a"), nValue = of(N_VALUE, "Value of n");
	
	public MultiplyingAAndNSupplier() {
		settings(aValue, nValue);
	}

	@Override
	public Problem get() {
		int a = intInclusive(aValue), n = intInclusive(nValue);
		System.out.printf("a=%d, n=%d%n", a, n);
		return FracSupUtils.multiplyScrambled(BigFraction.of(a, 1), BigFraction.of(a + n, a + 2 * n));
	}
	
	@Override
	public String getName() {
		return "Multiplying fractions of the form a*(a+n)/(a+2n)";
	}
	
}
