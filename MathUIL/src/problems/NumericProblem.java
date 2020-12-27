package problems;

import java.math.*;
import java.util.*;
import java.util.function.*;
import java.util.regex.Pattern;

import math.*;
import suppliers.*;
import utils.*;
import utils.function.CharSupplier;
import utils.refs.IntRange;

/**
 * <p>A problem that the user can attempt to solve. Any {@link Problem} that will be displayed to the user must have a
 * {@link ProblemSupplier} that supplies it.</p>
 * 
 * <p><b>All {@code Problems} are immutable.</b></p>
 * 
 * @author Sam Hooper
 *
 */
public interface NumericProblem extends Problem {
	
	/**
	 * Returns {@code true} if this problem only requires an approximate answer (within 5 percent of the actual answer, inclusive), {@code false}
	 * otherwise. The default implementation simply returns {@code false}.
	 */
	default boolean isApproximateResult() {
		return false;
	}
	
	/** Returns the percentage of the actual answer that a given answer must be in order to be counted correct. The default implementation returns {@code 0.05}.
	 * If this {@link Problem} is not {@link #isApproximateResult() an approximate result}, then the behavior of this method is undefined. Note that, for
	 * example, {@code 0.05} means within 5%, not within 0.05%.
	 * <p>The default implementation is equivalent to:
	 * <pre><code>return approximationPercentAsBigDecimal().doubleValue()</pre></code>
	 * </p>
	 * @return
	 */
	default double approximationPercent() {
		return approximationPercentAsBigDecimal().doubleValue();
	}
	
	/**
	 * Returns the {@link #approximationPercent()} as a {@code String}. This method should be called in preference to {@link #approximationPercent()}
	 * when possible because it may give a more precise value than can be stored in a {@code double}. The default implementation throws an
	 * {@link UnsupportedOperationException}.
	 */
	default BigDecimal approximationPercentAsBigDecimal() {
		throw new UnsupportedOperationException();
	}
	
}
