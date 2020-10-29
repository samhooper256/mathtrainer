package base;

import java.util.*;

/**
 * @author Sam Hooper
 *
 */
public class FixedBooleanQueue extends AbstractFixedQueue {
	
	private final boolean[] elements;
	
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
	public String toString() {
		StringJoiner j = new StringJoiner(",", "[", "]");
		for(int h = head, i = 0; i < size; i++, h = ((h + 1) % capacity))
			j.add(elements[h] ? "T" : "F");
		return j.toString();
	}
}
