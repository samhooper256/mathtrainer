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
		addAllSettings(numbers);
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
		BigFraction a = BigFraction.of(high, low);
		return FracSupUtils.addScrambled(a, a.multiplicativeInverse(), true, false);
	}
	
	
}
