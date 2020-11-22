package math;

/**
 * @author Sam Hooper
 *
 */
public enum NumberAdjectives {
	EVIL;
	
	
	/**
	 * Returns the nth s-gonal number.
	 */
	public static int sGonalNumber(int s, int n) {
		return ((s - 2) * n * n - (s - 4) * n) / 2;
	}
	
	public static final int MIN_POLYGON_SIDES = 3;
	public static final int MAX_POLYGON_SIDES = 12;
	
	private static final String[] POLYGONAL_ADJECTIVES = {"triangular", "square", "pentagonal", "hexagonal", "heptagonal", "octagonal", "nonagonal", "decagonal", "hendecagonal", "dodecagonal"};
	
	/** Throws some kind of exception if the given number of sides is invalid or unsupported. The number of sides must be between {@link #MIN_POLYGON_SIDES} and
	 * {@link #MAX_POLYGON_SIDES}, inclusive.*/
	public static String polygonalAdjective(int sides) {
		return POLYGONAL_ADJECTIVES[sides - MIN_POLYGON_SIDES];
	}
	
}
