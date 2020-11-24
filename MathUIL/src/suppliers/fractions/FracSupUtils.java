package suppliers.fractions;

import java.util.List;
import java.util.function.*;

import math.*;
import problems.*;
import suppliers.ProblemSupplier;

/**
 * Fraction-related {@link ProblemSupplier} Utilities
 * @author Sam Hooper
 *
 */
public class FracSupUtils {
	
	private FracSupUtils() {}
	
	/**
	 * Equivalent to {@code multiplyScrambled(a, b, true, true)}.
	 */
	public static Problem multiplyScrambled(final BigFraction a, final BigFraction b) {
		return multiplyScrambled(a, b, true, true);
	}
	
	public static Problem multiplyScrambled(final BigFraction a, final BigFraction b, final boolean acceptFraction, final boolean acceptMixed) {
		return opScrambled("*", (x, y) -> x.multiply(y), a, b, acceptFraction, acceptMixed);
	}
	
	/**
	 * Equivalent to {@code multiplyScrambled(a, b, true, true)}.
	 */
	public static Problem multiplyScrambled(final MixedNumber a, final MixedNumber b) {
		return multiplyScrambled(a, b, true, true);
	}
	
	public static Problem multiplyScrambled(final MixedNumber a, final MixedNumber b, final boolean acceptFraction, final boolean acceptMixed) {
		return multiplyScrambled(a.toFraction(), b.toFraction(), acceptFraction, acceptMixed);
	}
	
	/**
	 * Equivalent to {@code addScrambled(a, b, true, true)}.
	 */
	public static Problem addScrambled(final BigFraction a, final BigFraction b) {
		return addScrambled(a, b, true, true);
	}
	
	public static Problem addScrambled(final BigFraction a, final BigFraction b, final boolean acceptFraction, final boolean acceptMixed) {
		return opScrambled("+", BigFraction::add, a, b, acceptFraction, acceptMixed);
	}
	
	/**
	 * Equivalent to {@code addScrambled(a, b, true, true)}.
	 */
	public static Problem addScrambled(final MixedNumber a, final MixedNumber b) {
		return addScrambled(a, b, true, true);
	}
	
	public static Problem addScrambled(final MixedNumber a, final MixedNumber b, final boolean acceptFraction, final boolean acceptMixed) {
		return addScrambled(a.toFraction(), b.toFraction(), acceptFraction, acceptMixed);
	}
	
	/**
	 * Equivalent to {@code divideReformed(a, b, true, true)}.
	 */
	public static Problem divideReformed(final BigFraction a, final BigFraction b) {
		return divideReformed(a, b, true, true);
	}
	
	public static Problem divideReformed(final BigFraction a, final BigFraction b, final boolean acceptFraction, final boolean acceptMixed) {
		return opReformed("/", (x, y) -> x.divide(y), a, b, acceptFraction, acceptMixed);
	}
	
	/**
	 * Equivalent to {@code divideReformed(a, b, true, true)}.
	 */
	public static Problem subtractReformed(final BigFraction a, final BigFraction b) {
		return subtractReformed(a, b, true, true);
	}
	
	public static Problem subtractReformed(final BigFraction a, final BigFraction b, final boolean acceptFraction, final boolean acceptMixed) {
		return opReformed("-", (x, y) -> x.subtract(y), a, b, acceptFraction, acceptMixed);
	}
	
	/**
	 * Returns a {@link Problem} that asks for the solver to evaluate the expression "a <i>opString</p> b", where a may be changed to a {@link MixedNumber}
	 * with probability 0.5 and b may be changed to a {@code MixedNumber} with probability 0.5;
	 */
	public static Problem opReformed(final String opString, final BinaryOperator<BigFraction> op, final BigFraction a, final BigFraction b, final boolean acceptFraction, final boolean acceptMixed) {
		if(!acceptFraction && !acceptMixed)
			throw new IllegalArgumentException();
		BigFraction result = op.apply(a, b);
		Number n1 = Math.random() <= 0.5 && a.isImproper() ? a.toMixedNumber() : a;
		Number n2 = Math.random() <= 0.5 && b.isImproper() ? b.toMixedNumber() : b;
		final MultiValued exp = MultiValued.of(new DisplayExpression().addTerm(n1).addOperator(opString).addTerm(n2).toMathML()).setLines(1.5);
		if(acceptFraction)
			exp.addResult(result);
		if(acceptMixed)
			exp.addResult(result.toMixedNumber());
		return exp;
	}
	
	/**
	 * The {@link BinaryOperator} is assumed to be commutative and associative.
	 * @param opString
	 * @param op
	 * @param a
	 * @param b
	 * @param acceptFraction
	 * @param acceptMixed
	 * @return
	 */
	private static Problem opScrambled(final String opString, final BinaryOperator<BigFraction> op, final BigFraction a, final BigFraction b, final boolean acceptFraction, final boolean acceptMixed) {
		if(!acceptFraction && !acceptMixed)
			throw new IllegalArgumentException();
		BigFraction result = op.apply(a, b);
		List<BigFraction> shuffled = Problem.shuffled(a, b);
		BigFraction shuf1 = shuffled.get(0), shuf2 = shuffled.get(1);
		Number num1 = shuf1.isImproper() && Math.random() <= 0.5 ? shuf1.toMixedNumber() : shuf1;
		Number num2 = shuf2.isImproper() && Math.random() <= 0.5 ? shuf2.toMixedNumber() : shuf2;
		MultiValued prob = MultiValued.of(new DisplayExpression().addTerm(num1).addOperator(opString).addTerm(num2).toMathML()).setLines(1.5);
		if(acceptFraction)
			prob.addResult(result);
		if(acceptMixed && result.isImproper())
			prob.addResult(result.toMixedNumber());
		return prob;
	}
}
