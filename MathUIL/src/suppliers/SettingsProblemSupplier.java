package suppliers;

import java.util.*;

import utils.*;
import utils.refs.Ref;

/**
 * @author Sam Hooper
 *
 */
public abstract class SettingsProblemSupplier implements ProblemSupplier {
	
	private List<Ref> settings;

	@Override
	public final List<Ref> settings() {
		if(settings == null)
			return Collections.emptyList();
		else
			return Collections.unmodifiableList(settings);
	}
	
	/** Adds a change action to all of the {@code newSettings} that calls {@link #settingsChanged()}.*/
	protected final void addAllSettings(Ref... newSettings) {
		if(this.settings == null)
			this.settings = new ArrayList<>();
		final Runnable changeAction = this::settingsChanged;
		for(Ref setting : newSettings) {
			setting.addChangeAction(changeAction);
		}
		Collections.addAll(settings, newSettings);
		settingsAdded();
	}
	
	/** Called immediately after a setting is added via {@link #addAllSettings(Ref...)}. Does nothing
	 * by default.*/
	protected void settingsAdded() {
		//overridden for functionality
	}
}