package utils;

/**
 * the low value must be >= the min, and the high value must be <= the max.
 * @author Sam Hooper
 *
 */
public class IntRange {
	
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
	public IntRange(int min, int max, int low, int high) {
		super();
		verify(min, max, low, high);
		this.min = min;
		this.max = max;
		this.lowRef = new LowRef(low);
		this.highRef = new HighRef(high);
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
	
	public int getHigh() {
		return highRef().get();
	}
	
	public int getLow() {
		return lowRef().get();
	}
}
