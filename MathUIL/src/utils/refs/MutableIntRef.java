package utils.refs;

import java.util.ArrayList;

import utils.IntChangeListener;

/**
 * A reference to an {@code int}. The {@code int} can be changed. This class supports the use of {@link IntChangeListener}s to detect
 * changes in the {@code int}'s value and run code when a change occurs. The listeners are run in the order they are added.
 * @author Sam Hooper
 *
 */
public class MutableIntRef extends AbstractIntRef {
	
	private int value;
	
	/**
	 * Creates a new {@code IntRef} storing the given value.
	 */
	public MutableIntRef(int value) {
		this.value = value;
	}
	
	/**
	 * @return the {@code int} value currently stored by this {@code IntRef}.
	 */
	@Override
	public int get() {
		return value;
	}
	
	/**
	 * Does <b>not</b> trigger the {@link IntChangeListener}s if {@code newValue} is the same as {@code int} currently stored by this
	 * {@code IntRef}. Returns {@code false} if the new value is the same as from the existing one, {@code true} otherwise.
	 * @param newValue
	 */
	public boolean set(int newValue) {
		if(value == newValue)
			return false;
		int oldValue = this.value;
		this.value = newValue;
		runChangeListeners(newValue, oldValue);
		runChangeActions();
		return true;
	}
	
	/** Decrements the {@link #get() value} of this {@link MutableIntRef}. If the value is currently {@link Integer#MIN_VALUE}, it will become {@link Integer#MAX_VALUE}.
	 * Equivalent to {@code set(get() - 1)}*/
	public void decrement() {
		set(get() - 1);
	}

	@Override
	public String toString() {
		return String.format("IntRef[%d]", value);
	}
	
	
}
