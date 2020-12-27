package utils.function;

import java.util.function.*;

/**
 * <p>A {@link Consumer} specialized for the primitive type {@code boolean}. The functional method is {@link #acceptBoolean(boolean)}.</p>
 * @author Sam Hooper
 *
 */
public interface BooleanConsumer extends Consumer<Boolean> {
	
	void acceptBoolean(boolean t);
	
	@Override
	default void accept(Boolean t) {
		acceptBoolean(t);
	}
	
}