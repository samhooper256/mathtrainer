package suppliers;

import static problems.Problem.*;
import static suppliers.NamedIntRange.*;

import java.util.List;

import problems.*;
/**
 * Produces {@link IntegerMultiplication} {@link Problem Problems} with two terms. One is any integer and the other is an integer ending in 5.
 * @author Sam Hooper
 *
 */
public class Multiply5EndSupplier extends SettingsProblemSupplier {
	
	private static final RangeStore DIGITS5 = RangeStore.of(1, 3, 2, 2), DIGITS = RangeStore.of(1, 3);
	private final NamedIntRange digits5, digitsN;
	
	public Multiply5EndSupplier() {
		settings = List.of(digits5 = of(DIGITS5, "Digits in term ending in 5"), digitsN = of(DIGITS, "Digits in term not ending in 5"));
	}

	@Override
	public Problem get() {
		int term5 = 10 * Problem.intWithDigits(lowDigits5() - 1, highDigits5() - 1) + 5;
		int term = Problem.intWithDigits(lowDigitsN(), highDigitsN());
		return IntegerMultiplication.fromTerms(Problem.shuffled(term, term5));
	}
	
	public int lowDigits5() {
		return digits5.low();
	}
	
	public int highDigits5() {
		return digits5.high();
	}
	
	public int lowDigitsN() {
		return digitsN.low();
	}
	
	public int highDigitsN() {
		return digitsN.high();
	}
	
}
