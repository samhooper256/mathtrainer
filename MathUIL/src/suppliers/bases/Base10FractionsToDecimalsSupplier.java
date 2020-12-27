package suppliers.bases;

import static problems.Problem.*;
import static problems.Prettifier.*;
import static suppliers.NamedBooleanRef.*;
import static suppliers.NamedIntRange.*;
import static suppliers.NamedIntRange.of;

import math.*;
import problems.*;
import suppliers.*;

/**
 * @author Sam Hooper
 *
 */
public class Base10FractionsToDecimalsSupplier extends SettingsProblemSupplier {
	
	private static final RangeStore BASE = RangeStore.of(Utils.MIN_RADIX, Utils.MAX_RADIX), DIGITS = RangeStore.of(1, 4, 1, 3);
	
	private final NamedIntRange digits = of(DIGITS, "Digits in answer"), base = of(BASE, "Base of answer");
	
	public Base10FractionsToDecimalsSupplier() {
		settings(base, digits);
	}

	@Override
	public Problem get() {
		int b = base.low() == 10 && base.high() == 10 ? 10 : RAND.ints(base.low(), base.high() + 1).filter(i -> i != 10).findAny().getAsInt();
		int dig = intInclusive(digits);
		String dec = "." + Problem.stringOfDigits(dig, b);
		BigFraction frac = Utils.toBase10Fraction(dec, b);
		return Builder.of(String.format("Convert %s to a base %d decimal", ensureMath(frac(frac)), b)).addBaseResult(dec, b).build();
	}
	
	

}
