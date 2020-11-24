package math;

import java.math.BigInteger;
import java.util.function.BinaryOperator;

/**
 * @author Sam Hooper
 *
 */
public class SpecialSequences {
	
	
	public static void main(String[] args) {
		System.out.println(Squares.INFINITE.sum(2, 5) + " " + Squares.INFINITE.subSequence(2, 5).sum());
		System.out.println(Squares.INFINITE.sum(1, 3) + " " + Squares.INFINITE.subSequence(1, 3).sum());
		System.out.println(Squares.INFINITE.sum(6, 6) + " " + Squares.INFINITE.subSequence(6, 6).sum());
		System.out.println(Squares.INFINITE.sum(6, 7) + " " + Squares.INFINITE.subSequence(6, 7).sum() + " " + Squares.INFINITE.subSequence(5, 12).sum(2, 3));
		
		System.out.println();
		
		System.out.println(Cubes.INFINITE.sum(2, 5) + " " + Cubes.INFINITE.subSequence(2, 5).sum());
		System.out.println(Cubes.INFINITE.sum(1, 3) + " " + Cubes.INFINITE.subSequence(1, 3).sum());
		System.out.println(Cubes.INFINITE.sum(6, 6) + " " + Cubes.INFINITE.subSequence(6, 6).sum());
		System.out.println(Cubes.INFINITE.sum(6, 7) + " " + Cubes.INFINITE.subSequence(6, 7).sum());
		
		System.out.println();
		
		System.out.println(Triangles.INFINITE.sum(2, 5) + " " + Triangles.INFINITE.subSequence(2, 5).sum());
		System.out.println(Triangles.INFINITE.sum(1, 3) + " " + Triangles.INFINITE.subSequence(1, 3).sum());
		System.out.println(Triangles.INFINITE.sum(6, 6) + " " + Triangles.INFINITE.subSequence(6, 6).sum());
		System.out.println(Triangles.INFINITE.sum(6, 7) + " " + Triangles.INFINITE.subSequence(6, 7).sum());
	}
	
	
	public enum Squares implements SummableSequence<BigInteger> {
		INFINITE;
		
		private static final BinaryOperator<BigInteger> SUM_FUNCTION = BigInteger::add;
		
		@Override
		public BigInteger nthTerm(int n) {
			return BigInteger.valueOf(n).pow(2);
		}

		@Override
		public int size() {
			return -1;
		}

		@Override
		public BigInteger sum(int startInclusive) {
			throw new ArithmeticException("This sequence does not have a finite sum");
		}
		
		@Override
		public BigInteger sum(int startInclusive, int endInclusive) {
			if(startInclusive > endInclusive || (isFinite() && endInclusive > size()) || startInclusive < 1)
				throw new IllegalArgumentException(String.format("Invalid arguments: startInclusive=%d, endInclusive=%d", startInclusive, endInclusive));
			return sumFirstN(endInclusive).subtract(sumFirstN(startInclusive - 1));
		}

		public BigInteger sumFirstN(final int n) {
			final BigInteger bigN = BigInteger.valueOf(n);
			return bigN.multiply(bigN.add(BigInteger.ONE)).multiply(BigInteger.TWO.multiply(bigN).add(BigInteger.ONE)).divide(BigInteger.valueOf(6));
		}

		@Override
		public BinaryOperator<BigInteger> sumFunction() {
			return SUM_FUNCTION;
		}
		
	}
	
	public enum Cubes implements SummableSequence<BigInteger> {
		INFINITE;
		
		private static final BinaryOperator<BigInteger> SUM_FUNCTION = BigInteger::add;
		
		@Override
		public BigInteger nthTerm(int n) {
			return BigInteger.valueOf(n).pow(3);
		}

		@Override
		public int size() {
			return -1;
		}
		
		@Override
		public BigInteger sum(int startInclusive) {
			throw new ArithmeticException("This sequence does not have a finite sum");
		}
		
		@Override
		public BigInteger sum(int startInclusive, int endInclusive) {
			if(startInclusive > endInclusive || (isFinite() && endInclusive > size()) || startInclusive < 1)
				throw new IllegalArgumentException(String.format("Invalid arguments: startInclusive=%d, endInclusive=%d", startInclusive, endInclusive));
			return sumFirstN(endInclusive).subtract(sumFirstN(startInclusive - 1));
		}
		
		public BigInteger sumFirstN(final int n) {
			//The sum of the first N cubes is (nth triangular number)^2
			return BigInteger.valueOf(NumberAdjectives.sGonalNumber(3, n)).pow(2);
		}
		
		@Override
		public BinaryOperator<BigInteger> sumFunction() {
			return SUM_FUNCTION;
		}
	}
	
	public enum Triangles implements SummableSequence<BigInteger>  {
		INFINITE;
		
		private static final BinaryOperator<BigInteger> SUM_FUNCTION = BigInteger::add;
		
		@Override
		public BigInteger nthTerm(int n) {
			return BigInteger.valueOf(NumberAdjectives.sGonalNumber(3, n));
		}

		@Override
		public int size() {
			return -1;
		}

		@Override
		public BigInteger sum(int startInclusive) {
			throw new ArithmeticException("This sequence does not have a finite sum");
		}
		
		@Override
		public BigInteger sum(int startInclusive, int endInclusive) {
			if(startInclusive > endInclusive || (isFinite() && endInclusive > size()) || startInclusive < 1)
				throw new IllegalArgumentException(String.format("Invalid arguments: startInclusive=%d, endInclusive=%d", startInclusive, endInclusive));
			return sumFirstN(endInclusive).subtract(sumFirstN(startInclusive - 1));
		}

		public BigInteger sumFirstN(final int n) {
			final BigInteger bigN = BigInteger.valueOf(n);
			return bigN.multiply(bigN.add(BigInteger.ONE)).multiply(bigN.add(BigInteger.TWO)).divide(BigInteger.valueOf(6));
		}

		@Override
		public BinaryOperator<BigInteger> sumFunction() {
			return SUM_FUNCTION;
		}
	}
}
