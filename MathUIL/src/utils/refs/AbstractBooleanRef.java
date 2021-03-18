package utils.refs;

import java.util.*;

import utils.BooleanChangeListener;

/**
 * 
 * @author Sam Hooper
 */
abstract class AbstractBooleanRef extends AbstractRef implements BooleanRef {
	
	/** only initialized when a listener is actually added. */
	private ArrayList<BooleanChangeListener> changeListeners; 
	
	protected void runChangeListeners(boolean oldValue, boolean newValue) {
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
	
	
	@Override
	public void addChangeListener(BooleanChangeListener listener) {
		if(changeListeners == null)
			changeListeners = new ArrayList<>();
		changeListeners.add(listener);
	}
	
	
	@Override
	public boolean removeChangeListener(BooleanChangeListener listener) {
		return changeListeners != null && changeListeners.remove(listener);
	}


	@Override
	public Collection<BooleanChangeListener> getChangeListenersUnmodifiable() {
		return Collections.unmodifiableCollection(changeListeners);
	}
	
	
}
