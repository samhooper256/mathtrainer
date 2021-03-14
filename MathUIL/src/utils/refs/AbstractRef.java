package utils.refs;

import java.util.*;

/**
 * 
 * @author Sam Hooper
 */
public abstract class AbstractRef implements Ref {
	
	/** only initialized when a change action is actually {@link #addChangeAction(Runnable) added}.*/
	private List<Runnable> changeActions; 

	protected AbstractRef() {
		
	}

	@Override
	public void addChangeAction(Runnable action) {
		if(changeActions == null) {
			changeActions = new ArrayList<>();
		}
//		System.out.printf("change action %s added to %s%n",action,this);
		changeActions.add(action);
	}

	@Override
	public boolean removeChangeAction(Runnable action) {
		return changeActions.remove(action);
	}

	@Override
	public List<Runnable> getChangeActionsUnmodifiable() {
		if(changeActions == null)
			return Collections.emptyList();
		else
			return Collections.unmodifiableList(changeActions);
	}
	
	protected void runChangeActions() {
		if(changeActions != null) {
			changeActions.forEach(Runnable::run);
		}
	}
	
}
