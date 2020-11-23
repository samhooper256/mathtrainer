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
public class ArithmeticSequencesSupplier extends SettingsProblemSupplier {
	private static final RangeStore TERMS = RangeStore.of(1, 100, 1, 30), START_DIGITS = RangeStore.of(1, 4, 1, 2), DIFF_DIGITS = RangeStore.of(1, 4, 1, 2);
	private final NamedIntRange terms = of(TERMS, "Terms in sequence"), startDigits = of(START_DIGITS, "Digits in first term"), diffDigits = of(DIFF_DIGITS, "Digits in common difference");
	
	public ArithmeticSequencesSupplier() {
		settings(terms, startDigits, diffDigits);
	}

	@Override
	public Problem get() {
		StringBuilder start = new StringBuilder(stringOfDigits(intInclusive(startDigits)));
		start.insert(intInclusive(0, start.length()), '.');
		StringBuilder diff = new StringBuilder(stringOfDigits(intInclusive(diffDigits)));
		diff.insert(intInclusive(0, diff.length()), '.');
		ArithmeticSequence seq = new ArithmeticSequence(new Complex(start.toString()), new Complex(diff.toString()), intInclusive(terms));
		return ComplexValued.of(seq.toPartialString(3, " + "), seq.sum());
	}
	
}
