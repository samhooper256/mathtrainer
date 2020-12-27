package utils.function;

import java.util.function.Predicate;

/**
 * <p>A {@link Predicate} specialized for the primitive type {@code byte}. The functional method is {@link #testByte(byte)}.</p>
 * @author Sam Hooper
 *
 */
@FunctionalInterface
public interface BytePredicate extends Predicate<Byte> {
	
	public boolean testByte(byte t);
	
	@Override
	default boolean test(Byte t) {
		return testByte(t);
	}
	
}
