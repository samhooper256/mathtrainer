package suppliers.sets;

import java.util.*;
import java.util.stream.*;

import problems.Problem;

/**
 * @author Sam Hooper
 *
 */
public final class SetSupUtils {
	/*
	public static void main(String[] args) {
		System.out.println(letterList(26));
		System.out.println(letterList(25));
		System.out.println(letterList(24));
		System.out.println(letterList(23));
	}
	*/
	private SetSupUtils() {}

	private static final char[] LOWERCASE_LETTERS = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
	
	/**
	 * Returns a {@link Set} of {@code size} randomly selected lowercase English letters.
	 */
	public static Set<Character> letterSet(final int size) {
		return new HashSet<>(letterList(size));
	}
	/**
	 * Returns a {@link List} of {@code size} distinct lowercase English letters in a random order.
	 */
	public static List<Character> letterList(final int size) {
		if(size < 0)
			throw new IllegalArgumentException("size < 0");
		if(size > LOWERCASE_LETTERS.length)
			throw new IllegalArgumentException("There are not" + size + "different letters");
		char[] chars = Arrays.copyOf(LOWERCASE_LETTERS, LOWERCASE_LETTERS.length);
		int max = chars.length;
		List<Character> list = new ArrayList<>(size);
		for(int i = 0; i < size; i++) {
			int index = Problem.intExclusive(max);
			list.add(chars[index]);
			char temp = chars[index];
			chars[index] = chars[max - 1];
			chars[max - 1] = temp;
			max--;
		}
		return list;
	}
}
