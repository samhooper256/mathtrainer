package suppliers.fractions;

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
public class SpecialFractionsSupplier extends SettingsProblemSupplier {
	
	private static final RangeStore A_VALUE = RangeStore.of(1, 25, 1, 17), B_VALUE = RangeStore.of(2, 25, 2, 17),
			MULTIPLE = RangeStore.of(1, 50, 1, 10), DIFFERENCE = RangeStore.of(1, 10, 1, 2);
	
	private final NamedIntRange aValue = of(A_VALUE, "Value of smaller numerator"), bValue = of(B_VALUE, "Value of smaller denominator"),
			multiple = of(MULTIPLE, "Multiple of first fraction"), difference = of(DIFFERENCE, "Difference of terms in second fraction from first");
	
	public SpecialFractionsSupplier() {
		settings(aValue, bValue, multiple, difference);
	}

	@Override
	public Problem get() {
		int a = intInclusive(aValue), b = intInclusive(bValue), k = intInclusive(multiple), d = intInclusive(difference);
		BigFraction frac1 = BigFraction.of(a, b), frac2 = BigFraction.of(k * a + d, k * b - d);
		return MultiValued.of(new DisplayExpression().addTerm(frac1).addOperator("-").addTerm(frac2).toMathML()).setLines(1.5)
				.addResult(frac1.subtract(frac2));
	}
	
}
