package suppliers.fractions;

import static problems.Problem.*;
import static suppliers.NamedIntRange.*;

import java.util.List;

import math.BigFraction;
import problems.*;
import suppliers.*;

/**
 * @author Sam Hooper
 *
 */
public class CompareFractionsSupplier extends SettingsProblemSupplier {
	
	private static final RangeStore DENOM = RangeStore.of(2, 100, 2, 50);
	
	private final NamedIntRange denom = of(DENOM, "Denominators of fractions");
	
	public CompareFractionsSupplier() {
		settings(denom);
	}

	@Override
	public Problem get() {
		int d1 = intInclusive(denom), n1 = intInclusive(1, d1 - 1), d2 = intInclusive(denom), n2 = intInclusive(1, d2 - 1);
		List<BigFraction> shuf = Problem.shuffled(BigFraction.of(n1, d1), BigFraction.of(n2, d2));
		BigFraction f1 = shuf.get(0), f2 = shuf.get(1);
		if(Math.random() <= 0.5)
			return MultiValued.of(String.format("Which is larger, %s or %s?", Prettifier.ensureMath(Prettifier.frac(f1)),
					Prettifier.ensureMath(Prettifier.frac(f2)))).setLines(1.5).addResult(BigFraction.max(f1, f2));
		else
			return MultiValued.of(String.format("Which is smaller, %s or %s?", Prettifier.ensureMath(Prettifier.frac(f1)),
					Prettifier.ensureMath(Prettifier.frac(f2)))).setLines(1.5).addResult(BigFraction.min(f1, f2));
	}
	
}
