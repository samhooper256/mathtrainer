package problems;

import java.util.regex.Pattern;

/**
 * @author Sam Hooper
 *
 */
public class Prettifier {
	
	public static final String PI_HTML = "&#960;";
	public static final String E_HTML = "<i>e</i>";
	private static final Pattern WHITESPACE = Pattern.compile("\\s+");
	
	public static String stripMath(String html) {
		if(html.startsWith("<math>") && html.endsWith("</math>"))
			return html.substring(6, html.length() - 7);
		return html;
	}
	
	public static String ensureMath(String html) {
		if(html.startsWith("<math>"))
			if(html.endsWith("</math>"))
				return html;
			else
				throw new IllegalArgumentException("The given HTML snippet starts with a math tag but does not end with one.");
		else
			if(html.endsWith("</math>"))
				throw new IllegalArgumentException("The given HTML snippet ends with a math tag but does not start with one.");
			else
				return "<math>" + html + "</math>";
	}
	
	public static String pretty(final String expression) {
		String pretty = WHITESPACE.matcher(expression).replaceAll("");
		pretty = pretty.replaceAll("[-^*/+]", " $0 ").replace('*', '×').replace('/', '÷');
		pretty = pretty.replaceAll("(\\d+) \\^ (\\d+)", "$1<sup>$2</sup>");
		return pretty;
	}
}
