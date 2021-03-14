package suppliers;

import java.util.Set;
import java.util.function.Supplier;

/** 
 * 
 * <p>A class or interface that "hosts" several {@link ProblemSupplier}{@code s} should extend/implement this interface and provide those {@code ProblemSuppliers}
 * by <b>hiding the getFatories() method with a new method of the same signature that returns a {@link Set} of the {@code ProblemSupplier} being hosted.</b> </p>
 * @author Sam Hooper
 * */
public interface ProblemSupplierHost {
	
	static Set<Supplier<? extends ProblemSupplier>> getFactories() {
		throw new UnsupportedOperationException(String.format("This method must be hidden"));
	}
	
}
