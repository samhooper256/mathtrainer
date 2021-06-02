package suppliers.exponentiation;

import static suppliers.NamedIntRange.*;

import java.util.List;
import java.util.stream.*;

import problems.*;
import suppliers.*;

/**
 * @author Sam Hooper
 *
 */
public class CubesSupplier extends RandomAndStackedSupplier {
	
	private static final RangeStore BASE = RangeStore.of(1, 50, 1, 15);
	
	private static SimpleExpression getWithBase(final int baseValue) {
		return new SimpleExpression(String.format("%d^3", baseValue));
	}
	
	private final NamedIntRange base;
	
	public CubesSupplier() {
		addAllSettings(base = of(BASE, "Base Value"));
	}

	@Override
	public Problem getRandom() {
		return getWithBase(Problem.intInclusive(base));
	}
	
	public int minBase() {
		return base.low();
	}
	
	public int maxBase() {
		return base.high();
	}
	
	@Override
	protected boolean supportsStacked() {
		return true; //always support stacked, since there will always be < 1000 problems.
	}

	@Override
	protected List<Problem> generateAllPossibleProblems() {
		return IntStream.rangeClosed(base.low(), base.high()).mapToObj(CubesSupplier::getWithBase).collect(Collectors.toList());
	}
}
