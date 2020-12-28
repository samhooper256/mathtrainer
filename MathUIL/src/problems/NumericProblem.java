package problems;

import java.math.*;

import math.*;

/**
 * <p>A {@link Problem} with a <i>numeric</i> result, types of which include (but are not limited to): {@code double}, {@link BigDecimal} {@link Complex},
 * {@link MixedNumber}, and {@link BigFraction}.</p>
 * 
 * <p>Note that, even though {@code NumericProblems} have numeric results, they must still report their {@link #answerAsString() correct answer} a {@code String}.</p>
 * @author Sam Hooper
 *
 */
public interface NumericProblem extends Problem {
	
	/**
	 * Returns {@code true} if this problem requires an approximate answer (within the {@link #approximationPercent() approximation percent}
	 * of the actual answer, inclusive), {@code false} otherwise. The default implementation simply returns {@code false}.
	 */
	default boolean isApproximateResult() {
		return false;
	}
	
	/**
	 * If this {@link NumericProblem} requires an {@link #isApproximateResult() approximate result}, the percentage of the actual answer that any
	 * given answer must be within is returned as a {@link BigDecimal} between {@code 0} and {@code 1} (inclusive). Otherwise, all behavior is undefined.
	 */
	BigDecimal approximationPercent();
	
}
