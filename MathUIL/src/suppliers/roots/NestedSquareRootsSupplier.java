package suppliers.roots;

import static problems.Problem.*;
import static suppliers.NamedBooleanRef.*;
import static suppliers.NamedIntRange.*;


import java.math.*;
import java.util.*;
import java.util.regex.Pattern;

import math.*;
import problems.*;
import suppliers.*;
import utils.*;

/**
 * @author Sam Hooper
 *
 */
//TODO remove redundant parentheses in the resulting expression
public class NestedSquareRootsSupplier extends SettingsProblemSupplier {
	private static final RangeStore VALUE = RangeStore.of(1, 10, 3, 6);
	private static final List<Integer> SQUARES; //squares of the numbers from 1 to 100
	
	public static void main(String[] args) {
		NestedSquareRootsSupplier nsr = new NestedSquareRootsSupplier();
		final Problem prob = nsr.get();
		System.out.println(prob.displayString());
	}
	
	static {
		SQUARES = new ArrayList<>(100);
		for(int i = 1; i <= SQUARES.size(); i++) {
			SQUARES.add(i * i);
		}
	}
	
	private final NamedIntRange value = of(VALUE, "Value of expression");
	
	public NestedSquareRootsSupplier() {
		settings(value);
	}
	
	
	@Override
	public Problem get() {
		final int valueOfExpression = Problem.intInclusive(value);
		return new PMaker(valueOfExpression).make();
	}
	
	private static final MathContext PMAKER_CONTEXT = new MathContext(10);
	
	private class PMaker {
		

		private static final int MAX_MEMORIZED_SQUARE = 30;
		
		private final int voe;
		private final int voe2;
		private final int complexity;
		//no math tags
		private String display;
		private Node root;
		private abstract class Node{
			protected Node parent;
			Node(final Node parent) {
				this.parent = parent;
			}
			abstract BigDecimal eval();
			/** Updates {@code newChild's} parent pointer. */
			abstract void setChild(Node oldChild, Node newChild);
			Collection<Node> children() { return Collections.emptySet(); }
			abstract String mathMLString();
			abstract String expString();
		}
		
		private abstract class TwoChildNode extends Node {
			protected Node first, second;
			
			public TwoChildNode(final Node parent, final Node left, final Node right) {
				super(parent);
				this.first = left;
				if(this.first != null) this.first.parent = this;
				this.second = right;
				if(this.second != null) this.second.parent = this;
			}

			@Override
			void setChild(Node oldChild, Node newChild) {
				if(oldChild == first) {
					first = newChild;
					newChild.parent = this;
				}
				else if(oldChild == second) {
					second = newChild;
					newChild.parent = this;
				}
				else
					throw new IllegalArgumentException();
			}

			@Override
			Collection<Node> children() {
				return List.of(first, second);
			}
			
			
		}
		private class Sqrt extends Node {
			Node radicand;
			
			public Sqrt(final Node parent, final Node radicand) {
				super(parent);
				this.radicand = radicand;
				this.radicand.parent = this;
			}
			
			@Override BigDecimal eval() {
				return radicand.eval().sqrt(PMAKER_CONTEXT);
			}

			@Override
			Collection<Node> children() {
				return List.of(radicand);
			}

			
			@Override
			void setChild(Node oldChild, Node newChild) {
//				System.out.printf("(enter) Sqrt::setChild(oldChild.hash=%h, newChild.hash=%h), this.hash=%h, radicand=%s%n", oldChild, newChild, this, radicand);
//				if(radicand != null)
//					System.out.printf("radicand.hash=%h%n", radicand);
//				else System.out.printf("radicand is null%n");
				if(oldChild == radicand) {
					radicand = newChild;
					newChild.parent = this;
				}
				else
					throw new IllegalArgumentException();
			}
			
			@Override
			public String toString() {
				return String.format("sqrt(%s)", radicand.toString());
			}

			@Override
			String mathMLString() {
				return "<msqrt>" + radicand.mathMLString() + "</msqrt>";
			}

			@Override
			String expString() {
				return String.format("((%s)^.5)", radicand.expString());
			}
			
		}
		
		private class Addition extends TwoChildNode {
			Addition(final Node parent, final Node left, final Node right) {
				super(parent, left, right);
			}

			@Override
			BigDecimal eval() {
				return first.eval().add(second.eval(), PMAKER_CONTEXT);
			}
			
			@Override
			public String toString() {
				return String.format("(%s+%s)", first, second);
			}

			@Override
			String mathMLString() {
				return OPEN_PAR + first.mathMLString() + "<mo>+</mo>" + second.mathMLString() + CLOSE_PAR;
			}

			@Override
			String expString() {
				return String.format("(%s+%s)", first.expString(), second.expString());
			}
		}
		
		private class Subtraction extends TwoChildNode {
			Subtraction(final Node parent, final Node left, final Node right) {
				super(parent, left, right);
			}

			@Override
			BigDecimal eval() {
				return first.eval().subtract(second.eval(), PMAKER_CONTEXT);
			}
			
			@Override
			public String toString() {
				return String.format("(%s-%s)", first, second);
			}

			@Override
			String mathMLString() {
				return OPEN_PAR + first.mathMLString() + "<mo>-</mo>" + second.mathMLString() + CLOSE_PAR;
			}

			@Override
			String expString() {
				return String.format("(%s-%s)", first.expString(), second.expString());
			}
		}
		
		private class Multiplication extends TwoChildNode {
			Multiplication(final Node parent, final Node left, final Node right) {
				super(parent, left, right);
			}

			@Override
			BigDecimal eval() {
				return first.eval().multiply(second.eval(), PMAKER_CONTEXT);
			}
			@Override
			public String toString() {
				return String.format("(%s*%s)", first, second);
			}

			@Override
			String mathMLString() {
				return OPEN_PAR + first.mathMLString() + "<mo>*</mo>" + second.mathMLString() + CLOSE_PAR;
			}

			@Override
			String expString() {
				return String.format("(%s*%s)", first.expString(), second.expString());
			}
		}
		
		private class Const extends Node {
			final BigDecimal value;
			Const(final Node p, final BigDecimal value) {
				super(p);
				this.value = value;
			}
			@Override BigDecimal eval() { return value; }
			@Override
			void setChild(Node oldChild, Node newChild) {
				throw new IllegalArgumentException();
			}
			
			@Override
			public String toString() {
				return value.toString();
			}
			@Override
			String mathMLString() {
				return String.format("<mn>%s</mn>", value);
			}
			@Override
			String expString() {
				return value.toString();
			}
		}
		
		
		PMaker(final int valueOfExpression) {
			voe = valueOfExpression;
			voe2 = voe * voe;
			complexity = 4;
		}
		
		Problem make() {
			Const initialConst = new Const(null, BigDecimal.valueOf(voe2));
			root = new Sqrt(null, initialConst);
			initialConst.parent = root;
			for(int i = 0; i < complexity; i++) {
				complicate();
			}
			return ComplexValued.of(Prettifier.ensureMath(root.mathMLString()), Evaluator.evaluateAsComplex(root.expString()), 1.25); //TODO get result
		}
		
		private void complicate() {
//			System.out.printf("(enter) complicate, root=%n", root);
//			print(root);
			List<Const> consts = getConsts(root);
//			System.out.printf("consts=%s%n", consts);
			//Assumes all nodes' parent pointers are accurate.
			Const chosen = consts.get(Problem.RAND.nextInt(consts.size()));
			complicateConst(chosen);
		}
		
		private void complicateConst(final Const node) {
			//Ways to complicate:
			/* 1. Turn the const into a square root of its square
			 * 2. Split it into a multiplication, addition, subtraction 
			 * */
//			System.out.printf("\t(enter) complicateConst(node=%s) node.hash=%h%n", node, node);
			final BigDecimal val = node.eval();
			final double doubleVal = val.doubleValue();
			final Node parent = node.parent;
//			System.out.printf("parent=%s, parent.hash=%h%n", parent, parent);
			final boolean isInt = Utils.isInteger(val);
			int intVal = Integer.MAX_VALUE;
			if(isInt) {
				intVal = val.intValueExact();
				if(intVal <= MAX_MEMORIZED_SQUARE && Math.random() <= 0.5) {
					Node newConst = new Const(null, BigDecimal.valueOf(intVal * intVal));
					Node newNode = new Sqrt(parent, newConst);
//					System.out.printf("\t\tparent=%s%n", parent);
//					System.out.printf("parent.children()=%s%n", parent.children());
					parent.setChild(node, newNode);
					return;
				}
			}
			int chance = isInt ? Problem.RAND.nextInt(3) : Problem.RAND.nextInt(2);
			if(chance == 0) { //Addition
				int op1 = (int) (1 + (Math.random() * (doubleVal - 1)));
				BigDecimal bd1 = new BigDecimal(op1, PMAKER_CONTEXT);
				BigDecimal bd2 = val.subtract(bd1, PMAKER_CONTEXT);
				Node leftConst = new Const(null, bd1), rightConst = new Const(null, bd2);
				Node newNode = new Addition(parent, leftConst, rightConst);
				parent.setChild(node, newNode);
			}
			else if(chance == 1) { //Subtraction
				int op1 = (int) (1 + (Math.random() * (doubleVal - 1)));
				BigDecimal bd2 = new BigDecimal(op1, PMAKER_CONTEXT);
				BigDecimal bd1 = val.add(bd2, PMAKER_CONTEXT);
				Node leftConst = new Const(null, bd1), rightConst = new Const(null, bd2);
				Node newNode = new Subtraction(parent, leftConst, rightConst);
				parent.setChild(node, newNode);
			}
			else { //Multipication
				IntList facs = Utils.factorsUnsorted(intVal);
				int factor = facs.get(Problem.RAND.nextInt(facs.size()));
				Node leftConst = new Const(null, BigDecimal.valueOf(factor));
				Node rightConst = new Const(null, BigDecimal.valueOf(intVal / factor));
				Node newNode = new Multiplication(parent, leftConst, rightConst);
				parent.setChild(node, newNode);
			}
//			System.out.printf("\t(exit) complicateConst(node=%s)%n", node);
		}
		
		private final List<Const> getConsts(final Node node) {
			final ArrayList<Const> list = new ArrayList<>();
			getConsts(node, list);
			return list;
		}
		
		private final void getConsts(final Node node, final List<Const> list) {
			if(node instanceof Const)
				list.add((Const) node);
			else
				for(Node child : node.children())
					getConsts(child, list);
		}
		
		private void print(Node n) {
			print(n, 0);
		}
		private void print(Node n, int tabs) {
			if(n == null)
				System.out.println("\t".repeat(tabs) + "null");
			else {
				System.out.println("\t".repeat(tabs) + n.getClass().getSimpleName() + "@" + n.hashCode());
				for(Node c : n.children())
					print(c, tabs + 1);
			}
		}
	}
	
	private static final String OPEN_MN = "<mn>";

	private static final String CLOSE_MN = "</mn>";
	
	private static final String OPEN_PAR = "<mo>(</mo>", CLOSE_PAR = "<mo>)</mo>";
	private static String sqrt(String s) {
		return "<msqrt>" + s + "</msqrt>";
	}
	
	private static String sqrt(int n) {
		return sqrt(tag(n));
	}
	
	private static String tag(final int num) {
		return OPEN_MN + num + CLOSE_MN;
	}
	
}
