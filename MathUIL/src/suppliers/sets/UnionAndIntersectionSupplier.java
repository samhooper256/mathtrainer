package suppliers.sets;

import static problems.Prettifier.*;
import static problems.Problem.*;
import static suppliers.NamedIntRange.*;

import java.util.List;

import math.Complex;
import problems.*;
import suppliers.*;
import utils.Sets;

/**
 * @author Sam Hooper
 *
 */
public class UnionAndIntersectionSupplier extends SettingsProblemSupplier {
	
	private static final RangeStore SIZE = RangeStore.of(0, 26, 0, 8);
	
	private final NamedIntRange size = of(SIZE, "Size of sets");
	
	public UnionAndIntersectionSupplier() {
		addAllSettings(size);
	}

	@Override
	public Problem get() {
		if(Math.random() <= 0.5)
			return literalProblem();
		else
			return wordProblem();
	}
	
	private Problem literalProblem() {
		List<Character> set1 = SetSupUtils.letterList(intInclusive(size)), set2 = SetSupUtils.letterList(intInclusive(size));
		if(Math.random() <= 0.5)
			return Builder.of(String.format("The number of distinct elements in %s is:", ensureMath(union(set(set1, Prettifier::variable), set(set2, Prettifier::variable)))))
					.addResult(new Complex(Sets.union(set1, set2).size())).build();
		else
			return Builder.of(String.format("The number of distinct elements in %s is:", ensureMath(intersection(set(set1, Prettifier::variable), set(set2, Prettifier::variable)))))
					.addResult(new Complex(Sets.intersection(set1, set2).size())).build();
	}
	
	private Problem wordProblem() {
		int a = intInclusive(size), b = intInclusive(size), intersection = intInclusive(0, Math.min(a, b)), union = intersection + (a - intersection) + (b - intersection);
		String uDisp = union(variable("A"), variable("B")), iDisp = intersection(variable("A"), variable("B"));
		if(Math.random() <= 0.5)
			return Builder.of(String.format("There %s %d element%s in set A, %d in set B, and %d in %s. %s has how many elements:",
					a == 1 ? "is" : "are", a, a == 1 ? "" : "s", b, intersection, iDisp, uDisp))
					.addResult(new Complex(union)).build();
		else
			return Builder.of(String.format("There %s %d element%s in set A, %d in set B, and %d in %s. %s has how many elements:",
					a == 1 ? "is" : "are", a, a == 1 ? "" : "s", b, union, uDisp, iDisp))
					.addResult(new Complex(intersection)).build();
	}
	
}
