package suppliers.remainder;
import static suppliers.NamedIntRange.*;

import java.util.List;

import problems.*;
import suppliers.*;

/**
 * @author Sam Hooper
 *
 */
public class RemainderWithExponentsSupplier extends SettingsProblemSupplier {
	private static final RangeStore BASE = RangeStore.of(1, 25, 5, 21), EXPONENT = RangeStore.of(1, 25, 4, 22), DIVISOR = RangeStore.of(2, 25, 7, 22);
	private final NamedIntRange base, exponent, divisor;
	
	public RemainderWithExponentsSupplier() {
		addAllSettings(base = of(BASE, "Base Value"), exponent = of(EXPONENT, "Exponent Value"), divisor = of(DIVISOR, "Divisor Value"));
	}

	@Override
	public Problem get() {
		int baseV = Problem.intInclusive(base), exponentV = Problem.intInclusive(exponent);
		return new Remainder(baseV + "^" + exponentV, Problem.intInclusive(divisor));
	}
}
