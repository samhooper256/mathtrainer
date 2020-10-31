package suppliers;

import java.util.*;
import java.util.function.*;

/**
 * @author Sam Hooper
 *
 */
public class ProblemSuppliers {
	
	public static final class Info {
		
		private final Class<? extends ProblemSupplier> supplierClass;
		private final String displayName;
		private final Supplier<? extends ProblemSupplier> factory;
		
		private Info(Class<? extends ProblemSupplier> supplierClass, Supplier<? extends ProblemSupplier> factory, String displayName) {
			this.supplierClass = supplierClass;
			this.displayName = displayName;
			this.factory = factory;
		}
		
		private Info(Class<? extends ProblemSupplier> supplierClass, Supplier<? extends ProblemSupplier> factory) {
			this(supplierClass, factory, supplierClass.getSimpleName());
		}
		
		public Class<? extends ProblemSupplier> getSupplierClass() {
			return supplierClass;
		}
		
		public String getDisplayName() {
			return displayName;
		}
		
		public Supplier<? extends ProblemSupplier> getFactory() {
			return factory;
		}
		
	}
	private static final Map<Class<? extends ProblemSupplier>, Info> REGISTERED_SUPPLIERS;
	
	static {
		REGISTERED_SUPPLIERS = new HashMap<>();
		addInfos(
			info(AdditionProblemSupplier.class, AdditionProblemSupplier::new, "Addition Problem"),
			info(MultiplicationProblemSupplier.class, MultiplicationProblemSupplier::new, "Multiplication Problem")
		);
	}
	
	private static Info info(final Class<? extends ProblemSupplier> supplierClass, final Supplier<? extends ProblemSupplier> factory, final String displayName) {
		return new Info(supplierClass, factory, displayName);
	}
	
	private static void addInfos(Info... infos) {
		for(Info info : infos)
			REGISTERED_SUPPLIERS.put(info.getSupplierClass(), info);
	}
	
	public static Info info(Class<? extends ProblemSupplier> clazz) {
		checkRegistered(clazz);
		return REGISTERED_SUPPLIERS.get(clazz);
	}
	public static String nameOf(Class<? extends ProblemSupplier> clazz) {
		checkRegistered(clazz);
		return REGISTERED_SUPPLIERS.get(clazz).getDisplayName();
	}
	private static void checkRegistered(Class<? extends ProblemSupplier> problemSupplier) {
		if(!isRegistered(problemSupplier))
			throw new IllegalArgumentException(problemSupplier.getClass() + " is not registered.");
	}

	/**
	 * @throws NullPointerException if the given {@link Class} object is {@code null}.
	 */
	public static boolean isRegistered(Class<? extends ProblemSupplier> problemSupplier) {
		Objects.requireNonNull(problemSupplier);
		return REGISTERED_SUPPLIERS.containsKey(problemSupplier);
	}
	
	public static Set<Class<? extends ProblemSupplier>> getRegistered() {
		return Collections.unmodifiableSet(REGISTERED_SUPPLIERS.keySet());
	}
	
	public static Collection<Info> getRegisteredInfos() {
		return Collections.unmodifiableCollection(REGISTERED_SUPPLIERS.values());
	}
	
}