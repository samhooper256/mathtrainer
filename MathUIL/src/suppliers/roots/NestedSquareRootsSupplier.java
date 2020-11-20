package suppliers.roots;

import static problems.Problem.*;
import static suppliers.NamedBooleanRef.*;
import static suppliers.NamedIntRange.*;

import java.math.*;
import java.util.*;
import java.util.regex.Pattern;

import math.Utils;
import problems.*;
import suppliers.*;
import utils.*;

/**
 * @author Sam Hooper
 *
 */
public class NestedSquareRootsSupplier extends SettingsProblemSupplier {
	private static final RangeStore VALUE = RangeStore.of(1, 10, 3, 6);
	private static final List<Integer> SQUARES; //squares of the numbers from 1 to 100
	
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
		}
		
		private abstract class TwoChildNode extends Node {
			protected Node first, second;
			
			public TwoChildNode(final Node parent, final Node left, final Node right) {
				super(parent);
				this.first = left;
				this.first.parent = this;
				this.second = right;
				this.second.parent = this;
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
				throw new IllegalArgumentException();
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
				if(oldChild == radicand) {
					radicand = newChild;
					newChild.parent = this;
				}
				throw new IllegalArgumentException();
			}
			
			@Override
			public String toString() {
				return String.format("sqrt(%s)", radicand.toString());
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
			return ComplexValued.of(display, null, 1.25); //TODO get result
		}
		
		private void complicate() {
			List<Const> consts = getConsts(root);
			//Assumes all nodes' parent pointers are accurate.
			Const chosen = consts.get(Problem.RAND.nextInt(consts.size()));
			complicateConst(chosen);
		}
		
		private void complicateConst(final Const node) {
			//Ways to complicate:
			/* 1. Turn the const into a square root of its square
			 * 2. Split it into a multiplication, addition, subtraction 
			 * */
			final BigDecimal val = node.eval();
			final Node parent = node.parent;
			final boolean isInt = Utils.isInteger(val);
			int intVal = Integer.MAX_VALUE;
			if(isInt) {
				intVal = val.intValueExact();
				if(intVal <= MAX_MEMORIZED_SQUARE && Math.random() <= 0.5) {
					Node newConst = new Const(null, BigDecimal.valueOf(intVal * intVal));
					Node newNode = new Sqrt(parent, newConst);
					parent.setChild(node, newNode);
					return;
				}
			}
			int chance = isInt ? Problem.RAND.nextInt(3) : Problem.RAND.nextInt(2);
			if(chance == 0) { //Addition
				
			}
			else if(chance == 1) { //Subtraction
				
			}
			else { //Multipication
				IntList facs = Utils.factorsUnsorted(intVal);
				int factor = facs.get(Problem.RAND.nextInt(facs.size()));
				Node leftConst = new Const(null, BigDecimal.valueOf(factor));
				Node rightConst = new Const(null, BigDecimal.valueOf(intVal / factor));
				Node newNode = new Multiplication(parent, leftConst, rightConst);
				parent.setChild(node, newNode);
			}
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
		
		
	}
	
	private static final String OPEN_MN = "<mn>";

	private static final String CLOSE_MN = "</mn>";
	
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
