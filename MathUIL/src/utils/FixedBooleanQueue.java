package utils;

import java.util.*;

/**
 * @author Sam Hooper
 *
 */
public class FixedBooleanQueue extends AbstractFixedQueue {
	
	private boolean[] elements;
	
	private int trues;
	
	public FixedBooleanQueue(final int capacity) {
		super(capacity);
		this.elements = new boolean[this.capacity + 1];
	}
	
	/**
	 * Adds the given {@code double} to the front (first) in this {@link FixedBooleanQueue}.
	 */
	public void addFirst(final boolean item) {
		if(size == capacity)
			removeLast();
		elements[decHead()] = item;
		size++;
		added(item);
	}
	
	/**
	 * Called at the end of any add method. The argument is the {@code double} that was added. When this method is called,
	 * size must have already been incremented. This method updates the {@link #average} and {@link #total} values.
	 */
	private void added(boolean item) {
		if(item)
			trues++;
	}
	
	/**
	 * Removes and returns the element at the back (last) in this {@link FixedBooleanQueue}.
	 * @throws IllegalStateException if empty
	 */
	public boolean removeLast() {
		final boolean item = getLast(); //throws the IllegalStateException if necessary
		decTail();
		size--;
		removed(item);
		return item;
	}
	
	/**
	 * Called at the end of any remove method. The argument is the {@code double} that was removed. When this method is called,
	 * size must have already been decremented. This method updates the {@link #average} and {@link #total} values.
	 */
	private void removed(boolean item) {
		if(item)
			trues--;
	}
	
	public boolean getLast() {
		if(isEmpty())
			throw new NoSuchElementException();
		return elements[decced(tail)];
	}
	
	public int trues() {
		return trues;
	}
	
	public int falses() {
		return size() - trues;
	}
	
	/**
	 * Between {@code 0} and {@code 1} (inclusive).
	 */
	public double truthProportion() {
		return 1.0 * trues / size;
	}
	
	
	@Override
	public void clear() {
		super.clear();
		trues = 0;
	}

	@Override
	public String toString() {
		StringJoiner j = new StringJoiner(", ", "[", "]");
		for(BooleanIterator itr = primitiveIterator(); itr.hasNext();)
			j.add(String.valueOf(itr.nextBoolean()));
		return j.toString();
	}
	
	@Override
	public void clearAndDecreaseCapacityTo(final int newCapacity) {
		if(newCapacity >= getCapacity())
			throw new IllegalArgumentException("New capacity must be strictly less than current capacity");
		if(newCapacity <= 0)
			throw new IllegalArgumentException("Capacity must be greater than zero");
		clear();
		elements = new boolean[newCapacity + 1];
		capacity = newCapacity;
		head = tail = 0;
		
	}
	
	@Override
	public void increaseCapacityTo(final int newCapacity) {
		if(newCapacity <= getCapacity())
			throw new IllegalArgumentException("New capacity must be strictly greater than current capacity");
		boolean[] newElements = new boolean[newCapacity + 1];
		int index = 0;
		for(BooleanIterator itr = primitiveIterator(); itr.hasNext(); ) {
			newElements[index++] = itr.nextBoolean();
		}
		head = 0;
		tail = index;
		capacity = newCapacity;
		elements = newElements;
	}
	
	public BooleanIterator primitiveIterator() {
		return new BooleanIterator() {
			int i = 0;
			int h = head;
			
			@Override
			public boolean hasNext() {
				return i < size;
			}
			
			@Override
			public boolean nextBoolean() {
				boolean result = elements[h];
				i++;
				h = ((h + 1) % capacity);
				return result;
			}
		};
	}
}
