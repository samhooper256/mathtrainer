package suppliers.remainder;

import static suppliers.NamedIntRange.*;

import java.util.List;

import problems.*;
import suppliers.*;

/**
 * @author Sam Hooper
 *
 */
public class Mod9Supplier extends SettingsProblemSupplier {
	
	private static final RangeStore DIGITS = RangeStore.of(1, 5);
	private final NamedIntRange digits;
	
	public Mod9Supplier() {
		settings = List.of(digits = of(DIGITS, "Digits in terms"));
	}

	@Override
	public Problem get() {
		return new Remainder(Problem.intWithDigits(digits), 9);
	}

	@Override
	public String getName() {
		return "Remainder when divided by 9";
	}
	
}
