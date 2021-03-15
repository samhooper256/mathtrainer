package utils.refs;

import java.util.*;

import utils.IntChangeListener;

/**
 * 
 * @author Sam Hooper
 */
abstract class AbstractIntRef extends AbstractRef implements IntRef {
	
	/** only initialized when a listener is actually added. */
	private ArrayList<IntChangeListener> changeListeners;
	
	/**
	 * Adds the given {@link IntChangeListener} to this {@code IntRef}'s list of {@code IntChangeListener}s.
	 * @param listener
	 */
	@Override
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
	@Override
	public boolean removeChangeListener(IntChangeListener listener) {
		return changeListeners != null && changeListeners.remove(listener);
	}
	
	protected void runChangeListeners(int newValue, int oldValue) {
		if(changeListeners != null)
			for(IntChangeListener listener : changeListeners)
				listener.changed(oldValue, newValue);
	}

	@Override
	public Collection<IntChangeListener> getChangeListenersUnmodifiable() {
		return (changeListeners != null ? (Collections.unmodifiableCollection(changeListeners)) : (Collections.emptyList()));
	}
	
}
