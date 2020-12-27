package suppliers.bases;

import static problems.Problem.*;
import static suppliers.NamedIntRange.*;

import math.Utils;
import problems.*;
import suppliers.*;

/**
 * @author Sam Hooper
 *
 */
public class FromBase10Supplier extends SettingsProblemSupplier {
private static final RangeStore BASE = RangeStore.of(Utils.MIN_RADIX, Utils.MAX_RADIX), VALUE = RangeStore.of(1, 10_000, 2, 200);
	
	private final NamedIntRange base = of(BASE, "Base of answer"), value = of(VALUE, "Base 10 value of original number");
	
	public FromBase10Supplier() {
		settings(base);
	}

	@Override
	public Problem get() {
		int b = base.low() == 10 && base.high() == 10 ? 10 : RAND.ints(base.low(), base.high() + 1).filter(i -> i != 10).findAny().getAsInt();
		int v = intInclusive(value);
		return Builder.of(String.format("What is %s in base %d?", Prettifier.ensureMath(Prettifier.base(Integer.toString(v), 10)), b))
				.addBaseResult(Utils.convertBase(Integer.toString(v), 10, b), b).build();
	}
}
