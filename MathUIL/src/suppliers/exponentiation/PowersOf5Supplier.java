package suppliers.exponentiation;

import static suppliers.NamedIntRange.*;

import java.util.List;

import problems.*;
import suppliers.*;

/**
 * @author Sam Hooper
 *
 */
public class PowersOf5Supplier extends SettingsProblemSupplier {
	private static final RangeStore EXPONENT = RangeStore.of(1, 15, 1, 6);
	private final NamedIntRange exponent = of(EXPONENT, "Power Value");
	
	public PowersOf5Supplier() {
		settings = List.of(exponent);
	}

	@Override
	public Problem get() {
		return new SimpleExpression(String.format("5^%d", Problem.intInclusive(exponent)));
	}
}
