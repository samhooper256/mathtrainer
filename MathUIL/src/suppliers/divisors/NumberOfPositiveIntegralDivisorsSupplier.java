package suppliers.divisors;

import static problems.Problem.*;
import static suppliers.NamedBooleanRef.*;
import static suppliers.NamedIntRange.*;

import math.*;
import problems.*;
import suppliers.*;
import utils.IntList;

/**
 * @author Sam Hooper
 *
 */
public class NumberOfPositiveIntegralDivisorsSupplier extends SettingsProblemSupplier {
	private static final RangeStore VALUE = RangeStore.of(1, 500, 1, 200);
	private final NamedIntRange value = of(VALUE, "Value of number");
	
	public NumberOfPositiveIntegralDivisorsSupplier() {
		settings(value);
	}

	@Override
	public Problem get() {
		int val = intInclusive(value);
		IntList facs = Utils.factorsUnsorted(val);
		return ComplexValued.of(String.format("%d has how many positive integral divisors?", val), new Complex(facs.size()));
		
	}
	
}
