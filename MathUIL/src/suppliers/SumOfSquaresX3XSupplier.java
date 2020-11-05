package suppliers;

import static problems.Problem.*;
import static suppliers.NamedIntRange.*;

import java.util.List;

import problems.*;

/**
 * @author Sam Hooper
 *
 */
public class SumOfSquaresX3XSupplier extends SettingsProblemSupplier {
	
	/**
	 * The base of the smaller term; that is, "a" in "a^2 + (3*a)^2"
	 */
	private static final RangeStore BASE = RangeStore.of(1, 30, 3, 25);
	private final NamedIntRange base;
	
	public SumOfSquaresX3XSupplier() {
		settings = List.of(base = of(BASE, "Base of smaller term"));
	}

	@Override
	public Problem get() {
		int base1 = intInclusive(base.low(), base.high());
		int base2 = 3 * base1;
		return new AnyExpression(String.format("%d^2+%d^2", base1, base2));
	}
}
