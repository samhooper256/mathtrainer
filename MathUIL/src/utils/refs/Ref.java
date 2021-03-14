package utils.refs;

import java.util.List;

/**
 * A reference to some data. {@code Refs} allow an action to be run whenever the data to which they refer changes (if ever).
 * {@link Ref Refs} may or may not allow for the data they refer to to be changed through the {@link Ref}.
 * {@link Ref Refs} are not required to make the data they refer to publicly available.
 * 
 * @author Sam Hooper
 *
 */
public interface Ref {
	
	void addChangeAction(Runnable action);
	
	boolean removeChangeAction(Runnable action);
	
	List<Runnable> getChangeActionsUnmodifiable();
	
}
