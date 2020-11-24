package suppliers.fractions;

import static problems.Problem.*;
import static suppliers.NamedIntRange.*;

import java.util.List;

import math.BigFraction;
import problems.*;
import suppliers.*;

/**
 * <p>Sum of fractions of the form a/b + b/(a+b).</p>
 * @author Sam Hooper
 *
 */
public class AddingABPlusBAPlusBSupplier extends SettingsProblemSupplier {
	
	private static final RangeStore A_VALUE = RangeStore.of(1, 20, 1, 10), B_VALUE = RangeStore.of(2, 20, 2, 13);
	
	private final NamedIntRange aValue = of(A_VALUE, "Value of a"), bValue = of(B_VALUE, "Value of b");
	
	public AddingABPlusBAPlusBSupplier() {
		settings(aValue, bValue);
	}

	@Override
	public Problem get() {
		final int a = intInclusive(aValue), b = intInclusive(bValue);
		List<BigFraction> shuf = shuffled(BigFraction.of(a, b), BigFraction.of(b, a + b));
		DisplayExpression exp = new DisplayExpression().addTerm(shuf.get(0)).addOperator("+").addTerm(shuf.get(1));
		return MultiValued.of(exp.toMathML()).setLines(1.5).addResult(shuf.get(0).add(shuf.get(1)));
	}
	
	
	
}
