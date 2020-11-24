package suppliers.fractions;

import static problems.Problem.*;
import static suppliers.NamedBooleanRef.*;
import static suppliers.NamedIntRange.*;

import java.util.List;

import math.BigFraction;
import problems.*;
import suppliers.*;

/**
 * @author Sam Hooper
 *
 */
public class AddingOrSubtractingAnyFractionsSupplier extends SettingsProblemSupplier {
	
	private static final RangeStore NUM1 = RangeStore.of(1, 50, 1, 20), NUM2 = RangeStore.of(1, 50, 1, 20),
			DENOM1 = RangeStore.of(1, 50, 1, 20), DENOM2 = RangeStore.of(1, 50, 1, 20);
	
	private final NamedIntRange num1 = of(NUM1, "First numerator"), num2 = of(NUM2, "Second numerator"),
			denom1 = of(DENOM1, "First denominator"), denom2 = of(DENOM2, "First denominator");
	
	public AddingOrSubtractingAnyFractionsSupplier() {
		settings(num1, denom1, num2, denom2);
	}

	@Override
	public Problem get() {
		int a = intInclusive(num1), b = intInclusive(num2), c = intInclusive(denom1), d = intInclusive(denom2);
		BigFraction f1 = BigFraction.of(a, b), f2 = BigFraction.of(c, d);
		List<BigFraction> shuf = shuffled(f1, f2);
		f1 = shuf.get(0); f2 = shuf.get(1);
		String op = Math.random() <= 0.5 ? "-" : "+";
		BigFraction res = "+".equals(op) ? f1.add(f2) : f1.subtract(f2);
		Number n1, n2;
		n1 = f1.isImproper() && Math.random() <= 0.5 ? f1.toMixedNumber() : f1;
		n2 = f2.isImproper() && Math.random() <= 0.5 ? f2.toMixedNumber() : f2;
		return MultiValued.of(new DisplayExpression().addTerm(n1).addOperator(op).addTerm(n2).toMathML()).setLines(1.5).addResult(res);
	}
	
	
	
}
