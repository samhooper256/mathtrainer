package problems;

import java.util.*;
import java.util.stream.*;

import math.Evaluator;
import utils.IntList;

/**
 * @author Sam Hooper
 *
 */
public class IntAddSubtract implements Problem {
	
	private static final List<String> OPS = List.of("-", "+");
	private final long result;
	private final String display;
	
	/**
	 * max and min digit counts are inclusive bounds.
	 */
	public IntAddSubtract(int termCount, int minDigits, int maxDigits) {
		String expression = Problem.makeExpr(termCount, minDigits, maxDigits, OPS);
		result = Evaluator.evaluateAsLongOrThrow(expression);
		display = Problem.prettyExpression(expression);
	}
	
	@Override
	public String displayString() {
		return display;
	}
	
	@Override
	public String answerAsString() {
		return String.valueOf(result);
	}

	@Override
	public boolean isCorrect(String input) {
		return Problem.isInteger(input) && Integer.parseInt(input) == result;
	}
}
