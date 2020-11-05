package suppliers;

import java.util.*;

import utils.*;

/**
 * @author Sam Hooper
 *
 */
public abstract class SettingsProblemSupplier implements ProblemSupplier {
	
	protected List<Ref> settings;

	@Override
	public Collection<Ref> settings() {
		return settings;
	}
	
	
}
