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
public class PowersOfPiSupplier extends SettingsProblemSupplier {
	public static final boolean DEFAULT_INCLUDE_PI_TO_E = true;
	public static final Problem TO_E_PROBLEM = new SimpleApproximation(
			Prettifier.PI_HTML + "<sup>" + Prettifier.E_HTML + "</sup>", Utils.PI_TO_E);
	
	private static final RangeStore EXPONENT = RangeStore.of(1, 20, 1, 6);
	private final NamedIntRange exponent = of(EXPONENT, "Power Value");
	private final NamedBooleanRef includePiToE = of(DEFAULT_INCLUDE_PI_TO_E, "Include pi to the power of e");
	
	public PowersOfPiSupplier() {
		settings = List.of(includePiToE, exponent);
	}

	@Override
	public Problem get() {
		if(includePiToE.get() && Math.random() <= (1d / (exponent.ref().valueRange() + 1)))
			return TO_E_PROBLEM;
		int exp = Problem.intInclusive(exponent);
		return new SimpleApproximation(Prettifier.PI_HTML + "<sup>" + exp + "</sup>", Utils.piTo(exp));
	}
}
