package utils;

import java.util.*;

/**
 * @author Sam Hooper
 *
 */
public class FixedDoubleQueue extends AbstractFixedQueue {
	
	private final double[] elements;
	
	private double total;
	
	public FixedDoubleQueue(final int capacity) {
		super(capacity);
		this.elements = new double[this.capacity + 1];
	}
	
	/**
	 * Adds the given {@code double} to the front (first) in this {@link FixedDoubleQueue}.
	 */
	public void addFirst(final double item) {
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
	private void added(double item) {
		total += item;
	}
	
	/**
	 * Removes and returns the element at the back (last) in this {@link FixedDoubleQueue}.
	 * @throws IllegalStateException if empty
	 */
	public double removeLast() {
		final double item = getLast(); //throws the IllegalStateException if necessary
		decTail();
		size--;
		removed(item);
		return item;
	}
	
	/**
	 * Called at the end of any remove method. The argument is the {@code double} that was removed. When this method is called,
	 * size must have already been decremented. This method updates the {@link #average} and {@link #total} values.
	 */
	private void removed(double item) {
		total -= item;
	}
	
	public double getLast() {
		if(isEmpty())
			throw new NoSuchElementException();
		return elements[decced(tail)];
	}
	
	public double total() {
		return total;
	}
	
	public double average() {
		return total / size();
	}
	
	@Override
	public void clear() {
		super.clear();
		total = 0;
	}

	@Override
	public String toString() {
		StringJoiner j = new StringJoiner(", ", "[", "]");
		for(int h = head, i = 0; i < size; i++, h = ((h + 1) % capacity))
			j.add(String.valueOf(elements[h]));
		return j.toString();
	}
}
