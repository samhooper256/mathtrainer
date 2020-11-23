package math;

import java.util.*;
import java.util.function.BinaryOperator;

/**
 * <p>A finite, positive length sequence of {@link Complex} numbers. The size of a {@code FiniteSequence} does not change.</p>
 * @author Sam Hooper
 *
 */
public interface FiniteComplexSequence extends SummableSequence<Complex> {
	
	public static final BinaryOperator<Complex> SUM_FUNCTION = Complex::add;
	
	/** Returns the sum of the numbers in this sequence from {@code startIndexInclusive} to {@link #size()}.
	 * It may be calculated on demand or retrieved from a previous computation.
	 * @param startIndexInclusive must be >= {@code 0}.
	 */
	@Override
	default Complex sum(final int startIndexInclusive) {
		return sum(startIndexInclusive, size());
	}
	
	/** Returns the sum of the numbers in this sequence. It may be calculated on demand or retrieved from a previous computation. */
	@Override
	default Complex sum() {
		return sum(1, size());
	}

	@Override
	default boolean isFinite() {
		return true;
	}

	@Override
	default BinaryOperator<Complex> sumFunction() {
		return SUM_FUNCTION;
	}
	
	
	
	
}
