package suppliers.exponentiation;

import static problems.Problem.*;
import static suppliers.NamedIntRange.*;

import java.util.List;

import problems.*;
import suppliers.*;

/**
 * @author Sam Hooper
 *
 */
public class SquaresEndingIn5Supplier extends SettingsProblemSupplier {
	
	private final RangeStore TENS = RangeStore.of(1, 15);
	
	private final NamedIntRange tens;
	
	public SquaresEndingIn5Supplier() {
		addAllSettings(tens = of(TENS, "\"Tens\" Digit"));
	}

	@Override
	public SimpleExpression get() {
		return new SimpleExpression(String.format("%d^2", 10 * intInclusive(tens.low(), tens.high()) + 5));
	}
}
