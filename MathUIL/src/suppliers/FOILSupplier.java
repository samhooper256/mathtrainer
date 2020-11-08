package suppliers;

import problems.*;

/**
 * @author Sam Hooper
 *
 */
public class FOILSupplier implements ProblemSupplier {
	
	public FOILSupplier() {}

	@Override
	public Problem get() {
		return SimpleExpression.multiplyTerms(Problem.intInclusive(0, 99), Problem.intInclusive(0, 99));
	}
	
	
}
