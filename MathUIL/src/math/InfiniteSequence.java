package math;

import java.util.Iterator;

/**
 * @author Sam Hooper
 *
 */
public interface InfiniteSequence<T> extends Sequence<T> {
	/**
	 * Returns an {@link Iterator} over the terms in this {@link InfiniteSequence}. The returned iterator does not support the {@link Iterator#remove()} operation as
	 * {@code InfiniteSequences} are immutable. {@link Iterator#hasNext()} always returns {@code true}.
	 */
	@Override
	default Iterator<T> iterator() {
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

			@Override
			public void remove() {
				throw new UnsupportedOperationException("Cannot remove terms from a sequence");
			}
			
		};
	}
}
