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
public class ToBase10Supplier extends SettingsProblemSupplier {
	
	private static final RangeStore BASE = RangeStore.of(Utils.MIN_RADIX, Utils.MAX_RADIX), VALUE = RangeStore.of(1, 10_000, 2, 200);
	
	private final NamedIntRange base = of(BASE, "Base of original number"), value = of(VALUE, "Base 10 value of original number");
	
	public ToBase10Supplier() {
		addAllSettings(base, value);
	}

	@Override
	public Problem get() {
		int b = base.low() == 10 && base.high() == 10 ? 10 : RAND.ints(base.low(), base.high() + 1).filter(i -> i != 10).findFirst().getAsInt();
		int v = intInclusive(value);
		return Builder.of(String.format("What is %s in base 10?", Prettifier.ensureMath(Prettifier.base(Utils.convertBase(Integer.toString(v), 10, b), b))))
				.addBaseResult(Integer.toString(v), 10).build();
	}
	
}
