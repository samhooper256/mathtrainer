package suppliers;

import java.util.Objects;

import utils.*;
import utils.refs.MutableBooleanRef;

/**
 * @author Sam Hooper
 *
 */
public class NamedBooleanRef extends AbstractNamedSetting<MutableBooleanRef> {
	
	public static NamedBooleanRef of(final MutableBooleanRef ref, final String name) {
		return new NamedBooleanRef(ref, name);
	}
	
	public static NamedBooleanRef of(final boolean value, final String name) {
		return new NamedBooleanRef(new MutableBooleanRef(value), name);
	}
	
	private final MutableBooleanRef ref;
	private final String name;
	
	public NamedBooleanRef(final MutableBooleanRef ref, final String name) {
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
	public MutableBooleanRef ref() {
		return ref;
	}
	
}
