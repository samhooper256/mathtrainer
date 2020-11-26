package utils;

import java.util.*;
import java.util.function.Consumer;

import problems.Problem;

/**
 * @author Sam Hooper
 *
 */
public class Colls {
	
	private Colls() {}
	
	/**
	 * Returns a random element from the given {@link Collecton}. Throws an exception if the given {@code Collection} {@link Collection#isEmpty() is empty}.
	 * @throws IllegalArgumentException if {@code coll} is empty.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getRandom(Collection<? extends T> coll) {
		if(coll instanceof List<?>)
			return (T) getRandom((List<?>) coll);
		if(coll.isEmpty())
			throw new IllegalArgumentException();
		int stop = Problem.intExclusive(coll.size());
		Iterator<? extends T> itr = coll.iterator();
		while(stop > 0) {
			itr.next();
			stop--;
		}
		return itr.next();
	}
	
	/**
	 * Returns a random element from the given {@link List}. Throws an exception if the given {@code List} {@link List#isEmpty() is empty}.
	 * @throws IllegalArgumentException if {@code list} is empty.
	 */
	public static <T> T getRandom(List<? extends T> list) {
		return list.get(Problem.intExclusive(list.size()));
	}
	
	/**
	 * Returns {@code true} if all the {@link Collection Collections} in {@code colls} {@link Collection#contains(Object) contain} {@code item}.
	 * Returns {@code false} if {@code colls} {@link Collection#isEmpty() is empty}.
	 */
	public static boolean allContain(Collection<? extends Collection<?>> colls, Object item) {
		if(colls.isEmpty())
			return false;
		for(Collection<?> coll : colls)
			if(!coll.contains(item))
				return false;
		return true;
	}
	
	/**
	 * Returns {@code true} if all the {@link Collection Collections} in {@code colls} {@link Collection#isEmpty() are empty}, {@code false} otherwise.
	 * Returns {@code true} if {@code colls} is empty.
	 */
	public static boolean allAreEmpty(Collection<? extends Collection<?>> colls) {
		for(Collection<?> coll : colls)
			if(!coll.isEmpty())
				return false;
		return true;
	}
	
	/**
	 * Returns the index of the first (lowest index) element in the given {@link Collection} that is different from all other elements
	 * (compared using {@link Object#equals(Object)}). Returns {@code -1} if no such element exists. Runs in O(n^2).
	 */
	public static <T> int uniqueIndex(List<? extends T> list) {
		outer:
		for(int i = 0; i < list.size(); i++) {
			T candidate = list.get(i);
			inner:
			for(int j = 0; j < list.size(); j++) {
				if(i == j)
					continue inner;
				if(Objects.equals(candidate, list.get(j)))
					continue outer;
			}
			return i;
		}
		return -1;
	}
	/**
	 * Returns {@code true} if all the elements in the given {@link Collection} are {@link Object#equals(Object) equal}, {@code false} otherwise.
	 * Returns {@code true} for an {@link Collection#isEmpty() empty} {@code Collection}.
	 */
	public static boolean allEqual(Collection<?> coll) {
		if(coll.isEmpty())
			return true;
		Iterator<?> itr = coll.iterator();
		Object obj = itr.next();
		while(itr.hasNext())
			if(!itr.next().equals(obj))
				return false;
		return true;
	}
	
	/**
	 * Applies the given action to every element in the {@link Collection}. The action is done in the order the elements are returned by the Collections's
	 * {@link Collection#iterator() iterator}.
	 */
	public static <T> void applyToAll(Collection<T> coll, Consumer<? super T> action) {
		for(T item : coll)
			action.accept(item);
	}
	
	/**
	 * Applies the given action to every element in the {@link List} in the given range. The action is performed on elements from lowest index to highest
	 * index in the given range.
	 */
	public static <T> void applyToAll(List<T> list, int startInclusive, int endExclusive, Consumer<? super T> action) {
		for(int i = startInclusive; i < endExclusive; i++)
			action.accept(list.get(i));
	}
	
	/**
	 * Given a {@link List} of {@link Set Sets}, removes from each set the intersection of all the sets. Does nothing if the given list {@link List#isEmpty() is empty}.
	 */
	public static <T> void removeIntersection(List<? extends Set<? extends T>> listOfSets) {
		if(listOfSets.isEmpty())
			return;
		for(Iterator<? extends T> iterator = listOfSets.iterator().next().iterator(); iterator.hasNext();) {
			T adj = iterator.next();
			if(Colls.allContain(listOfSets, adj)) {
				iterator.remove();
				Colls.applyToAll(listOfSets, 1, listOfSets.size(), ((Set<? extends T> set) -> set.remove(adj)));
			}
		}
	}
	
	/**
	 * Returns a new {@link List} with the same elements as {@code list} but in a random order.
	 */
	public static <T> List<T> shuffled(final List<T> list) {
		List<T> shuf = new ArrayList<>(list);
		Collections.shuffle(shuf);
		return shuf;
	}
}