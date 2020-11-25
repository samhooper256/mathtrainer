package problems;

import java.math.*;
import java.util.regex.Pattern;

import math.*;

/**
 * @author Sam Hooper
 *
 */
public class Prettifier {
	
	private static final String OPENING_MATH_TAG = "<math>";
	private static final String CLOSING_MATH_TAG = "</math>";
	
	public static final String PI_HTML = "<mi>&#x3C0;</mi>";
	public static final String E_HTML = "<i>e</i>";
	public static final String I_HTML = "<mi mathvariant=\"bold\">i</mi>";
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
	
	public static String prettyOperatorRepresentation(final String operatorAsString) {
		if("*".equals(operatorAsString))
			return "×";
		if("/".equals(operatorAsString))
			return "÷";
		return operatorAsString;
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
	/**
	 * Strips any unnecessary zeros from after the decimal point in the {@code String} representation of {@code obj}. Returns {@code obj.toString()}
	 * if {@code obj.toString()} does not contain a decimal point ('.'). Otherwise, if the number is zero in any form (such as "0", "0.0", ".00", etc),
	 * returns "0". Behavior is undefined if {@code obj.toString()} contains more than one decimal point.
	 * @return
	 */
	public static String stripTrailingZeros(Object obj) {
		String number = obj.toString();
		int dotIndex = number.indexOf('.');
		if(dotIndex < 0)
			return number;
		int last0 = number.length();
		while(number.charAt(last0 - 1) == '0')
			last0--;
		if(number.charAt(last0 - 1) == '.')
			if(last0 == 1)
				return "0";
			else
				return number.substring(0, dotIndex);
		return number.substring(0, last0);
	}
	
	/**
	 * The returned {@code String} does not have {@code <math>} tags.
	 */
	public static String num(String num) {
		return "<mn>" + num + "</mn>";
	}
	
	/**
	 * The returned {@code String} does not have {@code <math>} tags.
	 */
	public static String num(final int num) {
		return num(Integer.toString(num));
	}
	
	/**
	 * The returned {@code String} does not have {@code <math>} tags.
	 */
	public static String num(final BigInteger num) {
		return num(stripTrailingZeros(num));
	}
	
	/**
	 * The returned {@code String} does not have {@code <math>} tags.
	 */
	public static String num(final BigDecimal num) {
		return num(stripTrailingZeros(num));
	}
	
	/**
	 * The returned {@code String} does not have {@code <math>} tags.
	 */
	public static String num(final Complex num) {
		if(BigNumbers.isZero(num.imaginaryPart()))
			return num(num.realPart());
		else if(BigNumbers.isZero(num.realPart()))
			return num(num.imaginaryPart());
		return num(num.realPart()) + op("+") + num(num.imaginaryPart());
	}
	
	/**
	 * The returned {@code String} does not have {@code <math>} tags.
	 */
	public static String sqrt(final String expr) {
		return "<msqrt>" + expr + "</msqrt>";
	}
	
	/**
	 * The returned {@code String} does not have {@code <math>} tags.
	 */
	public static String sqrt(final int num) {
		return sqrt(num(num));
	}
	
	/**
	 * The returned {@code String} does not have {@code <math>} tags.
	 */
	public static String op(final String op) {
		return "<mo>" + prettyOperatorRepresentation(op) + "</mo>";
	}
	
	/**
	 * The returned {@code String} does not have {@code <math>} tags.
	 */
	public static String op(final char op) {
		return op(Character.toString(op));
	}
	
	/**
	 * The returned {@code String} does not have {@code <math>} tags.
	 */
	public static String variable(final char variable) {
		return "<mi>" + variable + "</mi>";
	}
	
	/**
	 * The returned {@code String} does not have {@code <math>} tags.
	 */
	public static String frac(final BigFraction f) {
		if(f.getDenominator().compareTo(BigInteger.ONE) == 0)
			return num(f.getNumerator());
		return String.format("<mfrac>%s%s</mfrac>", num(f.getNumerator()), num(f.getDenominator()));
	}
	
	/**
	 * The returned {@code String} does not have {@code <math>} tags.
	 */ 
	public static String mixed(final MixedNumber mixedNumber) {
		if(mixedNumber.getFractionalPart().isZero())
			return num(mixedNumber.getIntegralPart());
		if(BigNumbers.isZero(mixedNumber.getIntegralPart()))
			return frac(mixedNumber.getFractionalPart());
		return String.format("%s%s", num(mixedNumber.getIntegralPart()), frac(mixedNumber.getFractionalPart()));
	}
	
	public static String repeating(final RepeatingDecimal repeatingDecimal) {
		final String term = stripTrailingZeros(repeatingDecimal.getTerminatingPart());
		return String.format("<mn>%s</mn><menclose notation=\"top\"><mn>%s</mn></menclose>",
				term + (!term.contains(".") ? "." : ""), repeatingDecimal.getRepeatingPart());
	}
	
	public static String base(final String number, final int base) {
		return String.format("<msub><mn>%s</mn><mn>%d</mn></msub>", number, base);
	}
	
	public static String pow(String text, final int power) {
		return String.format("<msup>%s<mn>%d</mn></msup>", text, power);
	}
	
	/**
	 * Returns a greater-than-or-equal-to sign, formatted in MathML (the returned {@code String} does not contain {@code <math>} tags).
	 */
	public static String ge() {
		return "<mo>&#x2265;</mo>";
	}
	
	/**
	 * Returns a less-than-or-equal-to sign, formatted in MathML (the returned {@code String} does not contain {@code <math>} tags).
	 */
	public static String le() {
		return "<mo>&#x2264;</mo>";
	}
	
	/**
	 * Returns a greater-than sign, formatted in MathML (the returned {@code String} does not contain {@code <math>} tags).
	 */
	public static String gt() {
		return "<mo>&gt;</mo>";
	}
	
	/**
	 * Returns a less-than sign, formatted in MathML (the returned {@code String} does not contain {@code <math>} tags).
	 */
	public static String lt() {
		return "<mo>&lt;</mo>";
	}
	
	/**
	 * Returns a MathML-formatted polynomial of degree {@code (coefficients.length - 1)} where the coefficients are
	 * given in left to right order in the {@code coefficients} array (that is, the coefficient of the variable with
	 * the highest power is first). For example, <code>polynomial('x', new int[] {1, 2, -3})</code> would return
	 * (a MathML formatted version of) "x^2+2x-3". The returned {@link String} does not have any {@code <math>} tags.
	 */
	public static String polynomial(char variable, final int... coefficients) {
		if(coefficients.length == 0)
			throw new IllegalArgumentException("coefficients.length == 0");
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < coefficients.length; i++) {
			int co = coefficients[i];
			if(co == 0) continue;
			int coAbs = Math.abs(co);
			int exp = coefficients.length - i - 1;
			if(co > 0 && sb.length() > 0)
				sb.append(op('+'));
			else if(co < 0)
				sb.append(op('-'));
			if(exp == 0 || coAbs != 1)
				sb.append(num(coAbs));
			if(exp > 0) {
				if(exp > 1)
					sb.append(pow(variable(variable), exp));
				else
					sb.append(variable(variable));
			}
		}
		return sb.toString();
	}
	
	/**
	 * Equivalent to: <pre><code>polynomial(variable, coefficients) + op('=') + num(0)}</code></pre>
	 */
	public static String polynomialEqualsZero(char variable, final int... coefficients) {
		return polynomial(variable, coefficients) + op('=') + num(0);
	}
}
