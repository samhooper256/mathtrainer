package suppliers.other;

import static problems.Problem.*;
import static suppliers.NamedBooleanRef.*;
import static suppliers.NamedIntRange.*;

import math.*;
import problems.*;
import suppliers.*;
import utils.*;

/**
 * @author Sam Hooper
 *
 */
public class ApproximationsWithRationalsSupplier extends SettingsProblemSupplier {
	
	private static final RandomSupplier<String> OP_SUPPLIER = RandomSupplier.of("*", "/");
	private static final RangeStore TERMS = RangeStore.of(1, 4, 2, 3), FRAC_NUMBERS = RangeStore.of(1, 12), SIG_FIGS = RangeStore.of(1, 6, 1, 3);
	
	private final NamedIntRange terms = of(TERMS, "terms"), fracNumbers = of(FRAC_NUMBERS, "Numbers in fractional terms"), sigFigs = of(SIG_FIGS, "Significant figures in non-fractional terms");
	
	public ApproximationsWithRationalsSupplier() {
		addAllSettings(terms, fracNumbers, sigFigs);
	}

	@Override
	public Problem get() {
		DisplayExpression exp = new DisplayExpression();
		final int ts = intInclusive(terms);
		for(int i = 1; i <= ts; i++) {
			exp.addTerm(makeTerm());
			if(i < ts)
				exp.addOperator(OP_SUPPLIER.get());
		}
		return Builder.of(exp.toMathML()).addResult(exp.evaluateAsComplex()).setApproximate(true).build();
	}
	
	/**
	 * 1/3 decimal number, 1/3 chance of mixed number, 1/3 chance of fraction.
	 * The resulting number has a 1/3 chance of being a percent.
	 * @return
	 */
	private Object makeTerm() {
		int ran = RAND.nextInt(3);
		if(ran == 0) {
			final StringBuilder sb = new StringBuilder(Problem.stringOfDigits(intInclusive(sigFigs)));
			return new Complex(sb.insert(sb.length(), '.').toString());
		}
		else if(ran == 1) {
			final BigFraction bigFrac = BigFraction.of(intInclusive(fracNumbers), intInclusive(2, Math.max(2, fracNumbers.high())));
			if(!bigFrac.isInteger()) //if not, fall through and return a fraction.
				return MixedNumber.of(intWithDigits(sigFigs), bigFrac);
		}
		return BigFraction.of(intInclusive(fracNumbers), intInclusive(fracNumbers));
	}
	
}
