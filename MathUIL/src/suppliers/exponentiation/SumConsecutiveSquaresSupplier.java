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
public class SumConsecutiveSquaresSupplier extends SettingsProblemSupplier {
	
	private static final RangeStore SMALL = RangeStore.of(0, 200, 1, 50);
	private final NamedIntRange small;
	
	public SumConsecutiveSquaresSupplier() {
		settings = List.of(small = of(SMALL, "Smaller term base"));
	}
	@Override
	public Problem get() {
		int term = intInclusive(small.low(), small.high());
		return new SimpleExpression(String.format("%s^2+%d^2", term, term + 1));
	}
	
	
}
