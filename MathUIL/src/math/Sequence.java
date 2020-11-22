package math;

import java.util.*;

/**
 * <p>A positive, finite length sequence of {@link Complex} numbers intended to represent a sequence in mathematics. {@code Sequences} are immutable;
 * the size of a {@code Sequence} and the values of its terms do not change. Sequences use 1-based indexing; the value at index <i>i</i> can be
 * retrieved by {@link #nthTerm(int) nthTerm}{@code (i)}.</p>
 * @author Sam Hooper
 *
 */
public interface Sequence extends Iterable<Complex> {
	
	/**
	 * Returns the nth term in this {@link Sequence}. It may be calculated on demand or retrieved from a pervious computation.
	 * Throws an exception of some kind if {@code n <= 0 || n > size()}. The value returned by {@code nthTerm(x)} is the value of this
	 * {@code Sequence} at index {@code x}.
	 */
	Complex nthTerm(int n);

	/** Return the number of terms in this sequence. {@link #nthTerm(int) nthTerm}{@code (size())} returns the last term in the sequence.
	 * The size of a {@link Sequence} will always be greater than {@code 0}.
	 */
	int size();
	
	/** Returns the sum of the numbers in this sequence from {@code startIndexInclusive} to {@code endIndexInclusive}.
	 * It may be calculated on demand or retrieved from a previous computation.
	 * If {@code startIndexInclusive} is greater than {@code endIndexInclusive}, returns {@code 0}.
	 * @param startIndexInclusive must be >= {@code 0}.
	 * @param endIndexInclusive must be <= {@link #size()}.
	 */
	default Complex sum(final int startIndexInclusive, final int endIndexInclusive) {
		if(startIndexInclusive <= 0 || endIndexInclusive > size())
			throw new IllegalArgumentException(String.format("Illegal arguments: startIndexInclusive=%d, endIndexInclusive=%d", startIndexInclusive, endIndexInclusive));
		Complex result = Complex.ZERO;
		for(int i = startIndexInclusive; i <= endIndexInclusive; i++)
			result = result.add(nthTerm(i));
		return result;
	}
	
	/** Returns the sum of the numbers in this sequence from {@code startIndexInclusive} to {@link #size()}.
	 * It may be calculated on demand or retrieved from a previous computation.
	 * @param startIndexInclusive must be >= {@code 0}.
	 */
	default Complex sum(final int startIndexInclusive) {
		return sum(startIndexInclusive, size());
	}
	
	/** Returns the sum of the numbers in this sequence.
	 * It may be calculated on demand or retrieved from a previous computation. */
	default Complex sum() {
		return sum(1, size());
	}
	
	/**
	 * Returns a {@code String} containing all the terms in the sequence, from lowest index to greatest, separated by ", ".
	 * @implNote {@link #toString()} may call this method in an implementation of {@link Sequence}.
	 */
	default String toFullString() {
		StringJoiner j = new StringJoiner(", ");
		for(Complex c : this)
			j.add(c.toString());
		return j.toString();
	}
	
	/**
	 * Returns a {@code String} containing the first {@code n} terms in this {@link Sequence}, separated by {@code delimiter}, followed by
	 * {@code (delimiter + "..." + delimiter)}, followed by the last term in this {@code Sequence}. If {@code (n >= size() - 1)}, then there
	 * are no ellipsis and the returned {@code String} consists of all the terms in this {@code Sequence}, separated by {@code delimiter}. Note
	 * that {@code n} may be larger than {@link #size()}.
	 * @throws IllegalArgumentException if {@code (n <= 0)}.
	 */
	default String toPartialString(int n, String delimiter) {
		StringJoiner j = new StringJoiner(delimiter);
		final int maxI = Math.min(n, size());
		for(int i = 1; i <= maxI; i++)
			j.add(nthTerm(i).toString());
		if(n >= size() - 1) {
			if(n == size() - 1)
				j.add(nthTerm(size()).toString());
			return j.toString();
		}	
		
		return j + delimiter + "..." + delimiter + nthTerm(size());
	}
	
	/**
	 * Returns an {@link Iterator} over the terms in this {@link Sequence}. The returned iterator does not support the {@link Iterator#remove()} operation as
	 * {@code Sequences} are immutable.
	 */
	@Override
	default Iterator<Complex> iterator() {
		return new Iterator<>() {
			int nextTerm = 1;
			@Override
			public boolean hasNext() {
				return nextTerm <= size();
			}

			@Override
			public Complex next() {
				Complex next = nthTerm(nextTerm);
				nextTerm++;
				return next;
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException("Cannot remove terms from a sequence");
			}
			
		};
	}
}
