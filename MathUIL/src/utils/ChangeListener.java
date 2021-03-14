package utils;

/**
 * 
 * @author Sam Hooper
 */
@FunctionalInterface
public interface ChangeListener<T> {
	
	void changed(T oldValue, T newValue);
	
}
