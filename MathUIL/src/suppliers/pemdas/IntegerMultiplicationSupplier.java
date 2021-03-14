package suppliers.pemdas;

import static suppliers.NamedIntRange.*;

import java.util.*;
import java.util.stream.IntStream;

import problems.*;
import suppliers.*;
import utils.EnumSetView;
/**
 * <p>Supports {@link SupplierMode#RANDOM} and {@link SupplierMode#STACKED} (under certain conditions). </p>
 * @author Sam Hooper
 *
 */
public class IntegerMultiplicationSupplier extends SettingsProblemSupplier {
	
	private static final int DEFAULT_TERM_COUNT = 2;

	private static final EnumSetView<SupplierMode> SUPPORTED_UNDER_ANY_SETTINGS = EnumSetView.of(SupplierMode.RANDOM, SupplierMode.STACKED);
	private static final RangeStore VALUES = RangeStore.of(0, 999, 1, 20), TERMS = RangeStore.of(2, 5, DEFAULT_TERM_COUNT, DEFAULT_TERM_COUNT);
	private final NamedIntRange values, terms;
	
	private List<SimpleExpression> stackedProblems = null;
	private int stackedProblemsMaxIndex = -1, lastStackedProblemsIndex = -1;
	private SupplierMode mode;
	
	/** Will be {@link SupplierMode#RANDOM} by default.*/
	public IntegerMultiplicationSupplier() {
		this.mode = SupplierMode.RANDOM;
		addAllSettings(values = of(VALUES, "Values"), terms = of(TERMS, "Terms"));
	}
	
	@Override
	public SimpleExpression get() {
		return switch(mode) {
			case RANDOM -> {
				yield SimpleExpression.multiplyTerms(IntStream.generate(() -> Problem.intInclusive(values))
						.limit(Problem.intInclusive(terms)).toArray());
			}
			case STACKED -> {
				lastStackedProblemsIndex = Problem.intExclusive(stackedProblemsMaxIndex);
				yield stackedProblems.get(lastStackedProblemsIndex);
			}
			default -> throw new IllegalStateException(String.format("Should not be in mode: %s", mode));
		};
	}

	private List<SimpleExpression> generateAllPossibleProblems() {
		assert terms.high() == 2 && terms.low() == 2;
		List<SimpleExpression> list = new ArrayList<>();
		for(int firstTerm = values.low(); firstTerm <= values.high(); firstTerm++) {
			for(int secondTerm = values.low(); secondTerm <= values.high(); secondTerm++) {
				list.add(SimpleExpression.multiplyTerms(firstTerm, secondTerm));
			}
		}
		assert list.size() >= 1;
		return list;
	}
	
	@Override
	public SupplierMode getCurrentMode() {
		return mode;
	}

	@Override
	public boolean supportsUnderCurrentSettings(SupplierMode mode) {
		if(mode == SupplierMode.RANDOM) {
			return true;
		}
		if(mode == SupplierMode.STACKED && terms.low() == 2 && terms.high() == 2 && values.high() <= 100) {
			return true;
		}
		return false;
	}
	
	@Override
	public EnumSetView<SupplierMode> getSupportedModesUnderCurrentSettings() {
		EnumSet<SupplierMode> modes = EnumSet.of(SupplierMode.RANDOM);
		if(supportsUnderCurrentSettings(SupplierMode.STACKED))
			modes.add(SupplierMode.STACKED);
		return EnumSetView.of(modes);
	}

	
	@Override
	public EnumSetView<SupplierMode> getSupportedModesUnderAnySettings() {
		return SUPPORTED_UNDER_ANY_SETTINGS;
	}

	@Override
	public void setMode(SupplierMode newMode) {
		if(mode == newMode) {
			return;
		}
		switch(newMode) {
			case RANDOM -> {
				mode = SupplierMode.RANDOM;
			}
			case STACKED -> {
				stackedProblems = generateAllPossibleProblems();
				stackedProblemsMaxIndex = stackedProblems.size();
				mode = SupplierMode.STACKED;
			}
			default -> throw new UnsupportedOperationException(String.format("%s is unsupported", newMode));
		}
	}

	@Override
	public void strictlySolved(Problem p) {
		if(stackedProblemsMaxIndex <= 1) {
			stackedProblemsMaxIndex = stackedProblems.size();
		}
		else {
			stackedProblemsMaxIndex--;
			Collections.swap(stackedProblems, lastStackedProblemsIndex, stackedProblemsMaxIndex);
		}
	}
	
	
	
}
