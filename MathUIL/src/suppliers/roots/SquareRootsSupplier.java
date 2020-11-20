package suppliers.roots;

import static problems.Problem.*;
import static suppliers.NamedBooleanRef.*;
import static suppliers.NamedIntRange.*;

import java.math.BigDecimal;
import java.util.List;

import problems.*;
import suppliers.*;

/**
 * @author Sam Hooper
 *
 */
public class SquareRootsSupplier extends SettingsProblemSupplier {
	private static final BigDecimal APPROX_PERCENT = new BigDecimal("0.0005");
	private static final RangeStore RADICAND = RangeStore.of(1, 50, 1, 10);
	private final NamedIntRange radicand = of(RADICAND, "Radicand value");
	
	
	public SquareRootsSupplier() {
		settings = List.of(radicand);
	}

	@Override
	public Problem get() {
		int rc = Problem.intInclusive(radicand);
		return new SimpleApproximation(APPROX_PERCENT, sqrtFormatted(rc), BigDecimal.valueOf(Math.sqrt(rc)));
	}
	
	private static String sqrtFormatted(final int radicand) {
		return String.format("<math><msqrt><mn>%d</mn></msqrt></math>", radicand);
	}
}
