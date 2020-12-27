package utils.function;

import java.util.function.Function;

/**
 * <p>A {@link Function} that produces a {@code byte}-valued result. The functional method is {@link #applyAsByte(Object)}.</p>
 * @author Sam Hooper
 */
public interface ToByteFunction<T> extends Function<T, Byte> {
	
	byte applyAsByte(T t);
	
	@Override
	default Byte apply(T t) {
		return applyAsByte(t);
	}
	
}