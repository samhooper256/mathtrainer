package suppliers;

import java.util.Objects;

import utils.*;
import utils.refs.BooleanRef;

/**
 * @author Sam Hooper
 *
 */
public class NamedBooleanRef extends AbstractNamedSetting<BooleanRef> {
	
	public static NamedBooleanRef of(final BooleanRef ref, final String name) {
		return new NamedBooleanRef(ref, name);
	}
	
	public static NamedBooleanRef of(final boolean value, final String name) {
		return new NamedBooleanRef(new BooleanRef(value), name);
	}
	
	private final BooleanRef ref;
	private final String name;
	
	public NamedBooleanRef(final BooleanRef ref, final String name) {
		this.ref = Objects.requireNonNull(ref);
		this.name = Objects.requireNonNull(name);
	}
	
	public boolean get() {
		return ref.get();
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public BooleanRef ref() {
		return ref;
	}
	
}
