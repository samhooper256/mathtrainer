package base;

import java.util.*;
import java.util.function.*;

/**
 * A {@link Supplier} of {@link Problem Problems} that is composed of several other {@code Suppliers}. The {@link #get()} method
 * returns a {@code Problem} from a randomly chosen one of the {@link CompositeProblemSupplier CompositeProblemSupplier's} {@code Suppliers}.
 * @author Sam Hooper
 *
 */
public class CompositeProblemSupplier implements Supplier<Problem> {
	private final ArrayList<Supplier<? extends Problem>> suppliers;
	
	
	@SafeVarargs
	public static CompositeProblemSupplier of(Supplier<? extends Problem>... suppliers) {
		return new CompositeProblemSupplier(suppliers);
	}
	
	@SafeVarargs
	private CompositeProblemSupplier(Supplier<? extends Problem>... suppliers) {
		this.suppliers = new ArrayList<>(suppliers.length);
		Collections.addAll(this.suppliers, suppliers);
	}

	public void addSupplier(Supplier<? extends Problem> supplier) {
		this.suppliers.add(supplier);
	}
	
	/**
	 * Return {@code true} if the {@link Supplier} was present and has been removed, {@code false} otherwise. 
	 */
	public boolean removeSupplier(Supplier<? extends Problem> suppler) {
		return suppliers.remove(suppler);
	}
	
	@Override
	public Problem get() {
		return suppliers.get((int) (Math.random() * suppliers.size())).get();
	}
	
}
