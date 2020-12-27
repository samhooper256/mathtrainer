package suppliers.matrices;

import static problems.Prettifier.*;
import static problems.Problem.*;
import static suppliers.NamedIntRange.*;

import math.*;
import problems.*;
import suppliers.*;

/**
 * @author Sam Hooper
 *
 */
public class MatrixDeterminantSupplier extends SettingsProblemSupplier {
	
	private static final RangeStore VALUES = RangeStore.of(-100, 100, -20, 20);
	
	private final NamedIntRange values = of(VALUES, "Values of matrix elements");
	
	public MatrixDeterminantSupplier() {
		settings(values);
	}

	@Override
	public Problem get() {
		Matrix m = Matrix.from(2, 2, () -> BigFraction.of(intInclusive(values), 1));
		BigFraction det = m.determinant();
		if(Math.random() <= 0.5) { //ask them to find the determinant
			return MultiValued.of(ensureMath(det(m) + op('='))).addResult(det);
		}
		else { //ask them to find the missing value given the rest of the matrix and the determinant.
			int missingRow = intExclusive(2), missingCol = intExclusive(2);
			BigFraction missingValue = m.get(missingRow, missingCol);
			String[][] strs = m.mapTo(Prettifier::frac, String[]::new, String[][]::new);
			strs[missingRow][missingCol] = variable('k');
			return MultiValued.of(ensureMath(det(strs) + op('=') + frac(det))).addResult(missingValue);
		}
	}
	
}
