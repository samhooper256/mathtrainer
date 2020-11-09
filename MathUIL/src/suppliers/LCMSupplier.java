package suppliers;

import static problems.Problem.*;
import static suppliers.NamedIntRange.*;

import java.util.List;

import math.*;
import problems.*;

/**
 * @author Sam Hooper
 *
 */
public class LCMSupplier extends SettingsProblemSupplier {
	private static final RangeStore DIGITS = RangeStore.of(1, 3, 1, 2);
	private final NamedIntRange digits;
	
	public LCMSupplier() {
		settings = List.of(digits = of(DIGITS, "Digits"));
	}

	@Override
	public Problem get() {
		int a = 0, b = 0;
		while(a == 0)
			a = Problem.intWithDigits(digits);
		while(b == 0)
			b = Problem.intWithDigits(digits);
		return ComplexValued.of(String.format("The LCM of %d and %d is", a, b), new Complex(Utils.lcm(a, b)));
	}
	
}
