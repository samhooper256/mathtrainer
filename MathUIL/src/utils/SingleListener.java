package utils;

/**
 * A {@link Listener} whose functional method can be called with some value. The meaning of that value and when the functional method
 * is called are specified by the user of this interface. This value may be, for example, the new value of a piece of data that has changed.
 * 
 * @author Sam Hooper
 *
 */
@FunctionalInterface
public interface SingleListener<T> extends Listener {
	
	void listen(T item);
	
}
