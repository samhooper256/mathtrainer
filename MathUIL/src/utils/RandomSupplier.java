package utils;

import java.util.function.Supplier;

/**
 * A {@link Supplier} that wishes to explicitly declare itself as "random." Objects whose type is a subtype of {@code RandomSupplier} should
 * return "random" values from {@link #get()} but are not required to. This interfaces imposes no new contracts from those in {@code Supplier}.
 * @author Sam Hooper
 *
 */
public interface RandomSupplier<T> extends Supplier<T> {
	
	/**
	 * Returns a new {@link RandomSupplier} whose {@link #get()} method returns one of the objects in {@code objects} at random each time
	 * it is called.
	 */
	@SafeVarargs
	public static <T> RandomSupplier<T> of(T... objects) {
		return new RandomSupplier<>() {
			
			@Override
			public T get() {
				return objects[(int) (Math.random() * objects.length)];
			}
			
		};
	}
}
