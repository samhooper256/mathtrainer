package utils.function;

import java.util.function.Predicate;
/**
 * <p>A {@link Predicate} specialized for the primitive type {@code boolean}. The functional method is {@link #testBoolean(boolean)}.<p>
 * @author Sam Hooper
 *
 */
@FunctionalInterface
public interface BooleanPredicate extends Predicate<Boolean> {
	
	BooleanPredicate IS_TRUE = b -> b;
	BooleanPredicate IS_FALSE = b -> !b;
	
	boolean testBoolean(boolean b);

	@Override
	default boolean test(Boolean b) {
		return testBoolean(b);
	}
	
}