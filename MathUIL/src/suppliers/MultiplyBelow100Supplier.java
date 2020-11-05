package suppliers;

import java.util.List;

import problems.IntegerMultiplication;
import problems.Problem;
import utils.IntRange;

/**
 * @author Sam Hooper
 *
 */
public class MultiplyBelow100Supplier extends SettingsProblemSupplier {
	
	private static final int MIN_DIST = 0, MAX_DIST = 20, LOW_DIST = 1, HIGH_DIST = 10;
	
	private final NamedSetting<IntRange> dist;
	
	public MultiplyBelow100Supplier() {
		this(LOW_DIST, HIGH_DIST);
	}
	
	public MultiplyBelow100Supplier(int lowDist, int highDist) {
		dist = NamedSetting.of(new IntRange(MIN_DIST, MAX_DIST, lowDist, highDist), "Amount below 100");
		settings = List.of(dist);
	}

	@Override
	public Problem get() {
		int term1 = 100 - Problem.intInclusive(lowDist(), highDist());
		int term2 = 100 - Problem.intInclusive(lowDist(), highDist());
		return IntegerMultiplication.fromTerms(Problem.shuffled(term1, term2));
	}
	
	public int lowDist() {
		return dist.ref().getLow();
	}
	
	public int highDist() {
		return dist.ref().getHigh();
	}
	
}
