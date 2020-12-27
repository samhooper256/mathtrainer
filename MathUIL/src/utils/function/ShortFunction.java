package utils.function;

import java.util.function.Function;

/**
 * <p>Represents a function that accepts a {@code short}-valued argument and produces a result. The
 * functional method is {@link #applyShort(short)}.</p>
 * @author Sam Hooper
 *
 */
public interface ShortFunction<T> extends Function<Short, T> {
	
	T applyShort(short value);

	@Override
	default T apply(Short value) {
		return applyShort(value);
	}
	
}