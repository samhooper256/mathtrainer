package math;

import java.util.*;
import java.util.function.BinaryOperator;

/**
 * An fibonacci-like {@link Sequence} of {@link Complex} numbers. A "fibonacci-like" sequence is {@link #FibSequence(Complex, Complex) constructed}
 * by providing the first and second term.
 * Each term after the second is equal to the sum of the previous two terms.
 * @author Sam Hooper
 *
 */
public class FibSequence implements SummableSequence<Complex> {
	
	public static final BinaryOperator<Complex> SUM_FUNCTION = Complex::add;
	
	private final Complex first, second;
	private final ArrayList<Complex> memoized;
	public FibSequence(Complex first, Complex second) {
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
	
	@Override
	public int size() {
		return -1;
	}
	
	@Override
	public Complex sum(int startInclusive, int endInclusive) {
		Complex total = Complex.ZERO;
		for(int i = startInclusive; i <= endInclusive; i++)
			total = total.add(nthTerm(i));
		return total;
	}

	@Override
	public Complex sum() {
		if(first.isZero() && second.isZero())
			return Complex.ZERO;
		throw new ArithmeticException("This FibSequence does not have a finite sum");
	}

	@Override
	public Complex sum(int startInclusive) {
		if(startInclusive < 1)
			throw new IllegalArgumentException("startInclusive < 1");
		if(first.isZero() && second.isZero())
			return Complex.ZERO;
		throw new ArithmeticException("This FibSequence does not have a finite sum");
	}

	@Override
	public BinaryOperator<Complex> sumFunction() {
		return SUM_FUNCTION;
	}
}