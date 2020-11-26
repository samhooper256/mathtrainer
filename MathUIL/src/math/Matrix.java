package math;

import java.util.*;
import java.util.function.*;

/**
 * A matrix of {@link BigFraction BigFractions}. Rows and columns are indexed starting from zero. {@code Matrix} object are immutable.
 * @author Sam Hooper
 *
 */
public class Matrix {
	
	public interface CellConsumer {
		void accept(final int row, final int col);
	}
	
	public interface CellMapper {
		BigFraction apply(final int row, final int col);
	}
	
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
	
	/**
	 * Returns {@code true} if {@code a} and {@code b} have the same dimensions, {@code false} otherwise.
	 */
	public static boolean sameDimensions(Matrix a, Matrix b) {
		return a.rowCount() == b.rowCount() && a.colCount() == b.colCount();
	}
	
	private final BigFraction[][] elements;
	
	/**
	 * Does <b>not</b> defensively copy the array, nor does it {@link #ensureValidDimensions(BigFraction[][]) ensure its dimensions are valid}.
	 */
	private Matrix(final BigFraction[][] elements) {
		this.elements = elements;
	}
	
	public void forEach(final Consumer<BigFraction> consumer) {
		Objects.requireNonNull(consumer);
		forEachCell((r, c) -> consumer.accept(get(r, c)));
	}
	
	public void forEachCell(final CellConsumer consumer) {
		Objects.requireNonNull(consumer);
		for(int row = 0; row < rowCount(); row++)
			for(int col = 0; col < colCount(); col++)
				consumer.accept(row, col);
	}
	
	public Matrix map(UnaryOperator<BigFraction> mapper) {
		return map((r, c) -> mapper.apply(get(r, c)));
	}
	
	public Matrix map(CellMapper mapper) {
		BigFraction[][] newElements = new BigFraction[rowCount()][colCount()];
		forEachCell((r, c) -> newElements[r][c] = mapper.apply(r, c));
		return new Matrix(newElements);
	}
	
	/**
	 * Returns a {@link Matrix} where every element is the {@link BigFraction#negate() negation} of the corresponding element in {@code this}
	 * {@code Matrix}.
	 */
	public Matrix negate() {
		return map(BigFraction::negate);
	}
	
	/**
	 * Returns {@code (this + val)}. Throws an exception if adding the two matrices is impossible.
	 * @throws NullPointerException if {@code val} is {@code null}.
	 * @throws ArithmeticException if {@code this} and {@code val} do not have the {@link #sameDimensions(Matrix, Matrix) same dimensions}.
	 */
	public Matrix add(Matrix val) {
		Objects.requireNonNull(val);
		if(!sameDimensions(this, val))
			throw new ArithmeticException("this and val have different dimensions");
		return map((r, c) -> get(r, c).add(val.get(r, c)));
	}
	
	/**
	 *Returns {@code (this - val)}. Throws an exception if subtracting the two matrices is impossible.
	 * @throws NullPointerException if {@code val} is {@code null}.
	 * @throws ArithmeticException if {@code this} and {@code val} do not have the {@link #sameDimensions(Matrix, Matrix) same dimensions}.
	 */
	public Matrix subtract(Matrix val) {
		Objects.requireNonNull(val);
		if(!sameDimensions(this, val))
			throw new ArithmeticException("this and val have different dimensions");
		return map((r, c) -> get(r, c).subtract(val.get(r, c)));
	}
	
	/**
	 * Returns {@code (this * val)}. Throws an exception if multiplying the two matrices is impossible.
	 * @throws NullPointerException if {@code val} is {@code null}.
	 * @throws ArithmeticException if {@code this} and {@code val} do not have compatible dimensions.
	 */
	public Matrix multiply(Matrix val) {
		Objects.requireNonNull(val);
		if(rowCount() != val.colCount() || colCount() != val.rowCount())
			throw new ArithmeticException("this and val do not have compatible dimensions");
		return map((r, c) -> {
			BigFraction total = BigFraction.ZERO;
			for(int i = 0; i < colCount(); i++)
				total = total.add(get(r, i).multiply(val.get(i, c)));
			return total;
		});
	}
	
	/**
	 * Returns the determinant of this {@link Matrix} as a {@link BigFraction}. Determinants are not supported for
	 * matrices larger than 2x2.
	 * @throws ArithmeticException if {@code this} {@link Matrix} is not {@link #isSquare() square}.
	 * @throws UnsupportedOperationException if this {@code Matrix} is square but is larger than 2x2.
	 */
	public BigFraction determinant() {
		if(!isSquare())
			throw new ArithmeticException("Cannot find the determinant of a non-square matrix");
		if(rowCount() > 2)
			throw new UnsupportedOperationException("Calculating the determinant of a " + rowCount() + "x" + colCount() + " matrix is not supported");
		if(rowCount() == 1)
			return get(0, 0);
		return get(0, 0).multiply(get(1, 1)).subtract(get(0, 1).multiply(get(1, 0)));
	}
	
	public BigFraction sumOfRow(final int row) {
		BigFraction total = BigFraction.ZERO;
		for(BigFraction f : elements[row])
			total = total.add(f);
		return total;
	}
	
	public BigFraction sumOfCol(final int col) {
		BigFraction total = BigFraction.ZERO;
		for(int row = 0; row < rowCount(); row++)
			total = total.add(elements[row][col]);
		return total;
	}
	
	/**
	 * Returns the element at row {@code row} and column {@code col} in this {@link Matrix}.
	 */
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
	
	/**
	 * Returns the number of rows in this {@link Matrix}. The returned value will always be greater than {@code 0}.
	 */
	public int rowCount() {
		return elements.length;
	}
	
	/**
	 * Returns the number of columns in this {@link Matrix}. The returned value will always be greater than {@code 0}.
	 */
	public int colCount() {
		return elements[0].length;
	}
	
	/**
	 * Returns {@code true} if {@code this} is a square {@link Matrix} (a matrix with the same number of rows as columns), {@code false}
	 * otherwise.
	 */
	public boolean isSquare() {
		return rowCount() == colCount();
	}

	@Override
	public String toString() {
		return Arrays.deepToString(elements);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.deepHashCode(elements);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(getClass() != obj.getClass())
			return false;
		Matrix other = (Matrix) obj;
		return Arrays.deepEquals(elements, other.elements);
	}
	
}
