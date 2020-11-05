package problems;

import java.util.stream.Collectors;

import utils.*;

/**
 * @author Sam Hooper
 *
 */
public class IntegerMultiplication implements Problem {
	
	private final IntList terms;
	private final long result;
	
	/**
	 * max and min digit counts are inclusive bounds.
	 */
	public IntegerMultiplication(int termCount, int minDigits, int maxDigits) {
		terms = new IntList(termCount);
		for(int i = 0; i < termCount; i++)
			terms.add(Problem.intWithDigits((int) (Math.random() * (maxDigits + 1 - minDigits) + minDigits)));
		result = Problem.product(terms);
	}
	
	private IntegerMultiplication(int[] termsArr) {
		this.terms = new IntList(termsArr);
		this.result = Problem.product(this.terms);
	}
	
	public static IntegerMultiplication fromTerms(int... terms) {
		return new IntegerMultiplication(terms);
	}
	
	@Override
	public String displayString() {
		return terms.stream().mapToObj(String::valueOf).collect(Collectors.joining(" × "));
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
