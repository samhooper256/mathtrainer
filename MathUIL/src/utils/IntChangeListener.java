package utils;

/**
 * @author Sam Hooper
 *
 */
@FunctionalInterface
public interface IntChangeListener {
	
	void changed(int oldValue, int newValue);
	
}
