package utils;

/**
 * @author Sam Hooper
 *
 */
@FunctionalInterface
public interface BooleanChangeListener {
	void changed(boolean oldValue, boolean newValue);
}
