package suppliers.divisors;

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
public class SumOfPositiveIntegralDivisorsSupplier extends SettingsProblemSupplier {
	private static final RangeStore VALUE = RangeStore.of(1, 200, 1, 150);
	private final NamedIntRange value = of(VALUE, "Value of number");
	
	public SumOfPositiveIntegralDivisorsSupplier() {
		addAllSettings(value);
	}

	@Override
	public Problem get() {
		int val = intInclusive(value);
		long sum = Utils.factorsUnsorted(val).sum();
		return ComplexValued.of(String.format("What is the sum of the positive integral divisors of %d:", val), new Complex(sum));
	}
	
}
