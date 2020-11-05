package suppliers;

import static problems.Problem.*;
import static suppliers.NamedIntRange.*;

import java.util.List;

import problems.*;

/**
 * @author Sam Hooper
 *
 */
public class SquaresEndingIn5Supplier extends SettingsProblemSupplier {
	
	private final RangeStore TENS = RangeStore.of(1, 15);
	
	private final NamedIntRange tens;
	
	public SquaresEndingIn5Supplier() {
		settings = List.of(tens = of(TENS, "\"Tens\" Digit"));
	}

	@Override
	public Problem get() {
		return new SinglePower(10 * intInclusive(tens.low(), tens.high()) + 5, 2);
	}
}
