package suppliers.fractions;

import static problems.Problem.*;
import static suppliers.NamedIntRange.*;
import static suppliers.NamedIntRange.of;

import math.BigFraction;
import problems.*;
import suppliers.*;

/**
 * @author Sam Hooper
 *
 */
@Named("Multiplying fractions of the form a*a/(a+b)")
public class ATimesAOverAPlusBSupplier extends SettingsProblemSupplier {
	
private static final RangeStore A_VALUE = RangeStore.of(1, 30), B_VALUE = RangeStore.of(-10, 10, -5, 5);
	
	private final NamedIntRange aValue = of(A_VALUE, "Value of a"), bValue = of(B_VALUE, "Value of b");
	
	public ATimesAOverAPlusBSupplier() {
		settings(aValue, bValue);
	}

	@Override
	public Problem get() {
		final int a = intInclusive(aValue), b = intInclusive(bValue);
		return FracSupUtils.multiplyScrambled(BigFraction.of(a, 1), BigFraction.of(a, a + b));
	}
	
	
}
