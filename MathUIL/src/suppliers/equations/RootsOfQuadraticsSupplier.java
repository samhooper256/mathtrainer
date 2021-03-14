package suppliers.equations;

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
public class RootsOfQuadraticsSupplier extends SettingsProblemSupplier {
	
	private static final RangeStore MAGNITUDES = RangeStore.of(1, 20, 1, 10), DENOMS = RangeStore.of(2, 10, 2, 6);
	private final NamedIntRange magnitudes = of(MAGNITUDES, "Magnitudes of integer roots"), denoms = of(DENOMS, "Denominators of fractional roots");
	
	public RootsOfQuadraticsSupplier() {
		addAllSettings(magnitudes, denoms);
	}
	
	@Override
	public Problem get() {
		BigFraction a = makeRoot(), b = makeRoot();
		String quad = Prettifier.polynomialEqualsZero('x', Utils.intQuadraticFromRoots(a, b));
		if(Problem.random() <= 0.5) {
			return Builder.of(String.format("What is the larger root of %s:", Prettifier.ensureMath(quad)))
					.addResult(BigFraction.max(a, b)).build();
		}
		else {
			return Builder.of(String.format("What is the smaller root of %s:", Prettifier.ensureMath(quad)))
					.addResult(BigFraction.min(a, b)).build();
		}
	}



	private BigFraction makeRoot() {
		if(Problem.random() <= 0.5)
			return BigFraction.of((Problem.random() <= 0.5 ? -1 : 1) * intInclusive(magnitudes), 1);
		else {
			int denom = intInclusive(denoms);
			int num = intInclusive(1, denom - 1);
			return BigFraction.of(num, denom);
		}
	}

}
