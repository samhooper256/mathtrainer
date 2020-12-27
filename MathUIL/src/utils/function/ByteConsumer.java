package utils.function;

import java.util.function.*;

/**
 * <p>A {@link Consumer} specialized for the primitive type {@code byte}. The functional method is {@link #acceptByte(byte)}.<p>
 * @author Sam Hooper
 *
 */
public interface ByteConsumer extends Consumer<Byte> {
	
	void acceptByte(byte t);
	
	@Override
	default void accept(Byte t) {
		acceptByte(t);
	}
	
}