package utils.refs;

import java.util.Collection;

import utils.IntChangeListener;

/**
 * 
 * @author Sam Hooper
 */
public interface IntRef extends Ref {
	
	int get();
	
	public void addChangeListener(IntChangeListener listener);
	
	public boolean removeChangeListener(IntChangeListener listener);
	
	public Collection<IntChangeListener> getChangeListenersUnmodifiable();
	
}
