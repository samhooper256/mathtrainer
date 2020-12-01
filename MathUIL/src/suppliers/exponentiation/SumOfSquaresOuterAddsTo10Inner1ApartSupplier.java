package suppliers.exponentiation;

import problems.*;
import suppliers.*;

/**
 * @author Sam Hooper
 *
 */
@Named("Sum of squares when outer digits add to 10 and inner digits are 1 apart")
public class SumOfSquaresOuterAddsTo10Inner1ApartSupplier implements ProblemSupplier {
	
	public SumOfSquaresOuterAddsTo10Inner1ApartSupplier() {}
	
	@Override
	public Problem get() {
		int outer1 = Problem.intInclusive(1, 9);
		int outer2 = 10 - outer1;
		int inner1 = Problem.intInclusive(1, 9);
		int inner2 = inner1 - 1;
		final int[] arr = Problem.shuffled(outer1 * 10 + inner1, inner2 * 10 + outer2);
		return new SimpleExpression(String.format("%d^2+%d^2", arr[0], arr[1]));
	}
	
}
