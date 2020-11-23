package math;

/**
 * @author Sam Hooper
 *
 */
public interface FiniteSequence<T> extends Sequence<T> {
	
	/** Return the number of terms in this sequence. {@link #nthTerm(int) nthTerm}{@code (size())} returns the last term in the sequence.
	 * The size of a {@link Sequence} will always be greater than {@code 0}.
	 */
	int size();
}
