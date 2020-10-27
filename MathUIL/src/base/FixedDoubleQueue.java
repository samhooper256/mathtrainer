package base;

import java.util.*;

/**
 * @author Sam Hooper
 *
 */
public class FixedDoubleQueue extends AbstractFixedQueue {
	
	private final double[] elements;
	
	private double total, average;
	private int head, tail;
	
	public FixedDoubleQueue(final int capacity) {
		super(capacity);
		this.elements = new double[this.capacity];
		this.head = capacity / 2;
		this.tail = head;
	}
	
	/**
	 * decrements {@link #head} and returns the decremented value.
	 */
	private int decHead() {
		if(--head < 0)
			head = capacity - 1;
		return head;
	}
	
	private int incHead() {
		if(++head >= capacity)
			head = 0;
		return head;
	}
	
	/**
	 * decrements {@link #tail} and returns the decremented value.
	 */
	private int decTail() {
		if(--tail < 0)
			tail = capacity - 1;
		return tail;
	}
	
	/**
	 * Adds the given {@code double} to the front (first) in this {@link FixedDoubleQueue}.
	 */
	public void addFirst(final double item) {
		if(size == elements.length) {
			removeLast();
		}
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
		average = total / size;
	}
	
	/**
	 * Removed the element at the back (last) in this {@link FixedDoubleQueue}.
	 * @throws IllegalStateException if empty
	 */
	public void removeLast() {
		if(isEmpty())
			throw new IllegalStateException("Cannot remove from empty TimeQueue");
		double item = elements[tail];
		decTail();
		size--;
		removed(item);
	}
	
	/**
	 * Called at the end of any remove method. The argument is the {@code double} that was removed. When this method is called,
	 * size must have already been decremented. This method updates the {@link #average} and {@link #total} values.
	 */
	private void removed(double item) {
		total -= item;
		average = total / size;
	}
	
	public double getLast() {
		if(isEmpty())
			throw new NoSuchElementException();
		return elements[tail];
	}
	
	public double getTotal() {
		return total;
	}
	
	public double getAverage() {
		return average;
	}
	
	@Override
	public String toString() {
		StringJoiner j = new StringJoiner(", ", "[", "]");
		for(int h = head, i = 0; i < size; i++, h = ((h + 1) % capacity))
			j.add(String.valueOf(elements[h]));
		return j.toString();
	}
}
