package math;

import java.util.Arrays;

/**
 * A matrix of {@link BigFraction BigFractions}. Rows and columns are indexed starting from zero. {@code Matrix} object are immutable.
 * @author Sam Hooper
 *
 */
public class Matrix {
	
	/**
	 * Returns a new {@link Matrix} whose elements are those in {@code elements}. The given array is defensively copied. {@code elements.length}
	 * must be greater than {@code 0} and every row in {@code elements} must have the same length.
	 */
	public static Matrix from(BigFraction[][] elements) {
		ensureValidDimensions(elements);
		return new Matrix(copyOf(elements));
	}
	
	/**
	 * Ensures that {@code (elements.length > 0)} and every row in {@code elements} has the same length.
	 * @param elements
	 */
	private static void ensureValidDimensions(BigFraction[][] elements) {
		if(elements.length == 0)
			throw new IllegalArgumentException("elements.length == 0");
		int length = elements[0].length;
		for(BigFraction[] row : elements)
			if(row.length != length)
				throw new IllegalArgumentException("The elements array has rows of different lengths");
	}
	
	private static BigFraction[][] copyOf(BigFraction[][] elements) {
		BigFraction[][] result = new BigFraction[elements.length][];
		for(int i = 0; i < elements.length; i++) {
			BigFraction[] row = elements[i];
			result[i] = Arrays.copyOf(row, row.length);
		}
		return result;
	}
	
	private final BigFraction[][] elements;
	
	/**
	 * Does <b>not</b> defensively copy the array, nor does it {@link #ensureValidDimensions(BigFraction[][]) ensure its dimensions are valid}.
	 */
	private Matrix(final BigFraction[][] elements) {
		this.elements = elements;
	}
	
	public BigFraction get(final int row, final int col) {
		return elements[row][col];
	}
	
	/**
	 * Returns the row at index {@code row}. The elements in the returned array are ordered from least column to greatest column.
	 * Runs in O({@link #colCount()}) since the returned array is a copy of the internal one.
	 * The returned array has length {@code colCount()}.
	 */
	public BigFraction[] getRow(final int row) {
		return Arrays.copyOf(elements[row], elements[row].length);
	}
	
	/**
	 * Returns the column at index {@code col}. The elements in the returned array are ordered from least row to greatest row.
	 * Runs in O({@link #rowCount()}) since the returned array is a copy of the internal one. The returned array has length
	 * {@code rowCount()}.
	 */
	public BigFraction[] getCol(final int col) {
		BigFraction[] column = new BigFraction[rowCount()];
		for(int row = 0; row < rowCount(); row++)
			column[row] = elements[row][col];
		return column;
	}
	
	public int rowCount() {
		return elements.length;
	}
	
	public int colCount() {
		return elements[0].length;
	}
}
