package suppliers.sets;

import static problems.Problem.*;
import static suppliers.NamedBooleanRef.*;
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
public class ProperAndImproperSubsetsSupplier extends SettingsProblemSupplier {
	
	private static final RangeStore SIZE = RangeStore.of(0, 26, 0, 8);
	
	private final NamedIntRange size = of(SIZE, "Size of set");
	
	public ProperAndImproperSubsetsSupplier() {
		settings(size);
	}

	@Override
	public Problem get() {
		List<Character> chars = SetSupUtils.letterList(intInclusive(size));
		final String displaySet = Prettifier.ensureMath(Prettifier.set(chars, Prettifier::variable));
		if(Math.random() <= 0.5)
			return MultiValued.of(String.format("How many improper subsets does the set %s have?", displaySet)).addResult(new Complex(Sets.numImproperSubsets(chars)));
		else
			return MultiValued.of(String.format("How many proper subsets does the set %s have?", displaySet)).addResult(new Complex(Sets.numProperSubsets(chars)));
			
	}

}
