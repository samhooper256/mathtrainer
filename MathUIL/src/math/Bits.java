package math;

/**
 * @author Sam Hooper
 *
 */
public class Bits {
	
	private Bits() {}
	
	public static int countSetBits(int n) { 
        int count = 0; 
        while (n > 0) { 
            n &= (n - 1); 
            count++; 
        } 
        return count; 
    }
	
	public static boolean isPowerOf2(int n) {
		return (n & (n - 1)) == 0;
	}
}
