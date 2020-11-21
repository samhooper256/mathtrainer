package suppliers.roots;

import static problems.Problem.*;
import static suppliers.NamedBooleanRef.*;
import static suppliers.NamedIntRange.*;

import math.*;
import problems.*;
import suppliers.*;

/**
 * @author Sam Hooper
 *
 */
//TODO some way to make the values be more reasonable?
public class OtherRootsSupplier extends SettingsProblemSupplier {
	private static final RangeStore ROOT = RangeStore.of(1, 7), RADICAND_BASE = RangeStore.of(1, 30, 1, 13);
	private final NamedIntRange root = of(ROOT, "Root"), radicandBase = of(RADICAND_BASE, "Base of the radicand");
	
	public OtherRootsSupplier() {
		settings(root, radicandBase);
	}

	@Override
	public Problem get() {
		long rb = Problem.intInclusive(radicandBase);
		long rootBottom = Problem.intInclusive(root);
		long rootTop = rootBottom == 1L ? 1L : Problem.longInclusive(1, rootBottom - 1);
		return ComplexValued.of(String.format("<math><msup><mn>%d</mn><mfrac><mn>%d</mn><mn>%d</mn></mfrac></msup></math>", Utils.pow(rb, rootBottom), rootTop, rootBottom), new Complex(Utils.pow(rb, rootTop)), 1.9);
	}
	
}
