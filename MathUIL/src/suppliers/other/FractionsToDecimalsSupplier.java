package suppliers.other;

import static problems.Problem.*;
import static suppliers.NamedIntRange.*;

import java.math.*;

import math.BigFraction;
import problems.*;
import suppliers.*;

/**
 * @author Sam Hooper
 *
 */
public class FractionsToDecimalsSupplier extends SettingsProblemSupplier {
	
	private static final RangeStore DENOM = RangeStore.of(2, 100, 2, 100), DIGITS = RangeStore.of(1, 10, 4, 4);
	
	private final NamedIntRange denom = of(DENOM, "Denominator of fraction"), digits = of(DIGITS, "Number of decimal places");
	
	public FractionsToDecimalsSupplier() {
		settings(denom, digits);
	}

	@Override
	public Problem get() {
		int den = intInclusive(denom), num = intInclusive(1, den - 1), dig = intInclusive(digits);
		BigFraction frac = BigFraction.of(num, den);
		BigDecimal decimal = frac.toBigDecimal(new MathContext(dig + 1));
		return Builder.of(String.format("What is %s accurate to %d decimal places?%n", Prettifier.ensureMath(Prettifier.frac(frac)), dig))
				.addMinimumDigitsAfterDecimalResult(decimal.toPlainString(), dig).build();
	}
	
}
