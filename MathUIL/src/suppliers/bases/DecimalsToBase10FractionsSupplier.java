package suppliers.bases;

import static problems.Problem.*;
import static problems.Prettifier.*;
import static suppliers.NamedIntRange.*;

import math.Utils;
import problems.*;
import suppliers.*;

/**
 * @author Sam Hooper
 *
 */
public class DecimalsToBase10FractionsSupplier extends SettingsProblemSupplier {
	
	private static final RangeStore DIGITS = RangeStore.of(1, 4, 1, 2), BASE = RangeStore.of(Utils.MIN_RADIX, Utils.MAX_RADIX);
	
	private final NamedIntRange digits = of(DIGITS, "Digits in decimals"), base = of(BASE, "Base of original number");
	
	public DecimalsToBase10FractionsSupplier() {
		addAllSettings(base, digits);
	}

	@Override
	public Problem get() {
		int b = base.low() == 10 && base.high() == 10 ? 10 : RAND.ints(base.low(), base.high() + 1).filter(i -> i != 10).findAny().getAsInt();
		int dig = intInclusive(digits);
		String dec = "." + Problem.stringOfDigits(dig, b);
		return Builder.of(String.format("Convert %s to a base 10 fraction:", ensureMath(base(dec, b))))
				.addResult(Utils.toBase10Fraction(dec, b)).build();
	}
	
}
