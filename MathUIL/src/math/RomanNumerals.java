package math;

import java.util.Scanner;

/**
 * @author Sam Hooper
 *
 */
public class RomanNumerals {
	
	public static void main(String[] args) {
		
		Scanner in = new Scanner(System.in);
		while(in.hasNextLine()) {
			String line = in.nextLine();
			if(line.charAt(0) <= '9') {
				System.out.println(toRomanNumerals(Integer.parseInt(line)));
			}
			else {
				System.out.println(fromRomanNumerals(line));
			}
		}
	}
	
	private RomanNumerals() {}
	
	private static final String[] 	HUNDREDS = {"", "C", "CC", "CCC", "CD", "D", "DC", "DCC", "DCCC", "CM"},
									TENS = {"", "X", "XX", "XXX", "XL", "L", "LX", "LXX", "LXXX", "XC"},
									ONES = {"", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX"};
	
	public static final int MAX_VALUE = 4000;
	public static final int MIN_VALUE = 1;
	
	public static String toRomanNumerals(int n) {
		ensureInBounds(n);
		StringBuilder result = new StringBuilder();
		int thousands = n / 1000;
		result.append("M".repeat(thousands));
		n -= thousands * 1000;
		int hundreds = n / 100;
		result.append(getHundreds(hundreds));
		n -= hundreds * 100;
		int tens = n / 10;
		result.append(getTens(tens));
		n -= tens * 10;
		result.append(getOnes(n));
		return result.toString();
	}
	
	/** The given String is assumed to be an accurate Roman numeral.*/
	public static int fromRomanNumerals(final String roman) {
		int total = 0;
		for(int i = 0; i < roman.length();) {
			final String sub = roman.substring(i, i + 1);
			int value = value(sub);
			if(i < roman.length() - 1) {
				int nextValue = value(roman.substring(i + 1, i + 2));
				if(value < nextValue) {
					total += nextValue - value;
					i += 2;
					continue;
				}
			}
			total += value;
			i += 1;
		}
		return total;
	}
	
	/** Precondition: 0 <= n <= 9 */
	private static String getHundreds(int n) {
		return HUNDREDS[n];
	}
	
	/** Precondition: 0 <= n <= 9 */
	private static String getTens(int n) {
		return TENS[n];
	}
	
	/** Precondition: 0 <= n <= 9 */
	private static String getOnes(int n) {
		return ONES[n];
	}
	
	private static int value(String s) {
		return switch(s) {
		case "I" -> 1;
		case "V" -> 5;
		case "X" -> 10;
		case "L" -> 50;
		case "C" -> 100;
		case "D" -> 500;
		case "M" -> 1000;
		default -> throw new IllegalArgumentException(s + " is unrecognized.");
		};
	}
	private static void ensureInBounds(int n) {
		if(n < MIN_VALUE || n > MAX_VALUE)
			throw new IllegalArgumentException("n must be between " + MIN_VALUE + " and " + MAX_VALUE);
	}
}
