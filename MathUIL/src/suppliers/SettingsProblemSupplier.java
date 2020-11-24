package suppliers;

import java.util.*;

import utils.refs.Ref;

/**
 * @author Sam Hooper
 *
 */
public abstract class SettingsProblemSupplier implements ProblemSupplier {
	
	protected List<Ref> settings;

	@Override
	public List<Ref> settings() {
		return settings;
	}
	
	protected void settings(Ref... sets) {
		if(settings == null)
			settings = new ArrayList<>();
		Collections.addAll(settings, sets);
	}
}