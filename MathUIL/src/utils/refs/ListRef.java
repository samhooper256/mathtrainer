package utils.refs;

import java.util.*;
import java.util.function.Supplier;

import utils.SingleListener;

/**
 * A reference to a {@link List}. Allows for {@link SingleListener SingleListeners} to be run when an element is added or removed.
 * Note that the listeners are run <i>after</i> the element is added or removed, and the object passed to the listener
 * is the element that was added/removed.
 * @author Sam Hooper
 *
 */
public class ListRef<E> implements Ref, Iterable<E> {
	
	private List<E> list;
	private ArrayList<SingleListener<E>> addListeners; //only constructed when a listener is actually added.
	private ArrayList<SingleListener<E>> removeListeners; //only constructed when a listener is actually added.
	
	/**
	 * Does <b>not</b> defensively copy the given {@link List}.
	 */
	public ListRef(List<E> list) {
		this.list = list;
	}
	
	public ListRef(Supplier<? extends List<E>> listFactory) {
		this.list = listFactory.get();
	}
	
	public boolean add(E item) {
		if(list.add(item)) {
			runAddListeners(item);
			return true;
		}
		return false;
	}
	
	/**
	 * Adds all of the given items to this {@link ListRef}. Calling this method is equivalent to calling 
	 * {@link #add(Object)} on each of the given items in the order they are given. Note that this method uses
	 * a non-reifiable vararg parameter, so take care to avoid heap pollution.
	 */
	public void addAll(@SuppressWarnings("unchecked") E... items) {
		for(E item : items)
			add(item);
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
	 * Removes the element from the list that this {@link ListRef} refers to.
	 * The listeners are only run if an item was removed.
	 * @param item
	 * @return {@code true} if an item was removed, {@code false} otherwise.
	 */
	public boolean remove(E item) {
		if(list.remove(item)) {
			runRemoveListeners(item);
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
	
	public boolean contains(E item) {
		return list.contains(item);
	}
	
	public List<E> getUnmodifiable() {
		return Collections.unmodifiableList(list);
	}
	
	public E get(final int index) {
		return list.get(index);
	}
	
	public int size() {
		return list.size();
	}

	@Override
	public Iterator<E> iterator() {
		return list.iterator();
	}
	
	@Override
	public String toString() {
		return "ListRef" + list.toString();
	}
}
