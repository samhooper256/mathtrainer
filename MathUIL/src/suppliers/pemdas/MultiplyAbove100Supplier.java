package suppliers.pemdas;

import java.util.*;

import base.*;
import problems.*;
import suppliers.*;
import utils.*;
import utils.refs.IntRange;

/**
 * @author Sam Hooper
 *
 */
public class MultiplyAbove100Supplier extends SettingsProblemSupplier {
	private static final int MIN_DIST = 0, MAX_DIST = 20, LOW_DIST = 1, HIGH_DIST = 10, MIN_MULTIPLE = 1, MAX_MULTIPLE = 9, LOW_MULTIPLE = 1, HIGH_MULTIPLE = 2;
	final NamedSetting<IntRange> dist, multiple;
	
	public MultiplyAbove100Supplier() {
		this(LOW_DIST, HIGH_DIST, LOW_MULTIPLE, HIGH_MULTIPLE);
	}
	
	public MultiplyAbove100Supplier(int lowDist, int highDist, int lowMult, int highMult) {
		dist = NamedSetting.of(new IntRange(MIN_DIST, MAX_DIST, lowDist, highDist), "Amount above 100");
		multiple = NamedSetting.of(new IntRange(MIN_MULTIPLE, MAX_MULTIPLE, lowMult, highMult), "Multiple of 100");
		addAllSettings(dist, multiple);
	}
	
	@Override
	public Problem get() {
		int mult = 100 * Problem.intInclusive(lowMultiple(), highMultiple());
		int term1 = mult + Problem.intInclusive(lowDist(), highDist()), term2 = mult + Problem.intInclusive(lowDist(), highDist());
		return SimpleExpression.multiplyTerms(Problem.shuffled(term1, term2));
	}
	
	public int lowDist() {
		return dist.ref().getLow();
	}
	
	public int highDist() {
		return dist.ref().getHigh();
	}
	
	public int lowMultiple() {
		return multiple.ref().getLow();
	}
	
	public int highMultiple() {
		return multiple.ref().getHigh();
	}
}
