package suppliers.exponentiation;

import static suppliers.NamedIntRange.*;

import java.util.List;

import problems.*;
import suppliers.*;

/**
 * @author Sam Hooper
 *
 */
public class PowersOf2Supplier extends SettingsProblemSupplier {
	private static final RangeStore EXPONENT = RangeStore.of(1, 30, 1, 10);
	private final NamedIntRange exponent = of(EXPONENT, "Power Value");
	
	public PowersOf2Supplier() {
		settings = List.of(exponent);
	}

	@Override
	public Problem get() {
		return new SimpleExpression(String.format("2^%d", Problem.intInclusive(exponent)));
	}
}
