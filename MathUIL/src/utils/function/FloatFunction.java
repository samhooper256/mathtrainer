package utils.function;

import java.util.function.Function;

/**
 * <p>Represents a function that accepts a {@code float}-valued argument and produces a result. The
 * functional method is {@link #applyFloat(float)}.</p>
 * @author Sam Hooper
 *
 */
public interface FloatFunction<T> extends Function<Float, T> {
	
	T applyFloat(float value);

	@Override
	default T apply(Float value) {
		return applyFloat(value);
	}
	
}