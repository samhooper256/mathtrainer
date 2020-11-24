package math;

import java.util.*;

/**
 * <p>A positive, non-zero, possibly infinite length sequence of objects intended to represent a sequence in mathematics. {@code Sequences} are immutable;
 * the values of their terms do not change. Sequences use 1-based indexing; the value at index <i>i</i> can be
 * retrieved by {@link #nthTerm(int) nthTerm}{@code (i)}.</p>
 * @author Sam Hooper
 *
 */
public interface Sequence<T> extends Iterable<T> {
	
	/**
	 * Returns the <i>n</i>th term in this {@link Sequence}. It may be calculated on demand or retrieved from a pervious computation.
	 * Throws an exception of some kind if {@code (n < 1 || n > size())}. The value returned by {@code nthTerm(x)} is the value of this
	 * {@code Sequence} at index {@code x}.
	 */
	T nthTerm(int n);
	
	/**
	 * Returns the size of (that is, the number of terms in) this {@link Sequence} if it {@link #isFinite() is finite}; otherwise,
	 * returns a negative (and non-zero) number.
	 */
	int size();
	
	/**
	 * Returns the size of (that is, the number of terms in) this {@link Sequence} if it {@link #isFinite() is finite}; otherwise,
	 * throws an exception.
	 * @throws IllegalStateException if this {@link Sequence} is not {@link #isFinite()}.
	 */
	default int finiteSize() {
		final int size = size();
		if(size < 0)
			throw new IllegalStateException("This is Sequence does not have a finite size");
		return size;
	}
	
	/** Returns {@code true} if this {@link Sequence} has a finite number of terms, {@code false} otherwise. */
	default boolean isFinite() {
		return size() > 0;
	}
	
	/**
	 * Returns a {@code String} containing all the terms in the sequence, from lowest index to greatest, separated by {@code ", "}. If this
	 * sequence {@link #isFinite() is infinite}, returns a {@code String} indicating that.
	 */
	default String toFullString() {
		if(!isFinite())
			return "INFINITE_SEQUENCE@" + hashCode();
		StringJoiner j = new StringJoiner(", ");
		for(T t : this)
			j.add(t.toString());
		return j.toString();
	}
	
	/**
	 * Equivalent to {@code toPartialString(n, ", ")}.
	 */
	default String toPartialString(int n) {
		return toPartialString(n, ", ");
	}
	
	/**
	 * Returns a {@code String} containing the first {@code n} terms in this {@link Sequence}, separated by {@code delimiter}, followed by
	 * {@code (delimiter + "..." + delimiter + nthTerm(size())} if this {@code Sequence} {@link #isFinite() is finite}, {@code (delimiter + "...")} if
	 * this {@code Sequence} is infinite. If this {@code Sequence} is finite and {@code (n >= size() - 1)}, then there
	 * are no ellipsis and the returned {@code String} consists of all the terms in this {@code Sequence}, separated by {@code delimiter}. Note
	 * that {@code n} may be larger than {@link #size()}.
	 * @throws IllegalArgumentException if {@code (n <= 0)}.
	 */
	default String toPartialString(int n, String delimiter) {
		return toPartialString(n, 0, delimiter);
	}
	
	default String toPartialString(int startTerms, int endTerms, String delimiter) {
		if(endTerms < 0)
			throw new IllegalArgumentException("startTerms < 0");
		if(startTerms < 0)
			throw new IllegalArgumentException("endTerms < 0");
		if(endTerms > 0 && !isFinite())
			throw new IllegalArgumentException("Cannot get any end terms from an infinite sequence");
		if(isFinite()) {
			if(startTerms + endTerms >= size()) {
				StringJoiner j = new StringJoiner(delimiter);
				for(int i = 1; i <= size(); i++)
					j.add(nthTerm(i).toString());
				return j.toString();
			}
			else {
				StringJoiner j1 = new StringJoiner(delimiter);
				int maxI = Math.min(startTerms, size());
				for(int i = 1; i <= maxI; i++)
					j1.add(nthTerm(i).toString());
				StringJoiner j2 = new StringJoiner(delimiter);
				for(int k = Math.max(0, size() - endTerms + 1); k <= size(); k++)
					j2.add(nthTerm(k).toString());
				return j1 + (startTerms > 0 ? delimiter : "") + "..." + (endTerms > 0 ? delimiter + j2 : "");
			}
			
			
		}
		else {
			StringJoiner j = new StringJoiner(delimiter);
			for(int i = 1; i <= startTerms; i++)
				j.add(nthTerm(i).toString());
			return j + delimiter + "...";
		}
	}
	
	@Override
	default Iterator<T> iterator() {
		return isFinite() ? finiteIterator() : infiniteIterator();
	}

	private Iterator<T> finiteIterator() {
		return new Iterator<>() {
			int nextTerm = 1;
			@Override
			public boolean hasNext() {
				return nextTerm <= size();
			}

			@Override
			public T next() {
				T next = nthTerm(nextTerm);
				nextTerm++;
				return next;
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException("Cannot remove terms from a sequence");
			}
			
		};
	}
	
	private Iterator<T> infiniteIterator() {
		return new Iterator<>() {
			int nextTerm = 1;
			@Override
			public boolean hasNext() {
				return true;
			}

			@Override
			public T next() {
				T next = nthTerm(nextTerm);
				nextTerm++;
				return next;
			}
			
		};
	}
	
	default Sequence<T> subSequence(final int startInclusive, final int endInclusive) {
		throw new UnsupportedOperationException(); //TODO
	}
	
}
