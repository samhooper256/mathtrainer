package suppliers;

import java.util.List;

import utils.refs.Ref;

/**
 * 
 * @author Sam Hooper
 */
abstract class AbstractNamedSetting<T extends Ref> implements NamedSetting<T> {

	protected AbstractNamedSetting() {
		
	}

	@Override
	public void addChangeAction(Runnable action) {
		ref().addChangeAction(action);
	}

	@Override
	public boolean removeChangeAction(Runnable action) {
		return ref().removeChangeAction(action);
	}

	@Override
	public List<Runnable> getChangeActionsUnmodifiable() {
		return ref().getChangeActionsUnmodifiable();
	}
	
}
