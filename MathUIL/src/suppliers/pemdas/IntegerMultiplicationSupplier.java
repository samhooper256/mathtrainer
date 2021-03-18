package suppliers.pemdas;

import static suppliers.NamedIntRange.*;

import java.util.*;
import java.util.stream.IntStream;

import problems.*;
import suppliers.*;
import utils.EnumSetView;
import utils.refs.*;
/**
 * <p>Supports {@link SupplierMode#RANDOM} and {@link SupplierMode#STACKED} (under certain conditions). </p>
 * @author Sam Hooper
 *
 */
public class IntegerMultiplicationSupplier extends RandomAndStackedSupplier {
	
	private static final int DEFAULT_TERM_COUNT = 2;

	private static final RangeStore VALUES = RangeStore.of(0, 999, 1, 20), TERMS = RangeStore.of(2, 5, DEFAULT_TERM_COUNT, DEFAULT_TERM_COUNT);
	private final NamedIntRange values, terms;
	
	public IntegerMultiplicationSupplier() {
		addAllSettings(values = of(VALUES, "Values"), terms = of(TERMS, "Terms"));
	}

	@Override
	protected Problem getRandom() {
		return SimpleExpression.multiplyTerms(IntStream.generate(() -> Problem.intInclusive(values))
				.limit(Problem.intInclusive(terms)).toArray());
	}
	
	@Override
	protected List<Problem> generateAllPossibleProblems() {
		assert supportsStacked();
		List<Problem> list = new ArrayList<>();
		for(int firstTerm = values.low(); firstTerm <= values.high(); firstTerm++) {
			for(int secondTerm = values.low(); secondTerm <= values.high(); secondTerm++) {
				list.add(SimpleExpression.multiplyTerms(firstTerm, secondTerm));
			}
		}
		assert list.size() >= 1;
		return list;
	}
	
	@Override
	protected boolean supportsStacked() {
		return terms.low() == 2 && terms.high() == 2 && values.high() <= 33;
	}
	
}
