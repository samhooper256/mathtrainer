package suppliers.remainder;

import static suppliers.NamedIntRange.of;

import java.util.*;
import java.util.function.Supplier;

import problems.*;
import suppliers.*;

/**
 * 
 * @author Sam Hooper
 */
public class ModSupplierHost implements ProblemSupplierHost {

	public static Set<Supplier<? extends ProblemSupplier>> getFactories() {
		return Set.of(supplierFor(3), supplierFor(9), supplierFor(11));
	}

	public static Supplier<? extends ProblemSupplier> supplierFor(int divisor) {
		return () -> new ModSupplier(divisor);
	}
	
	private static class ModSupplier extends SettingsProblemSupplier {
		private static final RangeStore DIGITS = RangeStore.of(1, 5);
		private final NamedIntRange digits;
		private final int number;
		
		public ModSupplier(final int number) {
			this.number = number;
			addAllSettings(digits = of(DIGITS, "Digits in terms"));
		}

		@Override
		public Problem get() {
			return new Remainder(Problem.intWithDigits(digits), number);
		}

		@Override
		public String getName() {
			return String.format("Remainder when divided by %d", number);
		}
		
	}

}
