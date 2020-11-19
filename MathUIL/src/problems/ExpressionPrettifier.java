package problems;

import java.util.regex.Pattern;

/**
 * @author Sam Hooper
 *
 */
public class ExpressionPrettifier {
	
	public static final String PI_HTML = "&#960;";
	private static final Pattern WHITESPACE = Pattern.compile("\\s+");
	public static String pretty(final String expression) {
		String pretty = WHITESPACE.matcher(expression).replaceAll("");
		pretty = pretty.replaceAll("[-^*/+]", " $0 ").replace('*', '×').replace('/', '÷');
		pretty = pretty.replaceAll("(\\d+) \\^ (\\d+)", "$1<sup>$2</sup>");
		return pretty;
	}
}
