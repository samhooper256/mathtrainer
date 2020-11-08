package suppliers;

import static suppliers.NamedIntRange.*;

import java.util.List;
import java.util.stream.IntStream;

import problems.*;
/**
 * @author Sam Hooper
 *
 */
public class IntegerMultiplicationSupplier extends SettingsProblemSupplier {
	
	private static final RangeStore DIGITS = RangeStore.of(1, 5, 1, 3), TERMS = RangeStore.of(2, 5, 2, 3);
	private final NamedIntRange digits, terms;
	
	public IntegerMultiplicationSupplier() {
		settings = List.of(digits = of(DIGITS, "Digits"), terms = of(TERMS, "Terms"));
	}
	
	@Override
	public SimpleExpression get() {
		int[] termsArr = IntStream.generate(() -> Problem.intWithDigits(Problem.intInclusive(digits))).limit(Problem.intInclusive(terms)).toArray();
		return SimpleExpression.multiplyTerms(termsArr);
	}
	
}
