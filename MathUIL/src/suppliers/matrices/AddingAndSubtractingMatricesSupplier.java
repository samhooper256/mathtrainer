package suppliers.matrices;

import static problems.Prettifier.*;
import static problems.Problem.*;
import static suppliers.NamedIntRange.*;

import java.util.Arrays;
import java.util.stream.Collectors;

import math.*;
import problems.*;
import suppliers.*;

/**
 * @author Sam Hooper
 *
 */
public class AddingAndSubtractingMatricesSupplier extends SettingsProblemSupplier {
	
	private static final RangeStore ROWS = RangeStore.of(1, 3, 2, 2), COLS = RangeStore.of(1, 3, 2, 2), VALUES = RangeStore.of(-1000, 1000, -50, 50);
	
	private final NamedIntRange rows = of(ROWS, "Rows in matrices"), cols = of(COLS, "Cols in matrices"), values = of(VALUES, "Values of matrix elements");
	
	public AddingAndSubtractingMatricesSupplier() {
		settings(values, rows, cols);
	}

	@Override
	public Problem get() {
		final int r = intInclusive(rows), c = intInclusive(cols);
		Matrix m1 = Matrix.from(r, c, () -> BigFraction.of(intInclusive(values), 1)), m2 = Matrix.from(r, c, () -> BigFraction.of(intInclusive(values), 1));
		String[][] letterMat = MatSupUtils.variableMatrix(r, c);
		final Matrix resultMatrix;
		StringBuilder displayBuilder = new StringBuilder(matrix(m1));
		if(Math.random() <= 0.5) {
			displayBuilder.append(op('+'));
			resultMatrix = m1.add(m2);
		}
		else {
			displayBuilder.append(op('-'));
			resultMatrix = m1.subtract(m2);
		}
		displayBuilder.append(matrix(m2)).append(op('=')).append(matrix(letterMat));
		String display = ensureMath(row(displayBuilder.toString()));
		if(Math.random() <= 0.5) { //only ask for a specific element from the result matrix
			int letterRow = intExclusive(r), letterCol = intExclusive(c);
			String letter = letterMat[letterRow][letterCol];
			BigFraction answer = resultMatrix.get(letterRow, letterCol);
			return MultiValued.of(String.format("%s. Find %s:", display, ensureMath(variable(letter)))).addResult(answer);
		}
		else { //ask for the sum of all elements in the result matrix
			String letterSum = ensureMath(Arrays.stream(letterMat).flatMap(Arrays::stream).map(Prettifier::variable).collect(Collectors.joining(op('+'))));
			BigFraction answer = resultMatrix.reduce(BigFraction::sum);
			return MultiValued.of(String.format("%s. Find %s:", display, letterSum)).addResult(answer);
			
		}
	}
	
	
}
