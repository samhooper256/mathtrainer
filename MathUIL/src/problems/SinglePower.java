package problems;

/**
 * @author Sam Hooper
 *
 */
public class SinglePower implements Problem {
	
	private final int result;
	private final String display;
	
	public SinglePower(final int value, final int power) {
		result = (int) Math.pow(value, power);
		display = value + "<sup>" + power + "</sup>";
	}

	@Override
	public String displayString() {
		return display;
	}

	@Override
	public boolean isCorrect(String input) {
		return Problem.isInteger(input) && Integer.parseInt(input) == result;
	}

	@Override
	public String answerAsString() {
		return String.valueOf(result);
	}
	
}
