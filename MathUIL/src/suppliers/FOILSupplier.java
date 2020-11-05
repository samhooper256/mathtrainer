package suppliers;

import problems.IntegerMultiplication;
import problems.Problem;

/**
 * @author Sam Hooper
 *
 */
public class FOILSupplier implements ProblemSupplier {
	
	public FOILSupplier() {}

	@Override
	public Problem get() {
		return IntegerMultiplication.fromTerms(Problem.intInclusive(0, 99), Problem.intInclusive(0, 99));
	}
	
	
}
