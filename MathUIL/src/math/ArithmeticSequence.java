package math;

import java.math.*;
import java.util.Objects;

/**
 * <p>An arithmetic sequence constructed from an initial (first) term and a common difference. Sums (see {@link #sum()}, {@link #sum(int)}, {@link #sum(int, int)})
 * and terms (see {@link #nthTerm(int)} are calculated on demand in O(1).</p>
 * @author Sam Hooper
 *
 */
public class ArithmeticSequence implements FiniteComplexSequence {
	/*
	public static void main(String[] args) {
		ArithmeticSequence seq = new ArithmeticSequence(Complex.ONE, Complex.ONE, 3);
		System.out.println(seq);
		System.out.println(seq.toPartialString(1, "+"));
		System.out.println(seq.toPartialString(2, "+"));
		System.out.println(seq.toPartialString(3, "+"));
		System.out.println(seq.toPartialString(123, "+"));
		ArithmeticSequence seq2 = new ArithmeticSequence(Complex.ONE, Complex.ONE, 100);
		System.out.println(seq2);
		System.out.println(seq2.toPartialString(3, " + "));
		System.out.println(seq2.sum());
		System.out.println(seq2.sum(10, 13));
	}
	*/
	
	private static final MathContext DEFAULT_MATH_CONTEXT = new MathContext(32);
	
	private final Complex firstTerm;
	private final Complex difference;
	private final int size;
	
	/**
	 * @throws NullPointerException if {@code firstTerm} or {@code difference} is {@code null}.
	 * @throws IllegalArgumentException if {@code size <= 0}
	 */
	public ArithmeticSequence(final Complex firstTerm, final Complex difference, final int size) {
		if(size <= 0)
			throw new IllegalArgumentException("size <= 0");
		this.firstTerm = Objects.requireNonNull(firstTerm);
		this.difference = Objects.requireNonNull(difference);
		this.size = size;
	}
	
	@Override
	public Complex nthTerm(int n) {
		return difference().multiply(n - 1).add(firstTerm);
	}
	
	@Override
	public int size() {
		return size;
	}
	
	@Override
	public Complex sum(int startIndexInclusive, int endIndexInclusive) {
		if(startIndexInclusive > endIndexInclusive)
			return Complex.ZERO;
		if(startIndexInclusive <= 0 || endIndexInclusive > size())
			throw new IllegalArgumentException(String.format("Illegal arguments: startIndexInclusive=%d, endIndexInclusive=%d", startIndexInclusive, endIndexInclusive));
		int n = endIndexInclusive - startIndexInclusive + 1;
		Complex a1 = nthTerm(startIndexInclusive), aN = nthTerm(endIndexInclusive);
		return a1.add(aN).divide(BigDecimal.valueOf(2), DEFAULT_MATH_CONTEXT).multiply(n);
	}
	
	/**
	 * Equivalent to {@link #toFullString()}.
	 */
	@Override
	public String toString() {
		return toFullString();
	}

	public Complex difference() {
		return difference;
	}
	
	public Complex firstTerm() {
		return firstTerm;
	}
	
}
