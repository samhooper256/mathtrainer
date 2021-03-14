package suppliers.equations;

import static problems.Problem.*;
import static suppliers.NamedBooleanRef.*;
import static suppliers.NamedIntRange.*;

import math.BigFraction;
import problems.*;
import suppliers.*;

/**
 * @author Sam Hooper
 *
 */
public class VietasFormulasSupplier extends SettingsProblemSupplier {
	
	private static final RangeStore VALUES = RangeStore.of(-50, 50, -10, 10), DEGREE = RangeStore.of(2, 5, 2, 4);
	
	private final NamedIntRange values = of(VALUES, "Values of coefficients"), degree = of(DEGREE, "Degree of polynomial");
	
	public VietasFormulasSupplier() {
		settings(values, degree);
	}

	@Override
	public Problem get() {
		int[] cos = RAND.ints(intInclusive(degree) + 1, values.low(), values.high() + 1).toArray();
		if(cos[0] == 0) cos[0] = 1;
		String displayExpr = Prettifier.polynomialEqualsZero('x', cos);
		if(Math.random() <= 0.5)
			return Builder.of(String.format("Find the sum of the roots of %s:", Prettifier.ensureMath(displayExpr))).addResult(BigFraction.of(cos[1], -1)
					.divide(BigFraction.of(cos[0], 1))).build();
		else {
			BigFraction p = BigFraction.of(cos[cos.length - 1], cos[0]);
			if((cos.length - 1) % 2 != 0)
				p = p.negate();
			return Builder.of(String.format("Find the product of the roots of %s:", Prettifier.ensureMath(displayExpr))).addResult(p).build();
		}
	}

	@Override
	public String getName() {
		return "Vieta's Formulas";
	}

	
}