package utils;

import java.util.*;

/**
 * <p>Several of the methods in this utility class that are related to sets actually accept any arbitrarily {@link Collection} as a parameter instead of just a {@link Set}.
 * This is done to allow {@code Collections} containing distinct elements to be treated as {@code Sets} when it is reasonable to do so.</P>
 * @author Sam Hooper
 *
 */
public final class Sets {

	private Sets() {}
	
	/**
	 * Returns the number of possible subsets of the given {@link Collection} if every element in the {@code Collection} is considered to be unique.
	 * The returned {@code long} value includes the empty set and the improper subset.
	 * @throws NullPointerException if {@code set} is {@code null}.
	 * @throws ArithmeticException if the number of subsets of {@code set} is too large to fit in a {@code long}.
	 */
	public static long numSubsets(Collection<?> set) {
		if(set.size() > 62)
			throw new ArithmeticException("The number of subsets of the given set is too large to fit in a long");
		return 1L << set.size();
	}
	
	/**
	 * Returns the number of improper subsets of the given {@link Collection} (that is, the number {@code 1}).
	 * @throws NullPointerException if {@code set} is {@code null}.
	 */
	public static int numImproperSubsets(Collection<?> set) {
		Objects.requireNonNull(set);
		return 1;
	}
	
	/**
	 * Returns the number of proper subsets of the given {@link Collection} if every element in the {@code Collection} is considered to be unique.
	 * @throws NullPointerException if {@code set} is {@code null}
	 * @throws ArithmeticException if the number of proper subsets of {@code set} is too large to fit in a {@code long}.
	 */
	public static long numProperSubsets(Collection<?> set) {
		if(set.size() == 63)
			return Long.MAX_VALUE;
		if(set.size() > 62)
			throw new ArithmeticException("The number of proper subsets of the given set is too large to fit in a long");
		return (1L << set.size()) - 1;
	}
	
	
	public static <T> Set<T> union(Collection<? extends T> coll1, Collection<? extends T> coll2) {
		Set<T> res = new HashSet<>(coll1);
		res.addAll(coll2);
		return res;
	}
	
	public static <T> Set<T> intersection(Collection<? extends T> coll1, Collection<? extends T> coll2) {
		Set<T> res = new HashSet<>(coll1);
		res.retainAll(coll2);
		return res;
	}

}
