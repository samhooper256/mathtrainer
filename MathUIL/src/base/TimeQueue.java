package base;

/**
 * @author Sam Hooper
 *
 */
public class TimeQueue {
	
	private double[] elements;
	private final int capacity;
	private int size;
	private int head, tail;
	
	public TimeQueue(final int capacity) {
		this.capacity = capacity;
		this.elements = new double[this.capacity];
		this.size = 0;
	}
	
	public void add(double item) {
		if(size == elements.length) {
			
		}
	}
}
