package base;

import java.util.*;
import java.util.function.*;

import utils.ListRef;

/**
 * A {@link ProblemSupplier} that is composed of several other {@code Suppliers}. The {@link #get()} method
 * returns a {@link Problem} from a randomly chosen one of the {@link CompositeProblemSupplier CompositeProblemSupplier's} {@link Suppliers}.
 * @author Sam Hooper
 *
 */
public class CompositeProblemSupplier implements ProblemSupplier {
	
	private final ListRef<ProblemSupplier> suppliers;
	
	@SafeVarargs
	public static CompositeProblemSupplier of(ProblemSupplier... suppliers) {
		return new CompositeProblemSupplier(suppliers);
	}
	
	@SafeVarargs
	private CompositeProblemSupplier(ProblemSupplier... suppliers) {
		final ArrayList<ProblemSupplier> aList = new ArrayList<>(suppliers.length);
		Collections.addAll(aList, suppliers);
		this.suppliers = new ListRef<>(aList);
	}

	public void addSupplier(ProblemSupplier supplier) {
		this.suppliers.add(supplier);
	}
	
	/**
	 * Return {@code true} if the {@link Supplier} was present and has been removed, {@code false} otherwise. 
	 */
	public boolean removeSupplier(ProblemSupplier suppler) {
		return suppliers.remove(suppler);
	}
	
	@Override
	public Problem get() {
		return suppliers.get((int) (Math.random() * suppliers.size())).get();
	}
	
	public ListRef<ProblemSupplier> suppliers() {
		return suppliers;
	}
}
