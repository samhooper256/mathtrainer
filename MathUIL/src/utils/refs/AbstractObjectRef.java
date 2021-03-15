package utils.refs;

import java.util.ArrayList;

import utils.ChangeListener;

/**
 * 
 * @author Sam Hooper
 * @param <T>
 */
abstract class AbstractObjectRef<T> extends AbstractRef implements ObjectRef<T> {

	/** only initialized when a listener is actually added. */
	protected ArrayList<ChangeListener<T>> changeListeners;

	public AbstractObjectRef() {
		super();
	}

	@Override
	public void addChangeListener(ChangeListener<T> listener) {
		if(changeListeners == null) {
			changeListeners = new ArrayList<>();
		}
		changeListeners.add(listener);
	}

	@Override
	public boolean removeChangeListener(ChangeListener<T> listener) {
		return changeListeners != null && changeListeners.remove(listener);
	}

	protected void runChangeListeners(T oldValue, T newValue) {
		if(changeListeners != null)
			for(ChangeListener<T> listener : changeListeners)
				listener.changed(oldValue, newValue);
	}
	
	
}