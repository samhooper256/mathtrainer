package suppliers.sets;

import static problems.Problem.*;
import static suppliers.NamedIntRange.*;

import java.util.List;

import math.Complex;
import problems.*;
import suppliers.*;
import utils.Sets;

/**
 * @author Sam Hooper
 *
 */
public class SubsetsSupplier extends SettingsProblemSupplier {
	
	private static final RangeStore SIZE = RangeStore.of(0, 26, 0, 8);
	
	private final NamedIntRange size = of(SIZE, "Size of set");
	
	
	public SubsetsSupplier() {
		addAllSettings(size);
	}

	@Override
	public Problem get() {
		List<Character> chars = SetSupUtils.letterList(intInclusive(size));
		long sub = Sets.numSubsets(chars);
		return Builder.of(String.format("How many subsets does the set %s have?%n", Prettifier.ensureMath(Prettifier.set(chars, Prettifier::variable))))
				.addResult(new Complex(sub)).build();
	}

}
