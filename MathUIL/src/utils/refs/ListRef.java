package utils.refs;

import java.util.*;

import utils.SingleListener;

/**
 * A reference to a {@link List}. Allows for {@link SingleListener SingleListeners} to be run when an element is added or removed <b>through
 * the {@code ListRef}.</b>
 * Note that the listeners are run <i>after</i> the element is added or removed, and the object passed to the listener
 * is the element that was added/removed. Change actions are run after add/remove listeners.
 * @author Sam Hooper
 *
 */
public class ListRef<E> extends AbstractRef implements Collection<E> {
	
	private List<E> list;
	private ArrayList<SingleListener<E>> addListeners; //only constructed when a listener is actually added.
	private ArrayList<SingleListener<E>> removeListeners; //only constructed when a listener is actually added.
	
	/**
	 * Does <b>not</b> defensively copy the given {@link List}.
	 */
	public ListRef(List<E> list) {
		this.list = list;
	}
	
	@Override
	public boolean add(E item) {
		if(list.add(item)) {
			runAddListeners(item);
			runChangeActions();
			return true;
		}
		return false;
	}
	
	private void runAddListeners(E item) {
		if(addListeners == null)
			return;
		for(SingleListener<E> listener : addListeners)
			listener.listen(item);
	}
	
	public void addAddListener(SingleListener<E> addListener) {
		if(addListeners == null)
			addListeners = new ArrayList<>();
		addListeners.add(addListener);
	}
	
	/**
	 * Returns {@code true} if this {@link ListRef} had that listener and it has been removed, {@code false} otherwise.
	 */
	public boolean removeAddListener(SingleListener<E> addListener) {
		if(addListeners == null)
			return false;
		return addListeners.remove(addListener);
	}
	
	/**
	 * Removes the first occurrence of the given item from the list that this {@link ListRef} refers to.
	 * The listeners are only run if an item was removed.
	 * @param item
	 * @return {@code true} if an item was removed, {@code false} otherwise.
	 */
	@Override
	public boolean remove(Object item) {
		if(list.remove(item)) {
			@SuppressWarnings("unchecked")
			E cast = (E) item;
			runRemoveListeners(cast);
			runChangeActions();
			return true;
		}
		return false;
	}
	
	private void runRemoveListeners(E item) {
		if(removeListeners == null)
			return;
		for(SingleListener<E> listener : removeListeners)
			listener.listen(item);
	}
	
	public void addRemoveListener(SingleListener<E> removeListener) {
		if(removeListeners == null)
			removeListeners = new ArrayList<>();
		removeListeners.add(removeListener);
	}
	
	/**
	 * Returns {@code true} if this {@link ListRef} had that listener and it has been removed, {@code false} otherwise.
	 */
	public boolean removeRemoveListener(SingleListener<E> removeListener) {
		if(removeListeners == null)
			return false;
		return removeListeners.remove(removeListener);
	}
	
	public List<E> getUnmodifiable() {
		return Collections.unmodifiableList(list);
	}
	
	public E get(final int index) {
		return list.get(index);
	}
	
	@Override
	public int size() {
		return list.size();
	}

	@Override
	public boolean isEmpty() {
		return list.isEmpty();
	}
	
	@Override
	public Iterator<E> iterator() {
		return list.iterator();
	}
	
	@Override
	public String toString() {
		return String.format("ListRef%s", list.toString());
	}

	@Override
	public boolean contains(Object o) {
		return list.contains(o);
	}

	@Override
	public Object[] toArray() {
		return list.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return list.toArray(a);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		for(Object o : c)
			if(!contains(o))
				return false;
		return true;
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		boolean result = false;
		for(E o : c)
			result |= add(o);
		return result;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		boolean changed = false;
		for(Object o : c)
			changed |= remove(o);
		return changed;
	}
	
	/** Unsupported. */
	@Override
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException("ListRefs do not support retainAll");
	}

	/** Unsupported. */
	@Override
	public void clear() {
		throw new UnsupportedOperationException("ListRefs do no support clear");
	}
	
}
