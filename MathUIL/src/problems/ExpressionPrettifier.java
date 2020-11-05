package problems;

import java.util.regex.Pattern;

/**
 * @author Sam Hooper
 *
 */
class ExpressionPrettifier {
	
	private static final Pattern WHITESPACE = Pattern.compile("\\s+");
	public static String pretty(final String expression) {
//		System.out.printf("Entered pretty: \"%s\"%n", expression);
		String pretty = WHITESPACE.matcher(expression).replaceAll("");
//		System.out.printf("pretty: \"%s\"%n", pretty);
		pretty = pretty.replaceAll("[-^*/+]", " $0 ").replace('*', '×').replace('/', '÷');
//		System.out.printf("pretty: \"%s\"%n", pretty);
		pretty = pretty.replaceAll("(\\d+) \\^ (\\d+)", "$1<sup>$2</sup>");
//		System.out.printf("pretty: \"%s\"%n", pretty);
		return pretty;
	}
}
