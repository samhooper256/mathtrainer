package suppliers.factorials;

import static problems.Problem.*;
import static suppliers.NamedIntRange.*;

import math.*;
import problems.*;
import suppliers.*;

/**
 * @author Sam Hooper
 *
 */
public class PermutationsSupplier extends SettingsProblemSupplier {
	private static final RangeStore N = RangeStore.of(1, 12, 1, 10), R = RangeStore.of(1, 12, 1, 10);
	private final NamedIntRange n = of(N, "Value of n in P(n, r)"), r = of(R, "Value of r in P(n, r)");
	
	public PermutationsSupplier() {
		settings(n, r);
	}

	@Override
	public Problem get() {
		final int nVal = intInclusive(n);
		final int rVal = intInclusive(Math.min(r.low(), nVal), Math.min(nVal, r.high()));
		return ComplexValued.of(String.format("P(%d, %d)", nVal, rVal), new Complex(Utils.nPr(nVal, rVal)));
	}
	
}
