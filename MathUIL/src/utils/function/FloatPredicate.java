package utils.function;

import java.util.function.Predicate;

/**
 * <p>A {@link Predicate} specialized for the primitive type {@code float}. The functional method is {@link #testFloat(float)}.</p>
 * @author Sam Hooper
 *
 */
@FunctionalInterface
public interface FloatPredicate extends Predicate<Float> {
	
	public boolean testFloat(float t);
	
	@Override
	default boolean test(Float t) {
		return testFloat(t);
	}
	
}
