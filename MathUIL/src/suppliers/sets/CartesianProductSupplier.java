package suppliers.sets;

import static problems.Prettifier.*;
import static problems.Problem.*;
import static suppliers.NamedIntRange.*;

import java.util.List;

import math.Complex;
import problems.*;
import suppliers.*;

/**
 * @author Sam Hooper
 *
 */
public class CartesianProductSupplier extends SettingsProblemSupplier {
	
	private static final RangeStore SIZE = RangeStore.of(0, 26, 0, 8);
	
	private final NamedIntRange size = of(SIZE, "Size of sets");
	
	public CartesianProductSupplier() {
		addAllSettings(size);
	}

	@Override
	public Problem get() {
		List<Character> set1 = SetSupUtils.letterList(intInclusive(size)), set2 = SetSupUtils.letterList(intInclusive(size));
		return Builder.of(String.format("The Cartesian product of the sets %s and %s contains how many ordered paris:",
				ensureMath(set(set1, Prettifier::variable)), ensureMath(set(set2, Prettifier::variable))))
				.addResult(new Complex(set1.size() * set2.size())).build();
	}
	
}
