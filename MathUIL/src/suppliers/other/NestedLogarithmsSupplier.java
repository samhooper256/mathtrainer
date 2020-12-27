package suppliers.other;

import static problems.Prettifier.*;
import static problems.Problem.*;
import static suppliers.NamedBooleanRef.*;
import static suppliers.NamedIntRange.*;

import java.math.BigInteger;
import java.util.ArrayDeque;

import math.Complex;
import problems.*;
import suppliers.*;

/**
 * @author Sam Hooper
 *
 */
public class NestedLogarithmsSupplier extends SettingsProblemSupplier {

	private static final RangeStore BASE = RangeStore.of(2, 5), VALUE = RangeStore.of(0, 4), LAYERS = RangeStore.of(2, 3);
	
	private final NamedIntRange base = of(BASE, "Bases of logarithms"), value = of(VALUE, "Values of logarithms"), layers = of(LAYERS, "Layers of nesting");
	
	public NestedLogarithmsSupplier() {
		settings(base, value, layers);
	}

	@Override
	public Problem get() {
		final int layerCount = intInclusive(layers);
		ArrayDeque<Integer> bases = new ArrayDeque<>();
		final int answer = intInclusive(value);
		BigInteger expVal = BigInteger.valueOf(answer);
		String displayArg = "";
		for(int i = 0; i < layerCount; i++) {
			int b = intInclusive(base);
			final int oldIntVal = expVal.intValueExact();
			if(oldIntVal > 100) {
				displayArg = num(oldIntVal);
				break;
			}
			expVal = BigInteger.valueOf(b).pow(oldIntVal);
			bases.addLast(b);
			if(expVal.compareTo(BigInteger.valueOf(10_000)) >= 0) {
				displayArg = pow(num(b), oldIntVal);
				break;
			}
		}
		if(displayArg.isEmpty())
			displayArg = num(expVal);
		String display = "";
		while(!bases.isEmpty()) {
			int pop = bases.removeLast();
			display = display.isEmpty() ? log(num(pop), displayArg) : log(num(pop), display);
		}
		return MultiValued.of(ensureMath(display)).addResult(new Complex(answer));
	}
	
}
