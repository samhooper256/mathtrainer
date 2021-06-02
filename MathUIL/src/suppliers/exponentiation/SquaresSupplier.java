package suppliers.exponentiation;

import static suppliers.NamedIntRange.of;

import java.util.List;
import java.util.stream.*;

import problems.*;
import suppliers.*;

/**
 * @author Sam Hooper
 *
 */
public class SquaresSupplier extends RandomAndStackedSupplier {
	
	private static final RangeStore BASE = RangeStore.of(1, 200, 1, 30);
	
	private static SimpleExpression getWithBase(final int baseValue) {
		return new SimpleExpression(String.format("%d^2", baseValue));
	}
	
	private final NamedIntRange base;
	
	public SquaresSupplier() {
		addAllSettings(base = of(BASE, "Base"));
	}

	@Override
	public SimpleExpression getRandom() {
		return getWithBase(Problem.intInclusive(minBase(), maxBase()));
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
		return IntStream.rangeClosed(base.low(), base.high()).mapToObj(SquaresSupplier::getWithBase).collect(Collectors.toList());
	}
	
}
