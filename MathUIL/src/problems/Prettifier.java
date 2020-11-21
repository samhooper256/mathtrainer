package problems;

import java.util.regex.Pattern;

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
}
