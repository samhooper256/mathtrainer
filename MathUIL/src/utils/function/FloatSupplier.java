package utils.function;

import java.util.function.Supplier;

/**
 * <p>A {@link Supplier} specialized for the primitive type {@code float}. The functional method is {@link #getAsFloat()}.</p>
 * @author Sam Hooper
 *
 */
public interface FloatSupplier extends Supplier<Float> {
	
	float getAsFloat();
	
	@Override
	default Float get() {
		return getAsFloat();
	}
	
}
