package utils.function;

import java.util.function.BiConsumer;

/**
 * <p>A {@link BiConsumer} specialized to accept two {@code ints}. The functional method is {@link #acceptInts(int, int)}.</p>
 * @author Sam Hooper
 *
 */
@FunctionalInterface
public interface IntBiConsumer extends BiConsumer<Integer, Integer>{
	
	void acceptInts(int a, int b);

	@Override
	default void accept(Integer a, Integer b) {
		acceptInts(a, b);
	}
	
}
