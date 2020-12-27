package utils.function;

import java.util.function.Supplier;

/**
 * <p>A {@link Supplier} specialized for the primitive type {@code short}. The functional method is {@link #getAsShort()}.</p>
 * @author Sam Hooper
 *
 */
public interface ShortSupplier extends Supplier<Short> {
	
	short getAsShort();
	
	@Override
	default Short get() {
		return getAsShort();
	}
	
}
