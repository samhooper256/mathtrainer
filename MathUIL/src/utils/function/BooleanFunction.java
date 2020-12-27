package utils.function;

import java.util.function.Function;

/**
 * <p>Represents a function that accepts a {@code boolean}-valued argument and produces a result. The
 * functional method is {@link #applyBoolean(boolean)}.</p>
 * @author Sam Hooper
 *
 */
public interface BooleanFunction<T> extends Function<Boolean, T> {
	
	T applyBoolean(boolean value);

	@Override
	default T apply(Boolean value) {
		return applyBoolean(value);
	}
	
	/**
	 * <p>Returns a {@link BooleanFunction} that returns {@code resultIfTrue} if the argument is {@code true} and {@code resultIfFalse}
	 * if the argument is {@code false}.</p>
	 */
	public static <T> BooleanFunction<T> either(T resultIfTrue, T resultIfFalse) {
		return b -> b ? resultIfTrue : resultIfFalse;
	}
	
}