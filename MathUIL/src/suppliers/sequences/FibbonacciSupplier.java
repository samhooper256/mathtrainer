package suppliers.sequences;

import static problems.Problem.intInclusive;
import static suppliers.NamedIntRange.of;

import math.*;
import problems.*;
import suppliers.*;

/**
 * @author Sam Hooper
 *
 */
public class FibbonacciSupplier extends SettingsProblemSupplier {
	private static final RangeStore SEEDS = RangeStore.of(1, 6, 1, 5), CONSIDERED_TERMS = RangeStore.of(2, 20, 2, 12);
	private final NamedIntRange seeds = of(SEEDS, "Values of first two terms in sequence"), consideredTerms = of(CONSIDERED_TERMS, "Number of terms considered");

	
	public FibbonacciSupplier() {
		addAllSettings(seeds);
	}


	@Override
	public Problem get() {
		FibSequence seq = new FibSequence(new Complex(intInclusive(seeds)), new Complex(intInclusive(seeds)));
		int considered = intInclusive(consideredTerms);
		if(Math.random() <= 0.5)
			return ComplexValued.of(String.format("The sum of the first %d terms of the Fib. sequence %s is:", considered, seq.toPartialString(5)), seq.sum(1, considered));
		else
			return ComplexValued.of(String.format("The sum of the Fib. sequence %s is:", seq.subSequence(1, considered).toPartialString(Math.min(4, considered), 2, ", ")), seq.sum(1, considered));
	}
	
}
