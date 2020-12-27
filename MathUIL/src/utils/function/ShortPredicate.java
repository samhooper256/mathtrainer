package utils.function;

import java.util.function.Predicate;

/**
 * <p>A {@link Predicate} specialized for the primitive type {@code short}. The functional method is {@link #testShort(short)}.</p>
 * @author Sam Hooper
 *
 */
@FunctionalInterface
public interface ShortPredicate extends Predicate<Short> {
	
	public boolean testShort(short t);
	
	@Override
	default boolean test(Short t) {
		return testShort(t);
	}
	
}
