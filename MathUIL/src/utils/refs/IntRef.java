package utils.refs;

import java.util.Collection;

import utils.IntChangeListener;

/**
 * 
 * @author Sam Hooper
 */
public interface IntRef extends Ref {
	
	int get();
	
	void addChangeListener(IntChangeListener listener);
	
	boolean removeChangeListener(IntChangeListener listener);
	
	Collection<IntChangeListener> getChangeListenersUnmodifiable();
	
}
