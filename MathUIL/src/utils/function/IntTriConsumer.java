package utils.function;

/**
 * <p>A {@link TriConsumer} specialized for the primitive type {@code int}. The functional method is {@link #acceptInts(int, int, int)}.</p>
 * @author Sam Hooper
 *
 */
public interface IntTriConsumer extends TriConsumer<Integer, Integer, Integer>{

	void acceptInts(int a, int b, int c);
	
	@Override
	default void accept(Integer a, Integer b, Integer c) {
		acceptInts(a, b, c);
	}
	
}