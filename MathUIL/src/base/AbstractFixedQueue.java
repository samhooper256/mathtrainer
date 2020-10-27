package base;

/**
 * @author Sam Hooper
 *
 */
public class AbstractFixedQueue {
	
	protected final int capacity;
	protected int size;
	
	public AbstractFixedQueue(final int capacity) {
		if(capacity <= 0)
			throw new IllegalArgumentException("capacity must be positive");
		this.capacity = capacity;
		this.size = 0;
	}
	
	public int size() {
		return size;
	}
	
	public boolean isEmpty() {
		return size() == 0;
	}
}
