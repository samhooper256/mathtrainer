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
	
	private enum Associativity {
		LEFT, RIGHT, BOTH;
		
		public boolean isLeft() {
			return this == LEFT || this == BOTH;
		}
		
		public boolean isRight() {
			return this == RIGHT || this == BOTH;
		}
		
	}
	public static final MathContext DEFAULT_RESULT_CONTEXT = new MathContext(16, RoundingMode.HALF_UP);
	
	private static final MathContext INTERMEDIATE_MATH_CONTEXT = new MathContext(32, RoundingMode.HALF_UP);
	private static final Map<String, Integer> binaryPrecedence;
	private static final Map<String, Integer> unaryPrecedence;
	private static final Map<String, Associativity> binaryAssociativities;
	private static final Map<String, Associativity> unaryAssociativities;
	private static final Map<String, BiFunction<Expression, Expression, BinaryOperator>> binaryFactories;
	private static final Map<String, Function<Expression, UnaryOperator>> unaryFactories;
	private static final Pattern WHITESPACE = Pattern.compile("\\s+");
	private static final char DECIMAL_POINT = '.';
	
	static {
		
		binaryPrecedence = new HashMap<>();
		unaryPrecedence = new HashMap<>();
		binaryFactories = new HashMap<>();
		unaryFactories = new HashMap<>();
		binaryAssociativities = new HashMap<>();
		unaryAssociativities = new HashMap<>();
		
		putBinaryOperator("+", 1, Associativity.BOTH, AdditionOperation::new);
		putBinaryOperator("-", 1, Associativity.LEFT, SubtractionOperation::new);
		putBinaryOperator("*", 2, Associativity.BOTH, MultiplicationOperation::new);
		putBinaryOperator("/", 2, Associativity.LEFT, DivisionOperation::new);
		putBinaryOperator("^", 1, Associativity.RIGHT, ExponentiationOperation::new);
		
		putUnaryOperator("-", 2, Associativity.RIGHT, UnaryMinus::new);
		putUnaryOperator("+", 2, Associativity.RIGHT, UnaryPlus::new);
		putUnaryOperator("!", 4, Associativity.LEFT, Factorial::new);
		putUnaryOperator("%", 5, Associativity.LEFT, Percent::new);
	}
	
	private static void putBinaryOperator(final String op, final int precedence, final Associativity associativity, final BiFunction<Expression, Expression, BinaryOperator> factory) {
		binaryPrecedence.put(op, precedence);
		binaryAssociativities.put(op, associativity);
		binaryFactories.put(op, factory);
	}
	
	private static void putUnaryOperator(final String op, final int precedence, final Associativity associativity, final Function<Expression, UnaryOperator> factory) {
		unaryPrecedence.put(op, precedence);
		unaryAssociativities.put(op, associativity);
		unaryFactories.put(op, factory);
	}
	
	interface Expression {
		Complex eval();
	}
	
	interface HasOperator {
		
		String getOperator();
		
		int getPrecendence();
		
		Associativity getAssociativity();
		
		default boolean isLeftAssociative() {
			return getAssociativity().isLeft();
		}
		
		default boolean isRightAssociative() {
			return getAssociativity().isRight();
		}
	}
	
	abstract static class BinaryOperator implements Expression, HasOperator {
		
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
		public String toString() {
			return "(" + left + getOperator() + right + ")";
		}

		@Override
		public Associativity getAssociativity() {
			return binaryAssociativities.get(getOperator());
		}
		
	}
	
	static abstract class AdditiveExpression extends BinaryOperator {
		/**
		 * @param left
		 * @param right
		 */
		public AdditiveExpression(Expression left, Expression right) {
			super(left, right);
		}
	}
	
	static abstract class MultiplicativeExpression extends BinaryOperator {
		/**
		 * @param left
		 * @param right
		 */
		public MultiplicativeExpression(Expression left, Expression right) {
			super(left, right);
		}
	}
	
	static abstract class ExponentiativeExpression extends BinaryOperator {
		/**
		 * @param left
		 * @param right
		 */
		public ExponentiativeExpression(Expression left, Expression right) {
			super(left, right);
		}
	}
	
	static class AdditionOperation extends AdditiveExpression {
		
		public AdditionOperation(Expression left, Expression right) {
			super(left, right);
		}
		
		@Override
		public String getOperator() {
			return "+";
		}
		
		@Override
		public Complex eval() {
			return left.eval().add(right.eval(), INTERMEDIATE_MATH_CONTEXT);
		}
		
	}
	
	static class SubtractionOperation extends AdditiveExpression {
		
		public SubtractionOperation(Expression left, Expression right) {
			super(left, right);
		}
		
		@Override
		public String getOperator() {
			return "-";
		}
		
		@Override
		public Complex eval() {
			return left.eval().subtract(right.eval(), INTERMEDIATE_MATH_CONTEXT);
		}
		
	}
	
	static class MultiplicationOperation extends MultiplicativeExpression {
		
		public MultiplicationOperation(Expression left, Expression right) {
			super(left, right);
		}
		
		@Override
		public Complex eval() {
			return left.eval().multiply(right.eval(), INTERMEDIATE_MATH_CONTEXT);
		}
		
		@Override
		public String getOperator() {
			return "*";
		}
		
	}
	
	static class DivisionOperation extends MultiplicativeExpression {

		public DivisionOperation(Expression left, Expression right) {
			super(left, right);
		}
		
		@Override
		public Complex eval() {
			return left.eval().divide(right.eval(), INTERMEDIATE_MATH_CONTEXT);
		}
		
		@Override
		public String getOperator() {
			return "/";
		}
		
	}
	
	static class ExponentiationOperation extends ExponentiativeExpression {
		
		public ExponentiationOperation(Expression left, Expression right) {
			super(left, right);
		}
		
		@Override
		public Complex eval() {
			return left.eval().pow(right.eval(), INTERMEDIATE_MATH_CONTEXT);
		}
		
		@Override
		public String getOperator() {
			return "^";
		}
		
	}
	
	abstract static class UnaryOperator implements Expression, HasOperator {
		
		Expression expr;
		
		public UnaryOperator(Expression expr) {
			this.expr = expr;
		}
		
		@Override
		public final int getPrecendence() {
			return unaryPrecedence.get(getOperator());
		}
		
		@Override
		public String toString() {
			if(isLeftAssociative())
				return "(" + expr + getOperator() + ")";
			else
				return "(" + getOperator() + expr + ")";
		}

		@Override
		public Associativity getAssociativity() {
			return unaryAssociativities.get(getOperator());
		}
		
	}
	
	static class UnaryMinus extends UnaryOperator {
		
		public UnaryMinus(Expression expr) {
			super(expr);
		}

		@Override
		public String getOperator() {
			return "-";
		}

		@Override
		public Complex eval() {
			return expr.eval().negate(INTERMEDIATE_MATH_CONTEXT);
		}
		
	}
	
	static class UnaryPlus extends UnaryOperator{
		
		public UnaryPlus(Expression expr) {
			super(expr);
		}
		
		@Override
		public String getOperator() {
			return "+";
		}
		
		@Override
		public Complex eval() {
			return expr.eval();
		}
		
	}
	
	static class AbsoluteValueOperator extends UnaryOperator {
		
		public AbsoluteValueOperator(final Expression expr) {
			super(expr);
		}
		
		@Override
		public String getOperator() {
			return "|";
		}

		@Override
		public Complex eval() {
			return expr.eval().abs(INTERMEDIATE_MATH_CONTEXT);
		}

		@Override
		public String toString() {
			return String.format("|%s|", expr);
		}
		
	}
	
	static class Factorial extends UnaryOperator {
		
		public Factorial(Expression expr) {
			super(expr);
		}
		
		@Override
		public String getOperator() {
			return "!";
		}
		
		@Override
		public Complex eval() {
			return new Complex(Utils.factorial(expr.eval().intValueExact()));
		}
		
	}
	
	static class Percent extends UnaryOperator {
		
		private static final BigDecimal B100 = BigDecimal.valueOf(100);

		public Percent(Expression expr) {
			super(expr);
		}
		
		@Override
		public String getOperator() {
			return "%";
		}
		
		@Override
		public Complex eval() {
			return expr.eval().divide(B100, INTERMEDIATE_MATH_CONTEXT);
		}
		
	}
	
	static class ConstantExpression implements Expression {
		
		private final Complex constant;
		
		public ConstantExpression(Complex constant) {
			this.constant = constant;
		}
		
		@Override
		public Complex eval() {
			return constant;
		}
		
		@Override
		public String toString() {
			return constant.toString();
		}
		
	}
	
	private enum TokenType {
		OPERATOR, UNARY_OPERATOR, BINARY_OPERATOR, OPEN_PARENTHESIS, CLOSE_PARENTHESIS, ABSOLUTE_VALUE_BAR, CLOSE_ABSOLUTE_VALUE_BAR, OPEN_ABSOLUTE_VALUE_BAR, NUMBER;
		
		public boolean isOperator() {
			return this == UNARY_OPERATOR || this == BINARY_OPERATOR || this == OPERATOR;
		}
		
		public boolean isParenthesis() {
			return this == OPEN_PARENTHESIS || this == CLOSE_PARENTHESIS;
		}
		
		public boolean isOpeningSeparator() {
			return this == OPEN_ABSOLUTE_VALUE_BAR || this == OPEN_PARENTHESIS;
		}
		
		public boolean isClosingSeparator() {
			return this == CLOSE_ABSOLUTE_VALUE_BAR || this == CLOSE_PARENTHESIS;
		}
		
		public boolean isAbsoluteValueBar() {
			return this == OPEN_ABSOLUTE_VALUE_BAR || this == CLOSE_ABSOLUTE_VALUE_BAR || this == ABSOLUTE_VALUE_BAR;
		}
	}
	
	private static class Token {
		
		private TokenType type;
		private final String representation;
		
		public Token(final String representation, final TokenType type) {
			this.representation = representation;
			this.type = type;
		}
		
		public void setType(final TokenType newType) {
			this.type = newType;
		}
		
		public TokenType getType() {
			return type;
		}
		
		public String getRepresentation() {
			return representation;
		}
		
		@Override
		public String toString() {
			return String.format("Token[%s, %s]", representation, type);
		}
		
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
				System.out.println("RESULT: " + evaluateAsComplex(in.nextLine()));
			}
			catch(Exception e) {
				System.out.println("Error occured while parsing your input.");
				e.printStackTrace();
			}
		}
	}
	
	public static Complex evaluateAsComplex(final String expression) {
		return evaluateAsComplex(expression, DEFAULT_RESULT_CONTEXT);
	}
	
	public static Complex evaluateAsComplex(final String expression, final MathContext resultContext) {
		System.out.printf("evaluateAsComplex(%s)%n", expression);
		final List<Token> tokens = tokenize(WHITESPACE.matcher(expression).replaceAll(""));
		final List<Token> postfixTokens = toPostfix(tokens);
		Expression exp = makeTree(postfixTokens);
		return exp.eval().round(resultContext);
	}

	public static BigDecimal evaluateAsBigDecimalExact(final String expression) {
		return evaluateAsComplex(expression).bigDecimalValueExact();
	}
	
	public static long evaluateAsLongExact(final String expression) {
		return evaluateAsComplex(expression).longValueExact();
	}

	/** Given a mathematical expression in String form, tokenizes the expression and returns the results as a {@link List} of {@link Token Tokens}.*/
	private static List<Token> tokenize(final String expression) {
//		System.out.printf("tokenize(%s)%n", expression);
		List<Token> tokens = new ArrayList<>();
		for(int i = 0; i < expression.length(); ) {
			String iStr = expression.substring(i, i + 1);
			final Token token;
			if(isOpenParenthesis(iStr)) {
				token = new Token(iStr, TokenType.OPEN_PARENTHESIS);
				i++;
			}
			else if(isCloseParenthesis(iStr)) {
				token = new Token(iStr, TokenType.CLOSE_PARENTHESIS);
				i++;
			}
			else if(isAbsoluteValueBar(iStr)) {
				token = new Token(iStr, TokenType.ABSOLUTE_VALUE_BAR);
				i++;
			}
			else {
				if("0123456789i.".contains(iStr)) {
					int end = endOfComplexNumber(expression, i);
					token = new Token(expression.substring(i, end), TokenType.NUMBER);
					i = end;
				}
				else {
					String op = null;
					op_finder:
					for(int j = i + 1; j <= expression.length(); j++) {
						final String sub = expression.substring(i, j);
						if(isOperator(sub)) {
							op = sub;
							break op_finder;
						}
					}
					if(op == null)
						throw new IllegalArgumentException("Invalid expression. Tokens (at this stage of parsing) were: " + tokens);
					if(isUnaryOperator(op)) {
						if(isBinaryOperator(op)) { //This token could be a left or a right unary operator! We will figure out which one it is after.
							token = new Token(op, TokenType.OPERATOR);
						}
						else {
							token = new Token(op, TokenType.UNARY_OPERATOR);
						}
					}
					else {
						token = new Token(op, TokenType.BINARY_OPERATOR);
					}
					i += op.length();
				}
			}
			
			tokens.add(token);
		}
//		System.out.printf("before figuring out types of operators: %n\t%s%n", tokens);
		for(int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			if(token.getType() == TokenType.OPERATOR) {
				figureOutTypeOfOperator(tokens, i);
			}
		}
//		System.out.printf("after figuring out types of operators, before figuring out types of absolute value bars: %n\t%s%n", tokens);
		for(int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			if(token.getType() == TokenType.ABSOLUTE_VALUE_BAR) {
				figureOutTypeOfAbsoluteValueBar(tokens, i);
			}
		}
		return tokens;
	}

	/** <p>Given the list of tokens and the index of a token that could either be a binary operator or a unary operator (such as a '-' sign), this method
	 * figures out which type of operator it is and updates its {@link TokenType type} (via {@link Token#setType(TokenType)}) accordingly.</p>
	 * <p>Precondition: {@code tokens.get(i).getType() == TokenType.OPERATOR}</p>
	 * <p>Precondition: Any absolute values in the token list have the type {@link TokenType#ABSOLUTE_VALUE_BAR}.</p>
	 * */
	private static void figureOutTypeOfOperator(List<Token> tokens, int i) {
		final Token token = tokens.get(i);
		assert token.getType() == TokenType.OPERATOR;
		if(wouldBeLeftAssociativeIfItWereAUnaryOperator(token.getRepresentation())) {
			if(i == 0) {
				throw new IllegalArgumentException("Invalid expression");
			}
			else if(i == tokens.size() - 1) {
				token.setType(TokenType.UNARY_OPERATOR);
			}
			else {
				Token nextToken = tokens.get(i + 1);
				if(nextToken.getType() == null)
					figureOutTypeOfOperator(tokens, i + 1);
				switch(nextToken.getType()) {
				case ABSOLUTE_VALUE_BAR -> {
					Token nextNA;
					int j = i + 2;
					while(j < tokens.size() && tokens.get(j).getType() == TokenType.ABSOLUTE_VALUE_BAR)
						j++;
					if(j >= tokens.size()) {
						token.setType(TokenType.UNARY_OPERATOR);
					}
					else {
						nextNA = tokens.get(j);
						if(nextNA.getType() == null)
							figureOutTypeOfOperator(tokens, j);
						switch(nextNA.getType()) {
							case NUMBER, OPEN_PARENTHESIS -> token.setType(TokenType.BINARY_OPERATOR);
							case CLOSE_PARENTHESIS, BINARY_OPERATOR -> token.setType(TokenType.UNARY_OPERATOR);
							case UNARY_OPERATOR -> {
								if(isLeftAssociative(nextNA)) {
									token.setType(TokenType.UNARY_OPERATOR);
								}
								else {
									token.setType(TokenType.BINARY_OPERATOR);
								}
							}
							default -> throw new IllegalArgumentException("Unknown TokenType:" + nextNA.getType());
						}
					}
					throw new IllegalArgumentException("Undecidable situation - cannot be determined whether an operator is unary or binary");
				}
				case NUMBER, OPEN_PARENTHESIS -> token.setType(TokenType.BINARY_OPERATOR);
				case CLOSE_PARENTHESIS, BINARY_OPERATOR -> token.setType(TokenType.UNARY_OPERATOR);
				case UNARY_OPERATOR -> {
					if(isLeftAssociative(nextToken)) {
						token.setType(TokenType.UNARY_OPERATOR);
					}
					else {
						assert isRightAssociative(nextToken);
						token.setType(TokenType.BINARY_OPERATOR);
					}
				}
				default -> throw new IllegalArgumentException("Invalid expression");
				}
			}
		}
		else {
			assert wouldBeRightAssociativeIfItWereAUnaryOperator(token.getRepresentation());
			if(i == 0) {
				token.setType(TokenType.UNARY_OPERATOR);
			}
			else if(i == tokens.size() - 1) {
				throw new IllegalArgumentException("Invalid Expression");
			}
			else {
				Token prevToken = tokens.get(i - 1);
				switch(prevToken.getType()) {
				case ABSOLUTE_VALUE_BAR -> {
					final Token prevNA;
					int j = i - 2;
					while(j >= 0 && tokens.get(j).getType() == TokenType.ABSOLUTE_VALUE_BAR)
						j--;
					if(j < 0) {
						token.setType(TokenType.UNARY_OPERATOR);
					}
					else {
						prevNA = tokens.get(j);
						switch(prevNA.getType()) {
						case NUMBER, CLOSE_PARENTHESIS -> token.setType(TokenType.BINARY_OPERATOR);
						case BINARY_OPERATOR, OPEN_PARENTHESIS -> token.setType(TokenType.UNARY_OPERATOR);
						case UNARY_OPERATOR -> {
							if(isLeftAssociative(prevNA))
								token.setType(TokenType.BINARY_OPERATOR);
							else
								token.setType(TokenType.UNARY_OPERATOR);
						}
						default -> throw new IllegalArgumentException("Unknown TokenType:" + prevNA.getType());
						}
					}
				}
				case NUMBER, CLOSE_PARENTHESIS -> token.setType(TokenType.BINARY_OPERATOR);
				case OPEN_PARENTHESIS, BINARY_OPERATOR -> token.setType(TokenType.UNARY_OPERATOR);
				case UNARY_OPERATOR -> {
					if(isLeftAssociative(prevToken)) {
						token.setType(TokenType.BINARY_OPERATOR);
					}
					else {
						assert isRightAssociative(prevToken);
						token.setType(TokenType.UNARY_OPERATOR);
					}
				}
				default -> throw new IllegalArgumentException("Invalid expression");
				}
			}
		}
		assert token.getType() == TokenType.UNARY_OPERATOR || token.getType() == TokenType.BINARY_OPERATOR;
	}
	
	/**<p>Given the list of tokens and the index of a token that is an absolute value bar, this method
	 * figures out whether it is an opening or closing absolute value bar and updates its {@link TokenType type} (via {@link Token#setType(TokenType)}) accordingly.</p>
	 * <p>Precondition: {@code tokens.get(i).getType() == TokenType.ABSOULTE_VALUE_BAR}</p>
	 */
	private static void figureOutTypeOfAbsoluteValueBar(List<Token> tokens, int i) {
		Token token = tokens.get(i);
		assert token.getType() == TokenType.ABSOLUTE_VALUE_BAR;
		if(i == 0)
			token.setType(TokenType.OPEN_ABSOLUTE_VALUE_BAR);
		else if(i == tokens.size() - 1)
			token.setType(TokenType.CLOSE_ABSOLUTE_VALUE_BAR);
		else {
			Token prev = tokens.get(i - 1);
			switch(prev.getType()) {
			case OPEN_ABSOLUTE_VALUE_BAR, OPEN_PARENTHESIS, BINARY_OPERATOR -> token.setType(TokenType.OPEN_ABSOLUTE_VALUE_BAR);
			case CLOSE_ABSOLUTE_VALUE_BAR, CLOSE_PARENTHESIS, NUMBER -> token.setType(TokenType.CLOSE_ABSOLUTE_VALUE_BAR);
			case UNARY_OPERATOR -> {
				if(isLeftAssociativeUnaryOperator(prev)) {
					token.setType(TokenType.CLOSE_ABSOLUTE_VALUE_BAR);
				}
				else {
					token.setType(TokenType.OPEN_ABSOLUTE_VALUE_BAR);
				}
			}
			default -> throw new IllegalArgumentException("Invalid Expression: " + prev.getType());
			}
		}
		assert token.getType() == TokenType.CLOSE_ABSOLUTE_VALUE_BAR || token.getType() == TokenType.OPEN_ABSOLUTE_VALUE_BAR;
	}
	
	private static int endOfComplexNumber(final String str, final int startIndex) {
		if(str.charAt(startIndex) == 'i') {
			return startIndex + 1;
		}
		int ind = startIndex;
		boolean decimalPointFound = false;
		if(isDecimalPoint(str.charAt(ind))) {
			decimalPointFound = true;
			ind++;
		}
		while(ind < str.length()) {
			if(isDecimalPoint(str.charAt(ind))) {
				if(decimalPointFound)
					return -1;
				decimalPointFound = true;
				ind++;
			}
			else if(isDigit(str.charAt(ind))) {
				ind++;
			}
			else {
				break;
			}
		}
		if(ind < str.length()) {
			if(str.charAt(ind) == 'i') {
				ind++;
			}
		}
		return ind;
	}
	
	/**
	 * Converts the {@link List} of infix {@link Token Tokens} to a list of postfix tokens. A token with the type of {@link TokenType#ABSOLUTE_VALUE_BAR}
	 * in the returned list should be treated, when the postfix tokens are being evaluated, as a unary operator that takes the absolute value of the previous number.
	 * @param infixTokens
	 * @return
	 */
	private static List<Token> toPostfix(List<Token> infixTokens) {
		List<Token> postfixTokens = new ArrayList<>();
		Stack<Token> opStack = new Stack<>();
		for(Token token : infixTokens) {
			switch(token.getType()) {
			case NUMBER -> postfixTokens.add(token);
			case OPEN_PARENTHESIS, OPEN_ABSOLUTE_VALUE_BAR -> opStack.push(token);
			case UNARY_OPERATOR, BINARY_OPERATOR -> {
				while(!opStack.isEmpty() && !opStack.peek().getType().isOpeningSeparator() && 
					(
						precedenceOfOperator(opStack.peek()) > precedenceOfOperator(token) ||
						(precedenceOfOperator(opStack.peek()) == precedenceOfOperator(token) && isLeftAssociative(token))
					)
				) {
					postfixTokens.add(opStack.pop());
				}
				opStack.push(token);
			}
			case CLOSE_PARENTHESIS -> {
				while(opStack.peek().getType() != TokenType.OPEN_PARENTHESIS)
					postfixTokens.add(opStack.pop());
				opStack.pop();
			}
			case CLOSE_ABSOLUTE_VALUE_BAR -> {
				while(opStack.peek().getType() != TokenType.OPEN_ABSOLUTE_VALUE_BAR)
					postfixTokens.add(opStack.pop());
				opStack.pop();
				postfixTokens.add(new Token("|", TokenType.ABSOLUTE_VALUE_BAR));
			}
			default -> throw new IllegalArgumentException("Invalid Expression");
			}
		}
		while(!opStack.isEmpty())
			postfixTokens.add(opStack.pop());
		return postfixTokens;
	}
	
	/**
	 * @param postfixTokens
	 * @return
	 */
	private static Expression makeTree(List<Token> postfixTokens) {
		Stack<Expression> stack = new Stack<>();
		for(Token token : postfixTokens) {
			switch(token.getType()) {
			case NUMBER -> stack.push(new ConstantExpression(new Complex(token.getRepresentation())));
			case ABSOLUTE_VALUE_BAR -> stack.push(new AbsoluteValueOperator(stack.pop()));
			case BINARY_OPERATOR -> {
				Expression b = stack.pop(), a = stack.pop();
				stack.push(fromOp(token, a, b));
			}
			case UNARY_OPERATOR -> stack.push(fromOp(token, stack.pop()));
			default -> throw new IllegalArgumentException("Invalid expression");
			}
		}
		if(stack.size() != 1)
			throw new IllegalArgumentException("Invalid expression, Postfix tokens were: " + postfixTokens);
		return stack.pop();
	}
	
	private static int precedenceOfOperator(final Token token) {
		if(token.getType() == TokenType.UNARY_OPERATOR)
			return precedenceOfUnaryOperator(token);
		else if(token.getType() == TokenType.BINARY_OPERATOR)
			return precedenceOfBinaryOperator(token);
		throw new IllegalArgumentException("The given token is not an operator");
	}
	
	private static int precedenceOfUnaryOperator(final Token token) {
		return unaryPrecedence.get(token.getRepresentation());
	}
	
	private static int precedenceOfBinaryOperator(final Token token) {
		return binaryPrecedence.get(token.getRepresentation());
	}
	
	private static boolean isDigit(char c) {
		return c >= '0' && c <= '9';
	}
	
	private static boolean isDecimalPoint(char c) {
		return c == DECIMAL_POINT;
	}
	
	/**
	 * @throws IllegalArgumentException if the operator denoted by {@code operatorAsString} could not be a unary operator.
	 */
	private static boolean wouldBeRightAssociativeIfItWereAUnaryOperator(String operatorAsString) {
		if(unaryAssociativities.containsKey(operatorAsString))
			return unaryAssociativities.get(operatorAsString).isRight();
		throw new IllegalArgumentException(operatorAsString + " could not be a unary operator");
	}
	
	/**
	 * @throws IllegalArgumentException if the operator denoted by {@code operatorAsString} could not be a unary operator.
	 */
	private static boolean wouldBeLeftAssociativeIfItWereAUnaryOperator(String operatorAsString) {
		if(unaryAssociativities.containsKey(operatorAsString))
			return unaryAssociativities.get(operatorAsString).isLeft();
		throw new IllegalArgumentException(operatorAsString + " could not be a unary operator");
	}
	
	private static boolean isLeftAssociative(Token token) {
		return 	isLeftAssociativeBinaryOperator(token) || isLeftAssociativeUnaryOperator(token);
	}

	private static boolean isLeftAssociativeUnaryOperator(Token token) {
		return token.getType() == TokenType.UNARY_OPERATOR && unaryAssociativities.get(token.getRepresentation()).isLeft();
	}

	private static boolean isLeftAssociativeBinaryOperator(Token token) {
		return token.getType() == TokenType.BINARY_OPERATOR && binaryAssociativities.get(token.getRepresentation()).isLeft();
	}
	
	private static boolean isRightAssociative(Token token) {
		return 	isRightAssociativeBinaryOperator(token) ||
				isRightAssociativeUnaryOperator(token);
	}

	private static boolean isRightAssociativeUnaryOperator(Token token) {
		return token.getType() == TokenType.UNARY_OPERATOR && unaryAssociativities.get(token.getRepresentation()).isRight();
	}

	private static boolean isRightAssociativeBinaryOperator(Token token) {
		return token.getType() == TokenType.BINARY_OPERATOR && binaryAssociativities.get(token.getRepresentation()).isRight();
	}
	
	private static boolean isOperator(final String s) {
		final boolean result = isBinaryOperator(s) || isUnaryOperator(s);
//		System.out.printf("\tisOperator(%s) == %b%n", s, result);
		return result;
	}
	
	private static boolean isBinaryOperator(final String s) {
		return binaryPrecedence.containsKey(s);
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
	
	private static boolean isGroupingSeparator(String s) {
		return isParenthesis(s) || isAbsoluteValueBar(s);
	}
	
	private static boolean isAbsoluteValueBar(String s) {
		return "|".equals(s);
	}
	
	private static boolean isParenthesis(String s) {
		return isOpenParenthesis(s) || isCloseParenthesis(s);
	}
	
	private static boolean isOpenParenthesis(String s) {
		return "(".equals(s);
	}
	
	private static boolean isCloseParenthesis(String s) {
		return ")".equals(s);
	}
	
	
	private static UnaryOperator fromOp(final Token operator, final Expression expr) {
		return unaryFactories.get(operator.getRepresentation()).apply(expr);
	}
	
	private static BinaryOperator fromOp(final Token operator, final Expression left, final Expression right) {
		return binaryFactories.get(operator.getRepresentation()).apply(left, right);
	}
}