package utils;

/**
 * @author Sam Hooper
 *
 */
public class Strings {
	
	private Strings() {}
	
	public static String stripTrailing(final String str, final char c) {
		int j = str.length();
		while(j > 0 && str.charAt(j - 1) == c)
			j--;
		return str.substring(0, j);
	}
	
	public static String stripLeading(final String str, final char c) {
		int i = 0;
		while(i < str.length() && str.charAt(i) == c)
			i++;
		return str.substring(i);
	}
	
	/**
	 * Returns {@code false} if the given {@code String} {@link String#isEmpty() is empty}.
	 */
	public static boolean containsOnly(final String str, final char c) {
		if(str.length() == 0)
			return false;
		for(int i = 0; i < str.length(); i++)
			if(str.charAt(i) != c)
				return false;
		return true;
	}
	
}
