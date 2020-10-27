package base;

/**
 * @author Sam Hooper
 *
 */
public class AbstractFixedQueue {
	
	protected final int capacity;
	protected int size, head, tail;
	
	public AbstractFixedQueue(final int capacity) {
		if(capacity <= 0)
			throw new IllegalArgumentException("capacity must be positive");
		this.capacity = capacity;
		this.size = 0;
		this.head = capacity / 2;
		this.tail = head;
	}
	
	public int size() {
		return size;
	}
	
	public boolean isEmpty() {
		return size() == 0;
	}
	
	/**
	 * decrements {@link #head} mod {@link #capacity} and returns the decremented value.
	 */
	protected int decHead() {
		if(--head < 0)
			head = capacity - 1;
		return head;
	}
	
	protected int incHead() {
		if(++head >= capacity)
			head = 0;
		return head;
	}
	
	/**
	 * decrements {@link #tail} mod {@link #capacity} and returns the decremented value.
	 */
	protected int decTail() {
		if(--tail < 0)
			tail = capacity - 1;
		return tail;
	}
}
