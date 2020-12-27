package utils.function;

import java.util.function.Function;

/**
 * <p>A {@link Function} that produces a {@code char}-valued result. The functional method is {@link #applyAsChar(Object)}.</p>
 * @author Sam Hooper
 */
public interface ToCharFunction<T> extends Function<T, Character> {
	
	/**
	 * <p>A {@link ToCharFunction} that returns the only {@code char} in a {@code String}. the function throws an
	 * {@link IllegalArgumentException} if the passed {@code String} does not have a {@link String#length() length}
	 * of {@code 1}; otherwise, for a {@code String} {@code s}, it returns {@code s.charAt(0)}.</p>
	 * 
	 * <p>Note: The function verifies the length of the {@code String}, which is extra overhead that may not be desired.
	 * If one knows that all {@code Strings} will have only one {@code char} and thus does not need them to be verified,
	 * {@link #FIRST_CHAR_OF_STRING} may be a suitable alternative.</p>
	 * */
	ToCharFunction<String> ONLY_CHAR_OF_STRING = str -> {
		if(str.length() != 1)
			throw new IllegalArgumentException("The given String does not have a length of 1");
		return str.charAt(0);
	};
	
	/** 
	 * <p>A {@link ToCharFunction} equivalent to:<pre>{@code s -> s.charAt(0)}</pre> Note that this implies that
	 * any exception caused by {@link String#charAt(int)} (such as an {@link IndexOutOfBoundsException} for an
	 * {@link String#isEmpty() empty} {@code String}) will be relayed to the caller.</p>
	 * */
	ToCharFunction<String> FIRST_CHAR_OF_STRING = str -> str.charAt(0);
	
	char applyAsChar(T t);
	
	@Override
	default Character apply(T t) {
		return applyAsChar(t);
	}
	
}