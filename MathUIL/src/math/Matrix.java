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
	
	public interface CellMapper<T> {
		T apply(final int row, final int col);
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
	 * Returns a new {@link Matrix} of {@code rowCount} rows and {@code colCount} columns where each element is supplied by the given {@link Supplier}. There
	 * is no guarantee as to which spots in the {@code Matrix} are filled first. {@code rowCount} and {@code colCount} must be strictly greater than {@code 0}.
	 */
	public static Matrix from(final int rowCount, final int colCount, final Supplier<BigFraction> elementSupplier) {
		Objects.requireNonNull(elementSupplier);
		ensureValidDimensions(rowCount, colCount);
		BigFraction[][] elems = new BigFraction[rowCount][colCount];
		for(int row = 0; row < rowCount; row++)
			for(int col = 0; col < colCount; col++)
				elems[row][col] = elementSupplier.get();
		return new Matrix(elems);
	}
	

	/**
	 * Returns a new {@link Matrix} of {@code rowCount} rows and {@code colCount} columns where each element is supplied by the given {@link CellMapper}. There
	 * is no guarantee as to which spots in the {@code Matrix} are filled first. {@code rowCount} and {@code colCount} must be strictly greater than {@code 0}.
	 */
	public static Matrix from(final int rowCount, final int colCount, final CellMapper<BigFraction> elementSupplier) {
		Objects.requireNonNull(elementSupplier);
		ensureValidDimensions(rowCount, colCount);
		BigFraction[][] elems = new BigFraction[rowCount][colCount];
		for(int row = 0; row < rowCount; row++)
			for(int col = 0; col < colCount; col++)
				elems[row][col] = elementSupplier.apply(row, col);
		return new Matrix(elems);
	}
	/**
	 * Ensures that {@code (elements.length > 0)} and every row in {@code elements} has the same length.
	 * @param elements
	 */
	private static void ensureValidDimensions(final BigFraction[][] elements) {
		if(elements.length == 0)
			throw new IllegalArgumentException("elements.length == 0");
		int length = elements[0].length;
		for(BigFraction[] row : elements)
			if(row.length != length)
				throw new IllegalArgumentException("The elements array has rows of different lengths");
	}
	
	/**
	 * Ensures that {@code (rows > 0)} and {@code (cos > 0)}.
	 * @param elements
	 */
	private static void ensureValidDimensions(final int rows, final int cols) {
		if(rows <= 0)
			throw new IllegalArgumentException("rows must be greater than 0. Was: " + rows);
		if(cols <= 0)
			throw new IllegalArgumentException("cols must be greater than 0. Was: " + cols);
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
	
	public Matrix map(CellMapper<BigFraction> mapper) {
		BigFraction[][] newElements = new BigFraction[rowCount()][colCount()];
		forEachCell((r, c) -> newElements[r][c] = mapper.apply(r, c));
		return new Matrix(newElements);
	}
	
	/**
	 * Maps this {@link Matrix} to a 2D array of some type, where each element in the returned array is computed from the corresponding {@link BigFraction}
	 * in this {@code Matrix} by the given {@link Function}.
	 */
	public <T> T[][] mapTo(Function<BigFraction, T> mapper, IntFunction<T[]> rowFactory, IntFunction<T[][]> arrayFactory) {
		T[][] result = arrayFactory.apply(rowCount());
		for(int i = 0; i < result.length; i++)
			result[i] = rowFactory.apply(colCount());
		forEachCell((r, c) -> result[r][c] = mapper.apply(get(r, c)));
		return result;
	}
	
	/**
	 * Reduces this {@link Matrix} into a single {@link BigFraction} using the specified {@link BinaryOperator}, which is assumed to be associative and commutative.
	 * If this {@code Matrix} only have one {@link #elementCount() element}, returns that element.
	 */
	public BigFraction reduce(BinaryOperator<BigFraction> op) {
		BigFraction result = get(0, 0);
		for(int c = 1; c < colCount(); c++) //start at (0, 1) and move right so that we don't accidentally count (0, 0) twice.
			result = op.apply(result, get(0, c));
		for(int r = 1; r < rowCount(); r++) //then continue from row 1 (the second row) at (1, 0).
			for(int c = 0; c < colCount(); c++)
				result = op.apply(result, get(r, c));
		return result;
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
//		System.out.printf("Multiplying %s and %s%n",this, val);
		return from(rowCount(), rowCount(), (r, c) -> {
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
	 * Returns an array of all the {@link #getRow(int) rows} in this {@link Matrix}. This method runs in O(r*c) where r is the {@link #rowCount() number of rows}
	 * and c is the {@link #colCount() number of columns} in this {@code Matrix}. Modifying the returned array in any way will not affect this {@code Matrix}.
	 */
	public BigFraction[][] getRows() {
		return copyOf(elements);
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
	
	public int elementCount() {
		return rowCount() * colCount();
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
