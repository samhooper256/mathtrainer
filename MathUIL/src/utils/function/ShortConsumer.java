package utils.function;

import java.util.function.*;

/**
 * <p>A {@link Consumer} specialized for the primitive type {@code short}. The functional method is {@link #acceptShort(short)}.</p>
 * @author Sam Hooper
 *
 */
public interface ShortConsumer extends Consumer<Short> {
	
	void acceptShort(short t);
	
	@Override
	default void accept(Short t) {
		acceptShort(t);
	}
	
}
