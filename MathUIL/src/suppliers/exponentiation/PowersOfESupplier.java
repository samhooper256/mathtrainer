package suppliers.exponentiation;

import static suppliers.NamedBooleanRef.*;
import static suppliers.NamedIntRange.*;

import java.util.List;

import math.Utils;
import problems.*;
import suppliers.*;

/**
 * @author Sam Hooper
 *
 */
public class PowersOfESupplier extends SettingsProblemSupplier {
	public static final boolean DEFAULT_INCLUDE_E_TO_PI = true;
	public static final Problem TO_PI_PROBLEM = new SimpleApproximation(
			ExpressionPrettifier.E_HTML + "<sup>" + ExpressionPrettifier.PI_HTML + "</sup>", Utils.E_TO_PI);
	
	private static final RangeStore EXPONENT = RangeStore.of(1, 20, 1, 6);
	private final NamedIntRange exponent = of(EXPONENT, "Power Value");
	private final NamedBooleanRef includeEToPi = of(DEFAULT_INCLUDE_E_TO_PI, "Include e to the power of pi");
	
	public PowersOfESupplier() {
		settings = List.of(includeEToPi, exponent);
	}

	@Override
	public Problem get() {
		if(includeEToPi.get() && Math.random() <= (1d / (exponent.ref().valueRange() + 1)))
			return TO_PI_PROBLEM;
		int exp = Problem.intInclusive(exponent);
		return new SimpleApproximation(ExpressionPrettifier.E_HTML + "<sup>" + exp + "</sup>", Utils.eTo(exp));
	}
}

