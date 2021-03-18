package suppliers;

import utils.refs.*;

/**
 * 
 * @author Sam Hooper
 */
public abstract class MultiModeSupplier extends SettingsProblemSupplier {

	protected final MutableObjectRef<SupplierMode> modeRef;
	protected SupplierMode currentProblemMode;
	
	protected MultiModeSupplier() {
		modeRef = new MutableObjectRef<>(SupplierMode.RANDOM);
		currentProblemMode = null;
	}
	
	@Override
	public ObjectRef<SupplierMode> getModeRef() {
		return modeRef;
	}
	
}
