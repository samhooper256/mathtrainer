package math;

import java.util.function.*;

/**
 * @author Sam Hooper
 *
 */
public interface SummableSequence<T> extends Sequence<T> {
	/**
	 * Returns the sum all the terms in this {@link SummableSequence}. If this {@link Sequence} {@link #isFinite() is finite},
	 * this method is equivalent to:
	 * <pre><code>
	 * T total = nthTerm(1);
	 * for(int i = 2; i <= size(); i++)
	 * 	total = sumFunction().apply(total, nthTerm(i));
	 * return total;
	 * </code></pre>
	 * May throw an exception if the sum does not exist.
	 */
	T sum();
	
	/**
	 * Returns the sum of the terms in this sequence between {@code startInclusive} and {@code endInclusive}, according to
	 * the {@link #sumFunction()}. If
	 * {@code (startInclusive == endInclusive)}, returns {@link #nthTerm(int) nthTerm}{@code (startInclusive)}.
	 * @throws IllegalArgumentException if {@code (startInclusive > endInclusive || (isFinite() && endInclusive > size()) || startInclusive < 1)}.
	 */
	default T sum(int startInclusive, int endInclusive) {
		if(startInclusive > endInclusive || (isFinite() && endInclusive > size()) || startInclusive < 1)
			throw new IllegalArgumentException(String.format("Invalid arguments: startInclusive=%d, endInclusive=%d", startInclusive, endInclusive));
		T total = nthTerm(startInclusive);
		for(int i = startInclusive + 1; i <= endInclusive; i++)
			total = sumFunction().apply(total, nthTerm(i));
		return total;
			
	}
	
	/**
	 * Returns the sum of the terms in this sequence after (and including) the term at {@code startInclusive}. If
	 * {@code startInclusive < 1)}, throws an exception. May throw an exception if the sum does not exist.
	 * @throws IllegalArgumentException if {@code startInclusive < 1)}.
	 */
	T sum(int startInclusive);
	
	/**
	 * Returns the sum function for this {@link SummableSequence} sequence. This function is an associative binary operator.
	 * All methods that sum elements from this {@link Sequence} act as if they use this {@link BinaryOperator} to compute the sum.
	 */
	BinaryOperator<T> sumFunction();
}
