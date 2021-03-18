package utils.refs;

import utils.BooleanChangeListener;

/**
 * <p>A reference to a {@code boolean}.</p>
 * 
 * <p>A {@link BooleanChangeListener} listening to this {@code BooleanRef} is permitted to {@link #removeChangeListener(BooleanChangeListener) remove} 
 * itself <b>and <i>only</i> itself</b> from this {@code BooleanRef}'s list
 * of {@code BooleanChangeListener}s during its action. If it removes any other {@code BooleanChangeListeners}, all future behavior of this object is undefined.</p>
 * @author Sam Hooper
 *
 */
public class MutableBooleanRef extends AbstractBooleanRef {
	
	private boolean value;
	
	/**
	 * Creates a new {@code BooleanRef} storing the given value.
	 */
	public MutableBooleanRef(boolean value) {
		this.value = value;
	}
	
	/**
	 * @return the {@code boolean} value currently stored by this {@code BooleanRef}.
	 */
	@Override
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
		runChangeActions();
		return true;
	}
	
	
	
}
