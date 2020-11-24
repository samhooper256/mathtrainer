package problems;

import java.math.*;
import java.util.*;
import java.util.function.Function;

import math.*;

/**
 * A class that allows for a mathematical expression to be built and displayed. The expression can be evaluated to produce a {@link Complex} result, or
 * converted to {@link Prettifier pretty} {@link #toMathML() MathML string}.
 * @author Sam Hooper
 *
 */
public class DisplayExpression {
	
	private static class ParseInfo<T> {
		
		private final Class<? extends T> objectClass;
		private final Function<T, String> toEvaluable, toMathML;
		
		public ParseInfo(Class<? extends T> objectClass, Function<T, String> toEvaluable, Function<T, String> toMathML) {
			super();
			this.objectClass = objectClass;
			this.toEvaluable = toEvaluable;
			this.toMathML = toMathML;
		}
		
		public Class<? extends T> getObjectClass() {
			return objectClass;
		}
		
		String evaluable(T o) {
			return toEvaluable.apply(o);
		}
		
		String mathML(T o) {
			return toMathML.apply(o);
		}
		
	}
	
	private static class Token {
		
		private Object object;
		
		public Token(final Object object) {
			this.object = object;
		}
		
		public Object getObject() {
			return object;
		}
		
		public String toEvaluable() {
			return infoFor(object).evaluable(object);
		}
		
		public String toMathML() {
			return infoFor(object).mathML(object);
		}
	}
	
	private static class OperatorToken extends Token {
		
		public OperatorToken(final String operatorAsString) {
			super(operatorAsString);
		}
		
		@Override
		public String toEvaluable() {
			return getObject().toString();
		}
		
		@Override
		public String toMathML() {
			return Prettifier.op(getObject().toString());
		}
		
	}
	private static final Map<Class<?>, ParseInfo<?>> infos;
	
	static {
		infos = new HashMap<>();
		
		putInfo(BigInteger.class, bi -> bi.toString(), Prettifier::num);
		putInfo(BigDecimal.class, bd -> bd.toString(), Prettifier::num);
		putInfo(Complex.class, c -> c.toString(), Prettifier::num);
		putInfo(BigFraction.class, bf -> "(" + bf.getNumerator() + "/" + bf.getDenominator() + ")", Prettifier::frac);
		putInfo(MixedNumber.class, m -> "(" + m.getIntegralPart() + "+(" + m.getFractionalPart().getNumerator() + "/" + m.getFractionalPart().getDenominator() + "))", Prettifier::mixed);
	}
	
	private static <T> void putInfo(Class<? extends T> clazz, Function<T, String> toEvaluable, Function<T, String> toMathML) {
		infos.put(clazz, new ParseInfo<>(clazz, toEvaluable, toMathML));
	}
	
	private List<Token> tokens;
	
	
	public DisplayExpression() {
		this.tokens = new ArrayList<>();
	}
	
	/**
	 * Adds the given token to this {@link DisplayExpression}. The token must be one of:
	 * <ul>
	 * <li>{@link BigInteger}</li>
	 * <li>{@link BigDecimal}</li>
	 * <li>{@link Complex} (NOTE: there will <b>not</b> be parentheses placed around a+bi)</li>
	 * <li>{@link BigFraction}</li>
	 * <li>{@link MixedNumber}</li>
	 * </ul>
	 * @param token
	 * @return
	 */
	public DisplayExpression addTerm(final Object token) {
		tokens.add(new Token(token));
		return this;
	}
	
	/**
	 * Adds the given operator (given as a {@code String}) to the tokens of this {@link DisplayExpression}. Parentheses, percents, and absolute value bars
	 * count as operators here.
	 * @param operatorAsString
	 * @return
	 */
	public DisplayExpression addOperator(final String operatorAsString) {
		tokens.add(new OperatorToken(operatorAsString));
		return this;
	}
	
	public Complex evaluateAsComplex() {
		return Evaluator.evaluateAsComplex(toEvaluableString());
	}
	
	public String toMathML() {
		StringBuilder sb = new StringBuilder();
		for(Token token : tokens)
			sb.append(token.toMathML());
		return Prettifier.ensureMath(sb.toString());
	}
	
	public String toEvaluableString() {
		StringBuilder sb = new StringBuilder();
		for(Token token : tokens)
			sb.append(token.toEvaluable());
		return sb.toString();
	}
	
	private static <T> ParseInfo<T> infoFor(T obj) {
		return infoFor((Class<? extends T>) obj.getClass());
	}
	
	private static <T> ParseInfo<T> infoFor(Class<? extends T> clazz) {
		return (ParseInfo<T>) infos.get(clazz);
	}
}
