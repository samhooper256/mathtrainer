package suppliers;

/**
 * @author Sam Hooper
 *
 */
public final class RangeStore {
	
	private final int min, max, low, high;

	private RangeStore(int min, int max, int low, int high) {
		super();
		this.min = min;
		this.max = max;
		this.low = low;
		this.high = high;
	}
	
	public static RangeStore of(final int min, final int max, final int low, final int high) {
		return new RangeStore(min, max, low, high);
	}
	
	/**
	 * Equivalent to {@code of(min, max, min, max)}.
	 */
	public static RangeStore of(final int min, final int max) {
		return of(min, max, min, max);
	}

	public int min() {
		return min;
	}

	public int max() {
		return max;
	}

	public int low() {
		return low;
	}

	public int high() {
		return high;
	}
	
	
}
