package suppliers.fractions;

import static problems.Problem.*;
import static suppliers.NamedIntRange.*;

import math.BigFraction;
import problems.*;
import suppliers.*;

/**
 * @author Sam Hooper
 *
 */
public class AddingMultiplicativeInversesSupplier extends SettingsProblemSupplier {
	
	private static final RangeStore NUMBERS = RangeStore.of(2, 20, 2, 14);
	
	private final NamedIntRange numbers = of(NUMBERS, "Numbers in fractions");
	
	public AddingMultiplicativeInversesSupplier() {
		settings(numbers);
	}

	@Override
	public Problem get() {
		DisplayExpression exp = new DisplayExpression();
		int low = intInclusive(numbers), high = intInclusive(numbers);
		if(high < low) {
			int temp = high;
			high = low;
			low = temp;
		}
		if(high == low) high++;
		Number a = BigFraction.of(high, low);
		Number b = ((BigFraction) a).multiplicativeInverse();
		BigFraction res = ((BigFraction) a).add((BigFraction) b);
		if(Math.random() <= 0.25)
			a = ((BigFraction) a).toMixedNumber();
		if(Math.random() <= 0.5) {
			Number temp = a;
			a = b;
			b = temp;
		}
		exp.addTerm(a).addOperator("+").addTerm(b);
		return MultiValued.of(exp.toMathML()).setLines(1.5).addResult(res);
	}
	
	
}
