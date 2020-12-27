package utils.function;

import java.util.function.Function;

/**
 * <p>Represents a function that accepts a {@code char}-valued argument and produces a result. The
 * functional method is {@link #applyChar(char)}.</p>
 * @author Sam Hooper
 *
 */
public interface CharFunction<T> extends Function<Character, T> {
	
	T applyChar(char value);

	@Override
	default T apply(Character value) {
		return applyChar(value);
	}
	
}