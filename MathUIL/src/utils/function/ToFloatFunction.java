package utils.function;

import java.util.function.Function;

/**
 * <p>A {@link Function} that produces a {@code float}-valued result. The functional method is {@link #applyAsFloat(Object)}.</p>
 * @author Sam Hooper
 */
public interface ToFloatFunction<T> extends Function<T, Float> {
	
	float applyAsFloat(T t);
	
	@Override
	default Float apply(T t) {
		return applyAsFloat(t);
	}
	
}