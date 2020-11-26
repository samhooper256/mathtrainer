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
public class MultiplyingMatricesSupplier extends SettingsProblemSupplier {
	
	private static final RangeStore VALUES = RangeStore.of(-100, 100, -10, 10), ROWS = RangeStore.of(1, 3, 2, 3), COLS = RangeStore.of(1, 3, 2, 3);
	
	private final NamedIntRange values = of(VALUES, "Values of matrix elements"), rows = of(ROWS, "Rows in left matrix"), cols = of(COLS, "Columns in left matrix");
	
	public MultiplyingMatricesSupplier() {
		settings(values, rows, cols);
	}

	@Override
	public Problem get() {
		int r = intInclusive(rows), c = intInclusive(cols);
		System.out.printf("values=%s%n", values);
		Matrix m1 = Matrix.from(r, c, () -> BigFraction.of(intInclusive(values), 1)), m2 = Matrix.from(c, r, () -> BigFraction.of(intInclusive(values), 1)),
				resultMatrix = m1.multiply(m2);
		String[][] variableMatrix = MatSupUtils.variableMatrix(r, r);
		String displayExp = ensureMath(row(matrix(m1) + op('*') + matrix(m2) + op('=') + matrix(variableMatrix)));
		int varRow = intExclusive(r), varCol = intExclusive(r);
		String varName = variableMatrix[varRow][varCol];
		BigFraction varValue = resultMatrix.get(varRow, varCol);
		return MultiValued.of(String.format("%s. Find %s:", displayExp, ensureMath(variable(varName)))).setLines(Math.max(r, c)).addResult(varValue);
	}
	
}
