package utils;

import java.util.*;

/**
 * @author Sam Hooper
 *
 */
public class FixedDoubleQueue extends AbstractFixedQueue {
	
	private double[] elements;
	
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
		for(PrimitiveIterator.OfDouble itr = primitiveIterator(); itr.hasNext();)
			j.add(String.valueOf(itr.nextDouble()));
			
		return j.toString();
	}
	
	@Override
	public void clearAndDecreaseCapacityTo(final int newCapacity) {
		if(newCapacity >= getCapacity())
			throw new IllegalArgumentException("New capacity must be strictly less than current capacity");
		if(newCapacity <= 0)
			throw new IllegalArgumentException("Capacity must be greater than zero");
		clear();
		elements = new double[newCapacity + 1];
		capacity = newCapacity;
		head = tail = 0;
		
	}
	
	@Override
	public void increaseCapacityTo(final int newCapacity) {
		if(newCapacity <= getCapacity())
			throw new IllegalArgumentException("New capacity must be strictly greater than current capacity");
		double[] newElements = new double[newCapacity + 1];
		int index = 0;
		for(PrimitiveIterator.OfDouble itr = primitiveIterator(); itr.hasNext(); ) {
			newElements[index++] = itr.nextDouble();
		}
		head = 0;
		tail = index;
		capacity = newCapacity;
		elements = newElements;
	}
	
	public PrimitiveIterator.OfDouble primitiveIterator() {
		return new PrimitiveIterator.OfDouble() {
			int i = 0;
			int h = head;
			
			@Override
			public boolean hasNext() {
				return i < size;
			}
			
			@Override
			public double nextDouble() {
				double result = elements[h];
				i++;
				h = ((h + 1) % capacity);
				return result;
			}
		};
	}
}
