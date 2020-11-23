package math;

import java.util.Objects;
import java.util.function.BinaryOperator;

/**
 * A infinite {@link #size() size} geometric sequence {@link #GeometricSequence(BigFraction, BigFraction) constructed} from an
 * initial (first) term and a ratio. The {@link #nthTerm(int) nth term} and {@link #sum()} (and its variants) are computed on demand in O(1).
 * 
 * @author Sam Hooper
 *
 */
public class GeometricSequence implements SummableSequence<BigFraction> {
	/*
	public static void main(String[] args) {
		GeometricSequence seq = new GeometricSequence(BigFraction.of(1, 1), BigFraction.of(1, 2));
		System.out.println(seq.toPartialString(10, " + "));
		System.out.println(seq.sum());
		System.out.println(seq.sum(2,2));
		System.out.println(seq.sum(2,3));
		System.out.println(seq.sum(2,4));
		System.out.println(seq.sum(2));
		System.out.println(seq.sum(3));
	}
	*/
	
	public static final BinaryOperator<BigFraction> SUM_FUNCTION = BigFraction::sum;
	
	private final BigFraction firstTerm, ratio;
	
	/**
	 * @param firstTerm
	 * @param ratio
	 * @throws NullPointerException if either {@code firstTerm} or {@code ratio} are {@code null}. 
	 */
	public GeometricSequence(final BigFraction firstTerm, final BigFraction ratio) {
		this.firstTerm = Objects.requireNonNull(firstTerm);
		this.ratio = Objects.requireNonNull(ratio);
	}
	
	@Override
	public BigFraction nthTerm(int n) {
		return firstTerm.multiply(ratio.pow(n - 1));
	}
	
	/**
	 * Returns this sum of this {@link GeometricSequence} if it is finite; otherwise, throws an {@link ArithmeticException}.
	 */
	@Override
	public BigFraction sum() {
		return sum(1);
	}
	
	public boolean hasFiniteSum() {
		return ratio.compareTo(BigFraction.NEGATIVE_ONE) > 0 && ratio.compareTo(BigFraction.ONE) < 0;
			
	}
	
	public BigFraction firstTerm() {
		return firstTerm;
	}

	public BigFraction ratio() {
		return ratio;
	}

	@Override
	public int size() {
		return -1;
	}

	@Override
	public BigFraction sum(int startInclusive) {
		if(startInclusive < 1)
			throw new IllegalArgumentException("startInclusive < 1");
		if(!hasFiniteSum())
			throw new ArithmeticException("This GeometricSequence does not have a finite sum");
		return nthTerm(startInclusive).divide(BigFraction.ONE.subtract(ratio));
	}

	@Override
	public BigFraction sum(int startInclusive, int endInclusive) {
		if(startInclusive > endInclusive || startInclusive < 1)
			throw new IllegalArgumentException(String.format("Invalid arguments: startInclusive=%d, endInclusive=%d", startInclusive, endInclusive));
		final BigFraction a = nthTerm(startInclusive);
		final int n = endInclusive - startInclusive + 1;
		return a.multiply(BigFraction.ONE.subtract(ratio.pow(n))).divide(BigFraction.ONE.subtract(ratio));
	}

	@Override
	public BinaryOperator<BigFraction> sumFunction() {
		return SUM_FUNCTION;
	}
	
}
