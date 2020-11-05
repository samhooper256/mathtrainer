package math;

import java.math.*;
import java.util.*;
import java.util.function.*;
import java.util.regex.*;

/**
 * @author Sam Hooper
 *
 */
public class Evaluator {
	
	private static final MathContext MATH_CONTEXT = new MathContext(32, RoundingMode.HALF_DOWN);
	private static final Map<String, Integer> binaryPrecedence;
	private static final Map<String, Integer> unaryPrecedence;
	private static final Map<String, BiFunction<Expression, Expression, BinaryOperator>> binaryFactories;
	private static final Map<String, Function<Expression, UnaryOperator>> unaryFactories;
	private static final Pattern WHITESPACE = Pattern.compile("\\s+");
	private static final Pattern SIGNS = Pattern.compile("[+-]+");
	private static final Pattern TOKEN_BOUNDARY = Pattern.compile("(?<=[^0-9\\.])|(?=[^0-9\\.])");
	
	static {
		HashMap<String, Integer> binMap = new HashMap<>();
		binMap.put("+", 1);
		binMap.put("-", 1);
		binMap.put("*", 2);
		binMap.put("/", 2);
		binMap.put("^", 3);
		binaryPrecedence = Collections.unmodifiableMap(binMap);
		HashMap<String, Integer> unaryMap = new HashMap<>();
		unaryMap.put("+", 4);
		unaryMap.put("-", 4);
		unaryPrecedence = Collections.unmodifiableMap(unaryMap);
		HashMap<String, BiFunction<Expression, Expression, BinaryOperator>> binFacs = new HashMap<>();
		binFacs.put("+", AdditionOperation::new);
		binFacs.put("-", SubtractionOperation::new);
		binFacs.put("*", MultiplicationOperation::new);
		binFacs.put("/", DivisionOperation::new);
		binFacs.put("^", ExponentiationOperation::new);
		binaryFactories = Collections.unmodifiableMap(binFacs);
		HashMap<String, Function<Expression, UnaryOperator>> unaryFacs = new HashMap<>();
		unaryFacs.put("+", UnaryPlus::new);
		unaryFacs.put("-", UnaryMinus::new);
		unaryFactories = Collections.unmodifiableMap(unaryFacs);
		
	}
	
	interface Expression{
		BigDecimal eval();
	}
	interface HasOperator{
		String getOperator();
		int getPrecendence();
	}
	abstract static class BinaryOperator implements Expression, HasOperator{
		final Expression left, right;
		public BinaryOperator(Expression left, Expression right) {
			this.left = left;
			this.right = right;
		}
		@Override
		public final int getPrecendence() {
			return binaryPrecedence.get(getOperator());
		}
		@Override
		public String toString() { return "(" + left + getOperator() + right + ")"; }
	}
	static abstract class AdditiveExpression extends BinaryOperator{
		/**
		 * @param left
		 * @param right
		 */
		public AdditiveExpression(Expression left, Expression right) {
			super(left, right);
		}
	}
	
	static abstract class MultiplicativeExpression extends BinaryOperator{
		/**
		 * @param left
		 * @param right
		 */
		public MultiplicativeExpression(Expression left, Expression right) {
			super(left, right);
		}
	}
	
	static class AdditionOperation extends AdditiveExpression{
		/**
		 * @param left
		 * @param right
		 */
		public AdditionOperation(Expression left, Expression right) {
			super(left, right);
		}
		@Override
		public String getOperator() {
			return "+";
		}
		@Override
		public BigDecimal eval() {
//			System.out.println("Evaluating Addition : '" + this + "'");
			return left.eval().add(right.eval(), MATH_CONTEXT);
		}
		
	}
	
	static class SubtractionOperation extends AdditiveExpression{
		/**
		 * @param left
		 * @param right
		 */
		public SubtractionOperation(Expression left, Expression right) {
			super(left, right);
		}
		@Override
		public String getOperator() {
			return "-";
		}
		@Override
		public BigDecimal eval() {
//			System.out.println("Evaluating Subtraction : '" + this + "'");
			return left.eval().subtract(right.eval(), MATH_CONTEXT);
		}
	}
	
	static class MultiplicationOperation extends MultiplicativeExpression{
		/**
		 * @param left
		 * @param right
		 */
		public MultiplicationOperation(Expression left, Expression right) {
			super(left, right);
		}
		@Override
		public BigDecimal eval() {
//			System.out.println("Evaluating Multiplication : '" + this + "'");
			return left.eval().multiply(right.eval(), MATH_CONTEXT);
		}
		@Override
		public String getOperator() {
			return "*";
		}
	}
	
	static class DivisionOperation extends MultiplicativeExpression{
		/**
		 * @param left
		 * @param right
		 */
		public DivisionOperation(Expression left, Expression right) {
			super(left, right);
		}
		@Override
		public BigDecimal eval() {
//			System.out.println("Evaluating Division : '" + this + "'");
			return left.eval().divide(right.eval(), MATH_CONTEXT);
		}
		@Override
		public String getOperator() {
			return "/";
		}
	}
	
	static class ExponentiationOperation extends MultiplicativeExpression{
		/**
		 * @param left
		 * @param right
		 */
		public ExponentiationOperation(Expression left, Expression right) {
			super(left, right);
		}
		@Override
		public BigDecimal eval() {
			BigDecimal left = this.left.eval();
			BigDecimal right = this.right.eval();
			final int rightSign = right.signum();
			// Perform X^(A+B)=X^A*X^B (B = remainder)
	        double dleft = left.doubleValue();
	        right = right.abs(MATH_CONTEXT);
	        BigDecimal remainderOf2 = right.remainder(BigDecimal.ONE, MATH_CONTEXT);
	        BigDecimal rightIntPart = right.subtract(remainderOf2, MATH_CONTEXT);
	        BigDecimal intPow = left.pow(rightIntPart.intValueExact(),
	                MATH_CONTEXT);
	        BigDecimal doublePow =
	            new BigDecimal(Math.pow(dleft, remainderOf2.doubleValue()));
	        BigDecimal result = intPow.multiply(doublePow, MATH_CONTEXT);
	        if(rightSign == -1) {
	        	result = BigDecimal.ONE.divide(result, MATH_CONTEXT);
	        }
	        return result;
		}
		@Override
		public String getOperator() {
			return "^";
		}
	}
	
	abstract static class UnaryOperator implements Expression, HasOperator{
		Expression expr;
		public UnaryOperator(Expression expr) {
			this.expr = expr;
		}
		@Override
		public final int getPrecendence() {
			return unaryPrecedence.get(getOperator());
		}
		@Override
		public String toString() { return "(" + getOperator() + expr + ")"; }
	}
	static class UnaryMinus extends UnaryOperator{
		/**
		 * @param expr
		 */
		public UnaryMinus(Expression expr) {
			super(expr);
		}

		@Override
		public String getOperator() {
			return "-";
		}

		@Override
		public BigDecimal eval() {
			return expr.eval().negate(MATH_CONTEXT);
		}
	}
	static class UnaryPlus extends UnaryOperator{
		/**
		 * @param expr
		 */
		public UnaryPlus(Expression expr) {
			super(expr);
		}
		@Override
		public String getOperator() {
			return "+";
		}
		@Override
		public BigDecimal eval() {
			return expr.eval();
		}
	}
	static class ConstantExpression implements Expression{
		private final BigDecimal constant;
		public ConstantExpression(BigDecimal constant) {
			this.constant = constant;
		}
		@Override
		public BigDecimal eval() {
			return constant;
		}
		@Override
		public String toString() { return constant.toString(); }
	}
	
	public static void main(String[] args) {
		String[] tests = {"4/3*2-1+100", "3*-4^2", "3-(-3)+4(2)", "2+((3)(2)(--1))"};
//		System.out.println("RUNNING TESTS:");
//		for(int i = 0; i < tests.length; i++) {
//			final String test = tests[i];
//			System.out.printf("%nTest result %d: %s%n%n", i, evalString(test));
//		}
		Scanner in = new Scanner(System.in);
		while(true) {
			try {
				System.out.println("RESULT: " + evaluateAsBigDecimal(in.nextLine()));
			}
			catch(Exception e) {
				System.out.println("Error occured while parsing your input.");
			}
		}
	}
	
	public static BigDecimal evaluateAsBigDecimal(final String expression) {
		ExpressionParser parser = new ExpressionParser(expression.strip());
		Expression exp = parser.parseTokens();
		return exp.eval();
	}
	
	public static long evaluateAsLongOrThrow(final String expression) {
		return evaluateAsBigDecimal(expression).longValueExact();
	}
	
	private static boolean hasAnyDigits(final String s) {
		for(int i = 0; i < s.length(); i++)
			if(s.charAt(i) >= '0' && s.charAt(i) <= '9')
				return true;
		return false;
	}
	
	private static boolean isOperator(final String s) {
		return isBinaryOperator(s) || isUnaryOperator(s);
	}
	private static boolean isBinaryOperator(final String s) {
		return	binaryPrecedence.containsKey(s);
	}
	
	private static boolean isUnaryOperator(final String s) {
		return unaryPrecedence.containsKey(s);
	}
	private static Integer getBinaryPrecedence(final String op) {
		return binaryPrecedence.get(op);
	}
	
	private static Integer getUnaryPrecedence(final String op) {
		return unaryPrecedence.get(op);
	}
	
	private static final class ExpressionParser{
		private int index = 0;
		final String[] tokens;
		public ExpressionParser(final String input) {
			this.tokens = makeTokens(input);
		}
		private static String[] makeTokens(final String input) {
			String revised = WHITESPACE.matcher(input).replaceAll("");
			revised = SIGNS.matcher(revised).replaceAll(r -> {
				final String group = r.group();
				final int count = countChar(group, '-');
				return ((count & 1) == 0) ? "+" : "-";
			});
			String[] result = TOKEN_BOUNDARY.splitAsStream(revised).toArray(String[]::new);
			ArrayList<String> tempList = new ArrayList<>(result.length);
			for(int i = 0; i < result.length - 1; i++) {
				tempList.add(result[i]);
				if(result[i].equals(")")) {
					if(hasAnyDigits(result[i+1]) || result[i+1].equals("(")) {
						tempList.add("*");
					}
				}
				else if(hasAnyDigits(result[i]) && result[i+1].equals("(")) {
					tempList.add("*");
				}
			}
			tempList.add(result[result.length - 1]);
			result = tempList.toArray(String[]::new);
			return result;
		}
		private static int countChar(final String str, final char c) {
			int count = 0;
			for(int i = 0; i < str.length(); i++) {
				if(str.charAt(i) == c)
					count++;
			}
			return count;
		}
		private Expression node;
		public Expression parseTokens() {
			return parseTokens(0, tokens.length - 1);
		}
		/**
		 * leaves {@link #index} at the spot right after {@code endInclusive}.
		 * @return
		 */
		private Expression parseTokens(int startInclusive, final int endInclusive) {
			index = startInclusive;
			node = makeNode(Integer.MIN_VALUE);
			while(index <= endInclusive) {
				final int precedence;
				while(index <= endInclusive && tokens[index].equals(")"))
					index++;
				if(index > endInclusive) {
					assert index == endInclusive + 1;
					break;
				}
				final String op = tokens[index];
				{
					Integer prec = getBinaryPrecedence(op);
					if(prec == null) throw new IllegalArgumentException("Unrecognized operator: '" + op + "'");
					precedence = prec.intValue();
				}
				index++;
				node = fromOp(op, node, makeNode(precedence));
			}
			assert index == endInclusive + 1;
			return node;
		}
		/**
		 * {@link #index} must either be a number or a prefix unary operator or '('.
		 * When this method returns, {@link #index} will either be an operator or a closing parenthesis.
		 * @param minPrecedence
		 * @return
		 */
		Expression makeNode(final int minPrecedence) {
			final Expression first;
			if(tokens[index].equals("(")) {
				Expression parsed = parseTokens(index + 1, closeParen(index) - 1);
				if(!tokens[index].equals(")")) {
					throw new IllegalArgumentException("Malformed Parenthesis");
				}
				index++;
				return parsed;
			}
			if(isUnaryOperator(tokens[index])) {
				final String op = tokens[index];
				final int precedence = getUnaryPrecedence(op);
				index++;
				first = fromOp(op, makeNode(precedence));
			}
			else {
				first = new ConstantExpression(new BigDecimal(tokens[index], MATH_CONTEXT));
				index++;
			}
			
			//At this point, index should be at a binary operator (or at the end of input).
			if(index >= tokens.length) {
				index = tokens.length;
				return first;
			}
			else if(tokens[index].equals(")")) {
				return first;
			}
			final String op = tokens[index];
			
			final int precedence;
			{
				Integer prec = getBinaryPrecedence(op);
				if(prec == null) throw new IllegalArgumentException("Unrecognized operator: '" + op + "'");
				precedence = prec.intValue();
			}
			if(precedence <= minPrecedence) {
				return first;
			}
			index++;
			Expression result = fromOp(op, first, makeNode(precedence));
			assert index == tokens.length || isOperator(tokens[index]) || tokens[index].equals(")") :
				String.format("tokens[%d] = %s", index, tokens[index]);
			return result;
		}
		private int closeParen(int index) {
			int n = 1;
			for(int i = index + 1; i < tokens.length; i++) {
				if(tokens[i].equals(")")) {
					n--;
					if(n == 0) {
						return i;
					}
				}
				else if(tokens[i].equals("(")) {
					n++;
				}
			}
			throw new IllegalArgumentException("Malformed Parenthesis");
		}
	}
	
	
	
	private static UnaryOperator fromOp(final String operator, final Expression expr) {
		return unaryFactories.get(operator).apply(expr);
	}
	
	private static BinaryOperator fromOp(final String operator, final Expression left, final Expression right) {
		return binaryFactories.get(operator).apply(left, right);
	}
}
