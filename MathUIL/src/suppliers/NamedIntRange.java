package suppliers;

import java.util.Objects;

import utils.refs.IntRange;

/**
 * @author Sam Hooper
 *
 */
public class NamedIntRange extends AbstractNamedSetting<IntRange> {
	
	/**
	 * Equivalent to {@code new IntRange(range, name)}.
	 */
	public static NamedIntRange of(final IntRange range, final String name) {
		return new NamedIntRange(range, name);
	}
	
	/**
	 * Equivalent to {@code of(new IntRange(min, max, low, high), name)}.
	 */
	public static NamedIntRange of(final int min, final int max, final int low, final int high, String name) {
		return of(new IntRange(min, max, low, high), name);
	}
	
	/**
	 * Equivalent to {@code of(store.min(), store.max(), store.low(), store.high(), name)}.
	 */
	public static NamedIntRange of(final RangeStore store, final String name) {
		return of(store.min(), store.max(), store.low(), store.high(), name);
	}
	
	private final IntRange range;
	private final String name;
	
	public NamedIntRange(final IntRange range, final String name) {
		this.range = Objects.requireNonNull(range);
		this.name = Objects.requireNonNull(name);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public IntRange ref() {
		return range;
	}
	
	public int low() {
		return ref().getLow();
	}
	
	public int high() {
		return ref().getHigh();
	}
	
	public int min() {
		return ref().getMin();
	}
	
	public int max() {
		return ref().getMax();
	}

	@Override
	public String toString() {
		return String.format("NamedIntRange[min=%d, max=%d, low=%d, high=%d]", min(), max(), low(), high());
	}
	
}
