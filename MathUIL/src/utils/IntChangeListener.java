package utils;

/**
 * @author Sam Hooper
 *
 */
@FunctionalInterface
public interface IntChangeListener extends Listener {
	
	void changed(int oldValue, int newValue);
	
}
