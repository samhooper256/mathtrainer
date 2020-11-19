package suppliers.exponentiation;

import static suppliers.NamedIntRange.*;

import java.util.List;

import problems.*;
import suppliers.*;

/**
 * @author Sam Hooper
 *
 */
public class PowersOf3Supplier extends SettingsProblemSupplier {
	private static final RangeStore EXPONENT = RangeStore.of(1, 20, 1, 6);
	private final NamedIntRange exponent = of(EXPONENT, "Power Value");
	
	public PowersOf3Supplier() {
		settings = List.of(exponent);
	}

	@Override
	public Problem get() {
		return new SimpleExpression(String.format("3^%d", Problem.intInclusive(exponent)));
	}
}
