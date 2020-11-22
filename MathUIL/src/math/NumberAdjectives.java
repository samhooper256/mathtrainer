package math;

import java.util.*;

/**
 * @author Sam Hooper
 *
 */
public enum NumberAdjectives {
	DEFICIENT{

		@Override
		public boolean describes(int n) {
			return Utils.factorsUnsorted(n).sum() < n * 2;
		}
		
	},
	PERFECT{

		@Override
		public boolean describes(int n) {
			return Utils.factorsUnsorted(n).sum() == n * 2;
		}
		
	},
	ABUNDANT{

		@Override
		public boolean describes(int n) {
			return Utils.factorsUnsorted(n).sum() > n * 2;
		}
		
	},
	HAPPY{

		@Override
		public boolean describes(int n) {
			Set<Integer> seen = new HashSet<>();
			while(n != 1) {
				if(seen.contains(n))
					return false;
				seen.add(n);
				n = Utils.digits(n).map(x -> x * x).sum();
			}
			return true;
		}
		
	},
	UNHAPPY{

		@Override
		public boolean describes(int n) {
			return !HAPPY.describes(n);
		}
		
	},
	EVIL{

		@Override
		public boolean describes(int n) {
			return (Bits.countSetBits(n) & 1) == 0;
		}
		
	},
	ODIOUS{

		@Override
		public boolean describes(int n) {
			return !EVIL.describes(n);
		}
		
	},
	POLITE{

		@Override
		public boolean describes(int n) {
			return !IMPOLITE.describes(n);
		}
		
	},
	IMPOLITE{

		@Override
		public boolean describes(int n) {
			return Bits.isPowerOf2(n);
		}
		
	},
	/** The same as "economical" */
	FRUGAL{

		@Override
		public boolean describes(int n) {
			return digitsInPrimeFactorization(n) < Utils.magnitude(n);
		}
		
	},
	EQUIDIGITAL{

		@Override
		public boolean describes(int n) {
			return digitsInPrimeFactorization(n) == Utils.magnitude(n);
		}
		
	},
	/** The same as "wasteful" */
	EXTRAVAGANT{

		@Override
		public boolean describes(int n) {
			return digitsInPrimeFactorization(n) > Utils.magnitude(n);
		}
		
	};
	
	public abstract boolean describes(int n);
	
	public static EnumSet<NumberAdjectives> describe(int n) {
		EnumSet<NumberAdjectives> result = EnumSet.noneOf(NumberAdjectives.class);
		for(NumberAdjectives adj : NumberAdjectives.values())
			if(adj.describes(n))
				result.add(adj);
		return result;
	}
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
	
	/** Returns the politeness of {@code n}
	 * @param n
	 * @return
	 */
	public static int politeness(int n) {
		return (int) Utils.factorsUnsorted(n).stream().filter(x -> x > 1 && (x & 1) == 0).count();
	}
	
	private static int digitsInPrimeFactorization(int n) {
		SortedMap<Integer, Integer> map = Utils.primeFactorization(n);
		int total = 0;
		for(Map.Entry<Integer, Integer> e : map.entrySet()) {
			total += e.getKey();
			if(e.getValue() > 1)
				total += e.getValue();
		}
		return total;
	}
	
}
