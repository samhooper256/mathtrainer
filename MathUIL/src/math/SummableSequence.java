package math;

/**
 * @author Sam Hooper
 *
 */
public interface SummableSequence<T> extends Sequence<T> {
	/**
	 * Returns the sum of this {@link SummableSequence}. May throw an exception if the sum does not exist at the discretion of implementations.
	 * @return
	 */
	T sum();
}
