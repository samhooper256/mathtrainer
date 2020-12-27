package suppliers.pemdas;

import java.util.*;

import math.Evaluator;
import problems.*;
import suppliers.*;
import utils.*;
import utils.refs.IntRange;

/**
 * @author Sam Hooper
 *
 */
@Named("PEMDAS Approximation")
public class PEMDASApproximationSupplier extends SettingsProblemSupplier {
	
	public static final int MIN_TERMS = 2, MAX_TERMS = 5, MIN_DIGITS = 1, MAX_DIGITS = 6;
	
	private static final int DEFAULT_MIN_TERMS = 2, DEFAULT_MAX_TERMS = 4, DEFAULT_MIN_DIGITS = 1, DEFUALT_MAX_DIGITS = 5;
	private static final List<String> DEFAULT_OPERATORS = List.of("-", "+", "*", "/");
	
	private final List<String> operators;
	private final NamedSetting<IntRange> termRange, digitRange;
	
	public PEMDASApproximationSupplier() {
		this(DEFAULT_MIN_TERMS, DEFAULT_MAX_TERMS, DEFAULT_MIN_DIGITS, DEFUALT_MAX_DIGITS, DEFAULT_OPERATORS);
	}
	
	/**
	 * Does <b>NOT</b> defensively copy the {@link List} of operators.
	 */
	public PEMDASApproximationSupplier(int minTerms, int maxTerms, int minDigits, int maxDigits, final List<String> operators) {
		this.operators = operators;
		this.termRange = NamedSetting.of(new IntRange(MIN_TERMS, MAX_TERMS, minTerms, maxTerms), "Terms");
		this.digitRange = NamedSetting.of(new IntRange(MIN_DIGITS, MAX_DIGITS, minDigits, maxDigits), "Digits");
		settings = List.of(termRange, digitRange);
	}

	@Override
	public Problem get() {
		String exp = Problem.makeExpr(Problem.intInclusive(minTerms(), maxTerms()), minDigits(), maxDigits(), operators);
		return Builder.approximation(Prettifier.pretty(exp), Evaluator.evaluateAsBigDecimalExact(exp));
	}
	
	
	public int minTerms() {
		return termRange.ref().getLow();
	}
	
	public int maxTerms() {
		return termRange.ref().getMax();
	}
	
	public int minDigits() {
		return digitRange.ref().getLow();
	}
	
	public int maxDigits() {
		return digitRange.ref().getHigh();
	}
	
}
