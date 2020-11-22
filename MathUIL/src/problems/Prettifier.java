package problems;

import java.util.regex.Pattern;

import math.Fraction;

/**
 * @author Sam Hooper
 *
 */
public class Prettifier {
	
	private static final String OPENING_MATH_TAG = "<math>";
	private static final String CLOSING_MATH_TAG = "</math>";
	
	public static final String PI_HTML = "&#960;";
	public static final String E_HTML = "<i>e</i>";
	private static final Pattern WHITESPACE = Pattern.compile("\\s+");
	
	public static String stripMath(String html) {
		if(html.startsWith(OPENING_MATH_TAG) && html.endsWith(CLOSING_MATH_TAG))
			return html.substring(OPENING_MATH_TAG.length(), html.length() - CLOSING_MATH_TAG.length());
		return html;
	}
	
	public static String ensureMath(String html) {
		if(html.startsWith(OPENING_MATH_TAG))
			if(html.endsWith(CLOSING_MATH_TAG))
				return html;
			else
				throw new IllegalArgumentException("The given HTML snippet starts with a math tag but does not end with one.");
		else
			if(html.endsWith(CLOSING_MATH_TAG))
				throw new IllegalArgumentException("The given HTML snippet ends with a math tag but does not start with one.");
			else
				return OPENING_MATH_TAG + html + CLOSING_MATH_TAG;
	}
	
	public static String pretty(final String expression) {
		String pretty = WHITESPACE.matcher(expression).replaceAll("");
		pretty = pretty.replaceAll("[-^*/+]", " $0 ").replace('*', '×').replace('/', '÷');
		pretty = pretty.replaceAll("(\\d+) \\^ (\\d+)", "$1<sup>$2</sup>");
		return pretty;
	}
	
	/**
	 * <p>Returns the suffix of the given number if it were describing a position in a sequence. For example, returns "st" for 1 (because "1st" ends with "st"),
	 * "nd" for 2,"rd" for 3, "th" for 4, etc.</p>
	 * <p>{@code n} must be greater than or equal to zero. Returns "th" for 0.</p>
	 */
	public static String ordinalSuffix(int n) {
		if(n < 0)
			throw new IllegalArgumentException("n must be >= 0");
		int mod100 = n % 100;
		int mod10 = n % 10;
		if(10 <= mod100 && mod100 <= 20)
			return "th";
		if(mod10 == 1)
			return "st";
		if(mod10 == 2)
			return "nd";
		if(mod10 == 3)
			return "rd";
		return "th";
	}
	public static String stripTrailingZeros(Object obj) {
		String number = obj.toString();
		int dotIndex = number.indexOf('.');
		if(dotIndex < 0)
			return number;
		int last0 = number.length();
		while(number.charAt(last0 - 1) == '0')
			last0--;
		if(number.charAt(last0 - 1) == '.')
			return number.substring(0, dotIndex);
		
		return number.substring(0, last0);
	}
	
	public static String num(String num) {
		return "<mn>" + num + "</mn>";
	}
	
	public static String num(final int num) {
		return num(Integer.toString(num));
	}
	
	public static String sqrt(final String expr) {
		return "<msqrt>" + expr + "</msqrt>";
	}
	
	public static String sqrt(final int num) {
		return sqrt(num(num));
	}
	
	public static String op(final String op) {
		return "<mo>" + op + "</mo>";
	}
	
	public static String op(final char op) {
		return op(Character.toString(op));
	}
	
	public static String frac(final Fraction f) {
		if(f.getDenominator() == 1)
			return num(f.getNumerator());
		return String.format("<mfrac><mn>%d</mn><mn>%d</mn></mfrac>", f.getNumerator(), f.getDenominator());
	}
}
