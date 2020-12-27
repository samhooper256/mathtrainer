package utils.function;

import java.util.function.Function;

/**
 * <p>A {@link Function} that produces a {@code short}-valued result. The functional method is {@link #applyAsShort(Object)}.</p>
 * @author Sam Hooper
 */
public interface ToShortFunction<T> extends Function<T, Short> {
	
	short applyAsShort(T t);
	
	@Override
	default Short apply(T t) {
		return applyAsShort(t);
	}
	
}