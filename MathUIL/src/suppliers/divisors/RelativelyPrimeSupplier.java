package suppliers.divisors;

import static problems.Problem.*;
import static suppliers.NamedBooleanRef.*;
import static suppliers.NamedIntRange.*;

import java.math.*;
import java.util.SortedMap;

import math.*;
import problems.*;
import suppliers.*;

/**
 * @author Sam Hooper
 *
 */
public class RelativelyPrimeSupplier extends SettingsProblemSupplier {
	private static final RangeStore VALUE = RangeStore.of(1, 100, 2, 80);
	private final NamedIntRange value = of(VALUE, "Value of number");
	
	public RelativelyPrimeSupplier() {
		addAllSettings(value);
	}

	@Override
	public Problem get() {
		int n = intInclusive(value);
		SortedMap<Integer, Integer> pf = Utils.primeFactorization(n);
		BigDecimal count = BigDecimal.valueOf(n);
		for(int i : pf.keySet())
			count = count.multiply(BigDecimal.valueOf(i - 1).divide(BigDecimal.valueOf(i), Utils.INTERMEDIATE_CONTEXT), Utils.INTERMEDIATE_CONTEXT);
		return ComplexValued.of(String.format("How many positive integers less than or equal to %d are relatively prime to %1$d?", n), new Complex(count.setScale(0, RoundingMode.HALF_UP)));
	}
	
}
