package base;

import java.util.*;

/**
 * @author Sam Hooper
 *
 */
public class FixedQueue<T> extends AbstractCollection<T> {
	
	private final ArrayDeque<T> queue;
	private final int capacity;
	
	public FixedQueue(int capacity) {
		this.queue = new ArrayDeque<>();
		this.capacity = capacity;
	}
	
	
	@Override
	public boolean add(T e) {
		if(size() == capacity)
			queue.removeLast();
		queue.addFirst(e);
		return true;
	}

	@Override
	public boolean remove(Object o) {
		return queue.remove(o);
	}
	
	@Override
	public void clear() {
		queue.clear();
	}

	@Override
	public Iterator<T> iterator() {
		return queue.iterator();
	}

	@Override
	public int size() {
		return queue.size();
	}
	
}
