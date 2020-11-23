package math;

import java.util.*;

/**
 * An fibonacci-like {@link InfiniteSequence} of {@link Complex} numbers. A "fibonacci-like" sequence is construced by providing the first and second term.
 * Each term after the second is equal to the sum of the previous two terms.
 * @author Sam Hooper
 *
 */
public class InfiniteFibSequence implements InfiniteSequence<Complex> {
	
	private final Complex first, second;
	private final ArrayList<Complex> memoized;
	public InfiniteFibSequence(Complex first, Complex second) {
		this.first = first;
		this.second = second;
		this.memoized = new ArrayList<>();
		Collections.addAll(memoized, null, first, second);
	}
	
	@Override
	public Complex nthTerm(int n) {
		while(n >= memoized.size())
			memoized.add(memoized.get(memoized.size() - 1).add(memoized.get(memoized.size() - 2)));
		return memoized.get(n);
	}
	
	public int sum(int startInclusive, int endInclusive) {
		Complex total = Complex.ZERO;
		for(int i = startInclusive; i <= endInclusive; i++)
			total = total.add(nthTerm(i));
		return total;
	}
}