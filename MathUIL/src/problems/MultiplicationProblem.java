package problems;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Sam Hooper
 *
 */
public class MultiplicationProblem implements Problem {
	
	private final List<Integer> terms;
	private final int result;
	
	/**
	 * max and min digit counts are inclusive bounds.
	 */
	public MultiplicationProblem(int termCount, int minDigits, int maxDigits) {
		terms = new ArrayList<>(termCount);
		for(int i = 0; i < termCount; i++)
			terms.add(Problem.intWithDigits((int) (Math.random() * (maxDigits + 1 - minDigits) + minDigits)));
		result = Problem.product(terms);
	}
	
	@Override
	public String displayString() {
		return terms.stream().map(String::valueOf).collect(Collectors.joining(" × "));
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
