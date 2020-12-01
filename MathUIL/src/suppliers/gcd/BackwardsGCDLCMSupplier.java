package suppliers.gcd;

import static suppliers.NamedIntRange.*;

import java.util.List;

import math.*;
import problems.*;
import suppliers.*;

/**
 * @author Sam Hooper
 *
 */
@Named("Find term given GCD and LCM")
public class BackwardsGCDLCMSupplier extends SettingsProblemSupplier {
	private static final RangeStore DIGITS = RangeStore.of(1, 3, 1, 2);
	private final NamedIntRange digits;
	
	public BackwardsGCDLCMSupplier() {
		settings = List.of(digits = of(DIGITS, "Digits in terms"));
	}

	@Override
	public Problem get() {
		int a = 0, b = 0;
		while(a == 0)
			a = Problem.intWithDigits(digits);
		while(b == 0)
			b = Problem.intWithDigits(digits);
		final int gcd = Utils.gcd(a, b), lcm = a * b / gcd;
		return ComplexValued.of(String.format("Given GCD(%d,x)=%d and LCM(%1$d,x)=%d, find x", a, gcd, lcm), new Complex(b));
	}
	
}
