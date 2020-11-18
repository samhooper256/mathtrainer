package utils;

/**
 * @author Sam Hooper
 *
 */
public abstract class AbstractFixedQueue {
	
	protected int capacity;
	protected int size, head, tail;
	
	/**
	 * @throws IllegalArgumentException if {@code capacity <= 0}.
	 */
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
	 * Returns the decremented value of {@code num}, mod {@link #capacity}.
	 */
	protected int decced(int num) {
		if(--num < 0)
			num = capacity - 1;
		return num;
	}
	/**
	 * decrements {@link #tail} mod {@link #capacity} and returns the decremented value.
	 */
	protected int decTail() {
		if(--tail < 0)
			tail = capacity - 1;
		return tail;
	}
	
	public void clear() {
		head = tail;
		size = 0;
	}
	
	public int getCapacity() {
		return capacity;
	}
	
	/** Increases the {@link #getCapacity() capacity} of this {@link AbstractFixedQueue} to {@code newCapacity}.
	 * @throws IllegalArgumentException if {@code newCapacity <= getCapacity()}.
	 * */
	public void increaseCapacityTo(int newCapacity) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Decreases the {@link #getCapacity() capacity} of this {@link AbstractFixedQueue} to {@code newCapacity}.
	 * @throws IllegalArgumentException if {@code newCapacity >= getCapacity()}.
	 */
	public void clearAndDecreaseCapacityTo(int newCapacity) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Changes the {@link #getCapacity() capacity} of this {@link AbstractFixedQueue} to {@code newCapacity}, calling
	 * either {@link #increaseCapacityTo(int)} or {@link #clearAndDecreaseCapacityTo(int)} as appropriate. {@code newCapacity}
	 * must be strictly greater than {@code 0}.
	 */
	public void changeCapacityTo(final int newCapacity) {
		if(newCapacity == getCapacity())
			return;
		else if(newCapacity > getCapacity())
			increaseCapacityTo(newCapacity);
		else
			clearAndDecreaseCapacityTo(newCapacity);
	}
}
