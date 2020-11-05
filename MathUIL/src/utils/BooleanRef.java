package utils;

import java.util.*;

/**
 * <p>A reference to a {@code boolean}.</p>
 * 
 * <p>A {@link BooleanChangeListener} listening to this {@code BooleanRef} is permitted to {@link #removeChangeListener(BooleanChangeListener) remove} 
 * itself <b>and <i>only</i> itself</b> from this {@code BooleanRef}'s list
 * of {@code BooleanChangeListener}s during its action. If it removes any other {@code BooleanChangeListener}, all future behavior of this object is undefined.</p>
 * @author Sam Hooper
 *
 */
public class BooleanRef implements Ref {
	private boolean value;
	
	private ArrayList<BooleanChangeListener> changeListeners; //only constructed when a listener is actually added.
	
	/**
	 * Creates a new {@code BooleanRef} storing the given value.
	 */
	public BooleanRef(boolean value) {
		this.value = value;
	}
	
	/**
	 * @return the {@code boolean} value currently stored by this {@code BooleanRef}.
	 */
	public boolean get() {
		return value;
	}
	
	/**
	 * Does <b>not</b> trigger the {@link BooleanChangeListener}s if {@code newValue} is the same as the {@code boolean} currently stored by this
	 * {@code booleanRef}. Returns {@code false} if the new value is the same as the current one, {@code true} otherwise.
	 * @param newValue
	 */
	public boolean set(boolean newValue) {
		if(value == newValue)
			return false;
		boolean oldValue = this.value;
		this.value = newValue;
		runChangeListeners(oldValue, newValue);
		return true;
	}

	private void runChangeListeners(boolean oldValue, boolean newValue) {
		if(changeListeners != null) {
			int size = changeListeners.size();
			for(int i = 0; i < size; i++) {
				changeListeners.get(i).changed(oldValue, newValue);
				if(size == changeListeners.size() + 1) {
					i--;
					size = changeListeners.size();
				}
			}
		}
	}
	
	/**
	 * Adds the given {@link BooleanChangeListener} to this {@code BooleanRef}'s list of {@code BooleanChangeListener}s.
	 * @param listener
	 */
	public void addChangeListener(BooleanChangeListener listener) {
		if(changeListeners == null)
			changeListeners = new ArrayList<>();
		changeListeners.add(listener);
	}
	
	/**
	 * Removes the given {@link BooleanChangeListener} from this {@code BooleanRef}'s list of {@code BooleanChangeListener}s.
	 * This is an O(n) operation, where n is the number of listeners on this {@code BooleanRef}.
	 * @param listener
	 * @return {@code true} if the listener was present and has been removed, {@code false} if the listener was not present.
	 */
	public boolean removeChangeListener(BooleanChangeListener listener) {
		return changeListeners != null && changeListeners.remove(listener);
	}
	
	public List<BooleanChangeListener> getChangeListenersUnmodifiable() {
		if(changeListeners == null)
			return Collections.emptyList();
		return Collections.unmodifiableList(changeListeners);
	}
}
