package suppliers.exponentiation;

import static suppliers.NamedIntRange.of;

import java.util.*;
import java.util.function.Supplier;

import problems.*;
import suppliers.*;

/**
 * 
 * @author Sam Hooper
 */
public class PowersOfHost implements ProblemSupplierHost {

	public static Set<Supplier<? extends ProblemSupplier>> getFactories() {
		return Set.of(createSupplier("2"), createSupplier("3"), createSupplier("5"));
	}
	
	private static Supplier<? extends ProblemSupplier> createSupplier(String number) {
		return () -> new PowersOfN(number);
	}
	
	private static class PowersOfN extends SettingsProblemSupplier {
		private static final RangeStore EXPONENT = RangeStore.of(1, 30, 1, 10);
		private final NamedIntRange exponent = of(EXPONENT, "Power Value");
		private final String number;
		
		private PowersOfN(String number) {
			this.number = number;
			addAllSettings(exponent);
		}

		@Override
		public Problem get() {
			return new SimpleExpression(String.format("%s^%d", number, Problem.intInclusive(exponent)));
		}
		
		@Override
		public String getName() {
			return String.format("Powers of %s", number);
		}
	}
	

}
