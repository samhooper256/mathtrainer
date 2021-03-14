package utils;

import java.util.*;

/**
 * <p>An unmodifiable view of an {@link EnumSet}.</p>
 * @author Sam Hooper
 */
public class EnumSetView<E extends Enum<E>> implements Set<E> {

	private final EnumSet<E> set;
	
	public static <E extends Enum<E>> EnumSetView<E> of(EnumSet<E> set) {
		return new EnumSetView<>(set);
	}
	
	public static <E extends Enum<E>> EnumSetView<E> of(E e) {
		return of(EnumSet.of(e));
	}
	
	public static <E extends Enum<E>> EnumSetView<E> of(E e1, E e2) {
		return of(EnumSet.of(e1, e2));
	}
	
	public static <E extends Enum<E>> EnumSetView<E> of(E e1, E e2, E e3) {
		return of(EnumSet.of(e1, e2, e3));
	}
	
	public static <E extends Enum<E>> EnumSetView<E> of(E e1, E e2, E e3, E e4) {
		return of(EnumSet.of(e1, e2, e3, e4));
	}
	
	public static <E extends Enum<E>> EnumSetView<E> of(E e1, E e2, E e3, E e4, E e5) {
		return of(EnumSet.of(e1, e2, e3, e4, e5));
	}
	
	@SafeVarargs
    public static <E extends Enum<E>> EnumSetView<E> of(E first, E... rest) {
		return of(EnumSet.of(first, rest));
	}
	
	
	private EnumSetView(EnumSet<E> set) {
		this.set = set;
	}

	@Override
	public int size() {
		return set.size();
	}

	@Override
	public boolean isEmpty() {
		return set.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return set.contains(o);
	}

	@Override
	public Iterator<E> iterator() {
		return set.iterator();
	}

	@Override
	public Object[] toArray() {
		return set.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return set.toArray(a);
	}

	@Override
	public boolean add(E e) {
		throw new UnsupportedOperationException("Cannot add to EnumSetView");
	}

	@Override
	public boolean remove(Object o) {
		throw new UnsupportedOperationException("Cannot remove from EnumSetView");
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return set.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		throw new UnsupportedOperationException("Cannot add to EnumSetView");
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException("Cannot remove from EnumSetView");
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException("Cannot remove from EnumSetView");
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException("Cannot remove from EnumSetView");
	}

}
