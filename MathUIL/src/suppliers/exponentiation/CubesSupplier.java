package suppliers.exponentiation;

import static suppliers.NamedIntRange.*;

import java.util.List;

import problems.*;
import suppliers.*;

/**
 * @author Sam Hooper
 *
 */
public class CubesSupplier extends SettingsProblemSupplier {
	
	private static final RangeStore BASE = RangeStore.of(1, 50, 1, 15);
	private final NamedIntRange base = of(BASE, "Base Value");
	
	public CubesSupplier() {
		settings = List.of(base);
	}

	@Override
	public Problem get() {
		return new SimpleExpression(String.format("%d^3", Problem.intInclusive(base)));
	}
	
	
}
