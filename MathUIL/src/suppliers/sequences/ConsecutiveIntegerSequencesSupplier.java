package suppliers.sequences;

import static problems.Problem.*;
import static suppliers.NamedIntRange.*;

import math.*;
import problems.*;
import suppliers.*;

/**
 * @author Sam Hooper
 *
 */
public class ConsecutiveIntegerSequencesSupplier extends SettingsProblemSupplier {
	private static final RangeStore TERMS = RangeStore.of(3, 100, 5, 25), START = RangeStore.of(0, 20, 0, 15);
	private final NamedIntRange terms = of(TERMS, "Terms in sequence"), start = of(START, "Start value of sequence");
	
	public ConsecutiveIntegerSequencesSupplier() {
		addAllSettings(terms, start);
	}

	@Override
	public Problem get() {
		int size = intInclusive(terms);
		ArithmeticSequence seq = randomSequenceOfSize(size, intInclusive(start));
		return ComplexValued.of(seq.toPartialString(3, " + "), seq.sum());
	}
	
	private static ArithmeticSequence randomSequenceOfSize(final int size, final int start) {
		int jump = RAND.nextInt(2) + 1;
		return new ArithmeticSequence(new Complex(start), new Complex(jump), size);
	}
	
}
