package suppliers.other;

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
public class PolygonalNumbersSupplier extends SettingsProblemSupplier {
	private static final RangeStore N = RangeStore.of(1, 25, 1, 10),
			S = RangeStore.of(NumberAdjectives.MIN_POLYGON_SIDES, NumberAdjectives.MAX_POLYGON_SIDES, NumberAdjectives.MIN_POLYGON_SIDES, Math.min(10, NumberAdjectives.MAX_POLYGON_SIDES));
	private final NamedIntRange n = of(N, "Polygonal number index"), s = of(S, "Sides of polygon");
	
	public PolygonalNumbersSupplier() {
		addAllSettings(s, n);
	}

	@Override
	public Problem get() {
		int sides = intInclusive(n), index = intInclusive(n), num = NumberAdjectives.sGonalNumber(sides, index);
		return ComplexValued.of(String.format("The %d%s %s number is:", index, Prettifier.ordinalSuffix(index), NumberAdjectives.polygonalAdjective(sides)), new Complex(num));
	}
	
}
