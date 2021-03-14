package suppliers.remainder;

import static problems.Problem.*;
import static suppliers.NamedIntRange.*;

import java.util.List;

import problems.*;
import suppliers.*;

/**
 * @author Sam Hooper
 *
 */
public class RemainderWithOperationsSupplier extends SettingsProblemSupplier {
	private static final RangeStore TERMS = RangeStore.of(2, 5, 2, 4), DIGITS = RangeStore.of(1, 5, 1, 4), DIVISOR = RangeStore.of(1, 20, 3, 12);
	private static final List<String> OPERATORS = List.of("+", "-", "*");
	private final NamedIntRange terms, digits, divisor;
	
	public RemainderWithOperationsSupplier() {
		addAllSettings(terms = of(TERMS, "Terms in expression"), digits = of(DIGITS, "Digits in expression terms"), divisor = of(DIVISOR, "Divisor value"));
	}

	@Override
	public Problem get() {
		String exp = Problem.makeExpr(Problem.intInclusive(terms), digits, OPERATORS);
		return new Remainder("(" + exp + ")", Problem.intInclusive(divisor));
	}
	
}
