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
		addAllSettings(aValue, bValue);
	}

	@Override
	public Problem get() {
		final int a = intInclusive(aValue), b = intInclusive(bValue);
		final BigFraction f1 = BigFraction.of(a, b);
		final BigFraction f2 = BigFraction.of(b, a + b);
		return FracSupUtils.addScrambled(f1, f2, true, false);
	}

	@Override
	public String getName() {
		return "Adding fractions of the form a/b + b/(a+b)";
	}
	
}
