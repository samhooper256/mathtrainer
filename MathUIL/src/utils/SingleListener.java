package utils;

/**
 * @author Sam Hooper
 *
 */
@FunctionalInterface
public interface SingleListener<T> {
	void listen(T item);
}
