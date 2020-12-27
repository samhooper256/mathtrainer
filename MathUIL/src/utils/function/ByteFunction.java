package utils.function;

import java.util.function.Function;

/**
 * <p>Represents a function that accepts a {@code byte}-valued argument and produces a result. The
 * functional method is {@link #applyByte(byte)}.</p>
 * @author Sam Hooper
 *
 */
public interface ByteFunction<T> extends Function<Byte, T> {
	
	T applyByte(byte value);

	@Override
	default T apply(Byte value) {
		return applyByte(value);
	}
	
}