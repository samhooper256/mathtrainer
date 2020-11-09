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
public class GCDSupplier extends SettingsProblemSupplier {
	private static final RangeStore DIGITS = RangeStore.of(1, 3, 1, 2);
	private final NamedIntRange digits;
	
	public GCDSupplier() {
		settings = List.of(digits = of(DIGITS, "Digits"));
	}

	@Override
	public Problem get() {
//		System.out.printf("enter GCDSupplier's get()%n");
		int a = 0, b = 0;
		while(a == 0)
			a = Problem.intWithDigits(digits);
		while(b == 0)
			b = Problem.intWithDigits(digits);
		return ComplexValued.of(String.format("The GCD of %d and %d is", a, b), new Complex(Utils.gcd(a, b)));
	}
	
}
