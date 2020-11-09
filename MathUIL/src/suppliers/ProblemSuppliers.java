package suppliers;

import java.io.*;
import java.lang.reflect.*;
import java.net.*;
import java.util.*;
import java.util.function.*;
import java.util.regex.Pattern;

/**
 * @author Sam Hooper
 *
 */
public final class ProblemSuppliers {
	
	public static final class Info {
		
		private final Class<? extends ProblemSupplier> supplierClass;
		private final Supplier<? extends ProblemSupplier> factory;
		
		private String displayName; //name can be changed, so not final
		
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
	private static final Set<String> EXCLUDED_SUPPLIER_NAMES = Set.of(CompositeProblemSupplier.class.getSimpleName(), SettingsProblemSupplier.class.getSimpleName());
	private static final Map<Class<? extends ProblemSupplier>, Info> REGISTERED_SUPPLIERS;
	private static final Pattern NAME_SPACE_LOCATIONS = Pattern.compile("(?<![A-Z])(?=[A-Z])|(?<!\\d)(?=\\d)");
	static {
		REGISTERED_SUPPLIERS = new HashMap<>();
		try {
			detectSuppliers();
		}
		catch (Exception e) {
			e.printStackTrace();
			System.err.println("\nUnable to detect ProblemSuppliers. Exiting program.");
			System.exit(-1);
		}
		customName(IntAddSubtractSupplier.class, "Integer Addition & Subtraction");
		customName(PEMDASApproximationSupplier.class, "PEMDAS Approximation");
		customName(Multiply11Supplier.class, "Multiply by 11 and like");
		customName(Multiply25Supplier.class, "Multiply by 25");
		customName(Multiply125Supplier.class, "Multiply by 125 and like");
		customName(Multiply5EndSupplier.class, "Multiply ending in 5");
		customName(Multiply101Supplier.class, "Multiply by 101 and like");
		customName(MultiplySame10sOnesAddTo10Supplier.class, "Multiply 2 numbers with same tens digit and ones digits that add to 10");
		customName(SumOfSquaresX3XSupplier.class, "Sum of squares with bases x and 3x");
		customName(SumConsecutiveSquaresSupplier.class, "Sum of two consecutive squares");
		customName(SumOfSquaresOuterAddsTo10Inner1ApartSupplier.class, "Sum of squares when outer digits add to 10 and inner digits are 1 apart");
		customName(BackwardsGCDLCMSupplier.class, "Find term given GCD and LCM");
	}
	
	private static void customName(Class<? extends ProblemSupplier> clazz, String name) {
		REGISTERED_SUPPLIERS.get(clazz).displayName = name;
	}
	
	private static void detectSuppliers() throws URISyntaxException, ClassNotFoundException {
		ProblemSuppliers instance = new ProblemSuppliers();
		URL res = instance.getClass().getResource(instance.getClass().getSimpleName() + ".class");
		File f = new File(res.toURI());
		File pkgFile = f.getParentFile();
		for(File classFile : pkgFile.listFiles()) {
			final String fileName = classFile.getName();
			if(fileName.endsWith("Supplier.class")) {
				final String clazzName = fileName.substring(0, fileName.lastIndexOf('.'));
				if(EXCLUDED_SUPPLIER_NAMES.contains(clazzName))
					continue;
				Class<? extends ProblemSupplier> clazz = (Class<? extends ProblemSupplier>)
						Class.forName("suppliers." + clazzName);
				if(clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers()))
					continue;
				Constructor<?> noArgConstructor = getNoArgConstructorOrThrow(clazz.getConstructors());
				Supplier<? extends ProblemSupplier> supplier = getSupplierFromNoArgConstructor(noArgConstructor);
				String name = getDisplayNameFromSupplierClass(clazz);
				addInfos(info(clazz, supplier, name));
			}
		}
	}
	
	private static String getDisplayNameFromSupplierClass(Class<?> clazz) {
		final String simpleName = clazz.getSimpleName();
		int endIndex = simpleName.lastIndexOf("Supplier");
		return NAME_SPACE_LOCATIONS.matcher(simpleName.substring(0, endIndex)).replaceAll(" ");
	}
	
	private static Supplier<? extends ProblemSupplier> getSupplierFromNoArgConstructor(final Constructor<?> constructor) {
		return () -> {
			try {
				return (ProblemSupplier) constructor.newInstance();
			}
			catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				throw new RuntimeException(e);
			}
		};
	}
	
	private static Constructor<?> getNoArgConstructorOrThrow(Constructor<?>[] constructors) {
		for(Constructor<?> constructor : constructors) {
			if(constructor.getParameterCount() == 0) {
				return constructor;
			}
		}
		throw new IllegalArgumentException("There is no no-arg constructor in the given array of constructors.\n\tThe given array contained: " + Arrays.toString(constructors));
	}
	
	private ProblemSuppliers() {
		
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
	
	/**
	 * @throws NullPointerException if the given {@link ProblemSupplier} is {@code null}.
	 */
	public static String nameOf(ProblemSupplier ps) {
		return nameOf(ps.getClass());
	}
	private static void checkRegistered(Class<? extends ProblemSupplier> problemSupplier) {
		if(!isRegistered(problemSupplier))
			throw new IllegalArgumentException(problemSupplier.getClass().getSimpleName() + " is not registered.");
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