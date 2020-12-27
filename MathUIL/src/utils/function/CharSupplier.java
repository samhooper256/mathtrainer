package utils.function;

import java.util.function.Supplier;

/**
 * <p>A {@link Supplier} specialized for the primitive type {@code char}. The functional method is {@link #getAsChar()}.</p>
 * @author Sam Hooper
 *
 */
public interface CharSupplier extends Supplier<Character> {
	
	char getAsChar();
	
	@Override
	default Character get() {
		return getAsChar();
	}
	
}
