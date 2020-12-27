package utils.function;

import java.util.function.*;

/**
 * <p>A {@link Consumer} specialized for the primitive type {@code char}. The functional method is {@link #acceptChar(char)}.</p>
 * @author Sam Hooper
 *
 */
public interface CharConsumer extends Consumer<Character> {
	
	void acceptChar(char t);
	
	@Override
	default void accept(Character t) {
		acceptChar(t);
	}
	
}
