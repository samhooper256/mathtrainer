package math;

/**
 * <p>A positive, non-zero length sequence of objects intended to represent a sequence in mathematics. {@code Sequences} are immutable;
 * the values of their terms do not change. Sequences use 1-based indexing; the value at index <i>i</i> can be
 * retrieved by {@link #nthTerm(int) nthTerm}{@code (i)}.</p>
 * @author Sam Hooper
 *
 */
public interface Sequence<T> extends Iterable<T> {
	
	/**
	 * Returns the nth term in this {@link Sequence}. It may be calculated on demand or retrieved from a pervious computation.
	 * Throws an exception of some kind if {@code n <= 0 || n > size()}. The value returned by {@code nthTerm(x)} is the value of this
	 * {@code Sequence} at index {@code x}.
	 */
	T nthTerm(int n);
}
