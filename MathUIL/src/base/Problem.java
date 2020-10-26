package base;

import java.util.function.IntSupplier;

/**
 * @author Sam Hooper
 *
 */
public interface Problem {
	
	public static final IntSupplier DIGIT_SUPPLIER = () -> (int) (Math.random() * 10);

	public static boolean isInteger(final String s) {
		if(s.length() == 0)
			return false;
		int i = s.charAt(0) == '-' ? 1 : 0;
		if(s.length() - i == 0)
			return false;
		for(int j = i; j < s.length(); j++)
			if(s.charAt(j) < '0' || s.charAt(j) > '9')
				return false;
		return true;
	}
	
	public static int randomInt(final int digits) {
		int num = 0;
		for(int i = 0, add = 1; i < digits; i++, add *= 10)
			num += DIGIT_SUPPLIER.getAsInt() * add;
		return num;
	}
	
	
	String displayString();
	
	boolean isCorrect(String input);
	
	/**
	 * Returns the answer to this {@link Problem}.
	 */
	String answerAsString();
	
}
