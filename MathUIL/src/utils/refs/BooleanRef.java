package utils.refs;

import java.util.*;

import utils.*;

/**
 * <p>A {@link Ref} whose {@link #get() value} is a {@code boolean}. Changes to the value can be listened to using {@link BooleanChangeListener BooleanChangeListeners}.</p>
 * 
 * @implNote
 * <p>{@code BooleanRefs} whose {@link #get() value} does not change are permitted to do nothing when {@link #addChangeListener(BooleanChangeListener)} is called,
 * always return {@code false} when {@link #removeChangeListener(BooleanChangeListener)} is called, and return an empty {@link Collection} when
 * {@link #getChangeListenersUnmodifiable()} is called.</p>
 * @author Sam Hooper
 */
public interface BooleanRef {
	
	static class ConstantBooleanRef implements BooleanRef {
		
		final boolean value;
		
		private ConstantBooleanRef(final boolean value) {
			this.value = value;
		}
		
		@Override
		public boolean get() {
			return value;
		}

		@Override
		public void addChangeListener(BooleanChangeListener listener) {
			//do nothing; the value of this BooleanRef will never change.
		}

		@Override
		public boolean removeChangeListener(BooleanChangeListener listener) {
			return false;
		}

		@Override
		public Collection<BooleanChangeListener> getChangeListenersUnmodifiable() {
			return Collections.emptySet();
		}
		
	}
	
	BooleanRef FALSE = new ConstantBooleanRef(false), TRUE = new ConstantBooleanRef(true);
	
	/** Returns a constant {@link BooleanRef} with the given value.*/
	public static BooleanRef of(boolean value) {
		return value ? TRUE : FALSE;
	}
	
	boolean get();
	
	/**
	 * Adds the given {@link BooleanChangeListener} to this {@code BooleanRef}'s list of {@code BooleanChangeListener}s.
	 * @param listener
	 */
	void addChangeListener(BooleanChangeListener listener);
	
	/**
	 * Removes the given {@link BooleanChangeListener} from this {@code BooleanRef}'s list of {@code BooleanChangeListener}s.
	 * This is an O(n) operation, where n is the number of listeners on this {@code BooleanRef}.
	 * @param listener
	 * @return {@code true} if the listener was present and has been removed, {@code false} if the listener was not present.
	 */
	boolean removeChangeListener(BooleanChangeListener listener);
	
	Collection<BooleanChangeListener> getChangeListenersUnmodifiable();
	
}
