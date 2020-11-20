package utils.refs;

import suppliers.*;

/**
 * the low value must be >= the min, and the high value must be <= the max.
 * @author Sam Hooper
 *
 */
public class IntRange implements Ref {
	
	public static NamedIntRange named(final int min, final int max, final int low, final int high, String name) {
		return NamedIntRange.of(new IntRange(min, max, low, high), name);
	}
	
	public static NamedIntRange from(final RangeStore store, final String name) {
		return NamedIntRange.of(store.min(), store.max(), store.low(), store.high(), name);
	}
	
	
	private final int min, max;
	private IntRef lowRef, highRef;
	
	/**
	 * {@link IntRange#min} must be initialized to its final value before a {@link LowRef} is constructed.
	 * @author Sam Hooper
	 *
	 */
	private class LowRef extends IntRef {
		
		public LowRef(int value) {
			super(value);
		}

		@Override
		public boolean set(int newValue) {
			if(newValue < min)
				throw new IllegalArgumentException("Low value cannot be lower than min value.");
			return super.set(newValue);
		}
		
	}
	
	/**
	 * {@link IntRange#max} must be initialized to its final value before a {@link HighRef} is constructed.
	 * @author Sam Hooper
	 *
	 */
	private class HighRef extends IntRef {
		
		public HighRef(int value) {
			super(value);
		}

		@Override
		public boolean set(int newValue) {
			if(newValue > max)
				throw new IllegalArgumentException("High value cannot be greater than max value.");
			return super.set(newValue);
		}
		
	}
	
	/**
	 * @param min
	 * @param max
	 * @param low
	 * @param high
	 */
	public IntRange(final int min, final int max, final int low, final int high) {
		super();
//		System.out.printf("(enter) IntRange constructor with args=(min=%d, max=%d, low=%d, high=%d)%n", min, max, low, high);
		verify(min, max, low, high);
		this.min = min;
		this.max = max;
		this.lowRef = new LowRef(low);
		this.highRef = new HighRef(high);
//		System.out.printf("new IntRange created: %s%n", this);
	}
	
	private static void verify(final int min, final int max, final int low, final int high) {
		if(low < min)
			throw new IllegalArgumentException("Low value must be greater than or equal to min value.");
		if(high > max)
			throw new IllegalArgumentException("High value must be less than or equal to max value.");
	}

	public int getMin() {
		return min;
	}

	public int getMax() {
		return max;
	}

	public IntRef lowRef() {
		return lowRef;
	}

	public IntRef highRef() {
		return highRef;
	}
	
	public void setHigh(final int newHigh) {
		highRef().set(newHigh);
	}
	
	public void setLow(final int newLow) {
		lowRef().set(newLow);
	}
	
	public int getHigh() {
		return highRef().get();
	}
	
	public int getLow() {
		return lowRef().get();
	}
	
	/** Returns the total number of {@code int}s that would be allowed in this {@link IntRange}, equal to {@code (getHigh() - getLow() + 1)}.
	 */
	public int valueRange() {
		return getHigh() - getLow() + 1;
	}
	
	@Override
	public String toString() {
		return "IntRange[min=" + min + ", max=" + max + ", lowRef=" + lowRef + ", highRef=" + highRef + "]";
	}
	
	
}
