package suppliers.exponentiation;

import static suppliers.NamedIntRange.*;

import java.util.List;

import problems.*;
import suppliers.*;

/**
 * @author Sam Hooper
 *
 */
public class DifferenceOfCubes1ApartSupplier extends SettingsProblemSupplier {
		
	private static final RangeStore SMALLER_BASE = RangeStore.of(1, 40);
	private final NamedIntRange smallerBase = of(SMALLER_BASE, "Smaller Base Value");
	
	public DifferenceOfCubes1ApartSupplier() {
		settings = List.of(smallerBase);
	}

	@Override
	public Problem get() {
		int smallBase = Problem.intInclusive(smallerBase);
		int[] bases = Problem.shuffled(smallBase, smallBase + 1);
		return new SimpleExpression(String.format("%d^3-%d^3", bases[0], bases[1]));
	}
	
	
}
