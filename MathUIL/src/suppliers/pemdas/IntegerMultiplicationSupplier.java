package suppliers.pemdas;

import static suppliers.NamedIntRange.*;

import java.util.List;
import java.util.stream.IntStream;

import problems.*;
import suppliers.*;
/**
 * @author Sam Hooper
 *
 */
public class IntegerMultiplicationSupplier extends SettingsProblemSupplier {
	
	private static final RangeStore VALUES = RangeStore.of(0, 999, 1, 20), TERMS = RangeStore.of(2, 5, 2, 2);
	private final NamedIntRange values, terms;
	
	public IntegerMultiplicationSupplier() {
		settings = List.of(values = of(VALUES, "Values"), terms = of(TERMS, "Terms"));
	}
	
	@Override
	public SimpleExpression get() {
		return SimpleExpression.multiplyTerms(IntStream.generate(() -> Problem.intInclusive(values)).limit(Problem.intInclusive(terms)).toArray());
	}
	
}
