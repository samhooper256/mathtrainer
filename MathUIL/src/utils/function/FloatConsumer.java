package utils.function;

import java.util.function.*;

/**
 * <p>A {@link Consumer} specialized for the primitive type {@code float}. The functional method is {@link #acceptFloat(float)}.</p>
 * @author Sam Hooper
 *
 */
public interface FloatConsumer extends Consumer<Float> {
	
	void acceptFloat(float t);
	
	@Override
	default void accept(Float t) {
		acceptFloat(t);
	}
	
}
