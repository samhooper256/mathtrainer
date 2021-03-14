package utils.refs;

import java.util.ArrayList;

import utils.IntChangeListener;

/**
 * A reference to an {@code int}. The {@code int} can be changed. This class supports the use of {@link IntChangeListener}s to detect
 * changes in the {@code int}'s value and run code when a change occurs. The listeners are run in the order they are added.
 * @author Sam Hooper
 *
 */
public class IntRef extends AbstractRef {
	private int value;
	
	private ArrayList<IntChangeListener> changeListeners; //only constructed when a listener is actually added.
	
	/**
	 * Creates a new {@code IntRef} storing the given value.
	 */
	public IntRef(int value) {
		this.value = value;
	}
	
	/**
	 * @return the {@code int} value currently stored by this {@code IntRef}.
	 */
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

	private void runChangeListeners(int newValue, int oldValue) {
		if(changeListeners != null)
			for(IntChangeListener listener : changeListeners)
				listener.changed(oldValue, newValue);
	}
	
	/**
	 * Adds the given {@link IntChangeListener} to this {@code IntRef}'s list of {@code IntChangeListener}s.
	 * @param listener
	 */
	public void addChangeListener(IntChangeListener listener) {
		if(changeListeners == null)
			changeListeners = new ArrayList<>();
		changeListeners.add(listener);
	}
	
	/**
	 * Removes the given {@link IntChangeListener} from this {@code IntRef}'s list of {@code IntChangeListener}s.
	 * This is an O(n) operation, where n is the number of listeners on this {@code IntRef}.
	 * @param listener
	 * @return {@code true} if the listener was present and has been removed, {@code false} if the listener was not present.
	 */
	public boolean removeChangeListener(IntChangeListener listener) {
		return changeListeners != null && changeListeners.remove(listener);
	}

	@Override
	public String toString() {
		return String.format("IntRef[%d]", value);
	}
	
	
}
