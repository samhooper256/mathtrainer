package suppliers.matrices;

import problems.Prettifier;
import suppliers.ProblemSupplier;

/**
 * Matrix-related {@link ProblemSupplier} utilities.
 * @author Sam Hooper
 *
 */
public final class MatSupUtils {

	private MatSupUtils() {}
	
	public static String[][] variableMatrix(final int rows, final int cols) {
		if(rows * cols > 26)
			throw new IllegalArgumentException("There are not enough letters in the alphabet to make a letter matrix of the given size: " + rows + "x" + cols);
		String[][] mat = new String[rows][cols];
		char letter = 'a';
		for(int r = 0; r < rows; r++)
			for(int c = 0; c < cols; c++)
				mat[r][c] = Prettifier.variable(letter++);
		return mat;
	}
}
