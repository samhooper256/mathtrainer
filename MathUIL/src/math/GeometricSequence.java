package math;

import java.util.Objects;

/**
 * @author Sam Hooper
 *
 */
public class GeometricSequence implements InfiniteSequence<BigFraction>, SummableSequence<BigFraction> {
	
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
		if(!hasFiniteSum())
			throw new ArithmeticException("This GeometricSequence does not have a finite sum");
		return firstTerm.divide(BigFraction.of(1, 1).subtract(ratio));
	}
	
	public boolean hasFiniteSum() {
		return ratio.compareTo(BigFraction.of(-1, 1)) > 0 && ratio.compareTo(BigFraction.of(1, 1)) < 0;
			
	}
	
	public BigFraction firstTerm() {
		return firstTerm;
	}

	public BigFraction ratio() {
		return ratio;
	}
	
}
