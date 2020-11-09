package math;

/**
 * @author Sam Hooper
 *
 */
public class Utils {
	
	private Utils() {}
	
	public static int gcd(int a, int b) {
		if(a == 0 || b == 0)
			throw new IllegalArgumentException("numbers cannot be zero");
		if(b > a) {
			int temp = a;
			a = b;
			b = temp;
		}
		while(a != b) {
			if(a > b)
				a = a - b;
			else
				b = b - a;
		}
		return a;
	}
	
	public static int lcm(int a, int b) {
		return a * b / gcd(a, b);
	}
}
