package problems;

import java.util.List;
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
	
	public static int intWithDigits(final int digits) {
		int num = 0;
		for(int i = 0, add = 1; i < digits; i++, add *= 10)
			num += DIGIT_SUPPLIER.getAsInt() * add;
		return num;
	}
	
	/**
	 * Returns {@code 0} if the given {@link List} is empty.
	 */
	public static int sum(List<Integer> terms) {
		int sum = 0;
		for(Integer i : terms)
			sum += i.intValue();
		return sum;
	}
	
	/**
	 * Returns {@code 1} if the given {@link List} is empty.
	 */
	public static int product(List<Integer> terms) {
		int product = 1;
		for(Integer i : terms)
			product *= i.intValue();
		return product;
	}
	
	String displayString();
	
	boolean isCorrect(String input);
	
	/**
	 * Returns the answer to this {@link Problem}.
	 */
	String answerAsString();
	
}
