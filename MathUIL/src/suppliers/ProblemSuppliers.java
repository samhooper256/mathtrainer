package suppliers;

import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.net.*;
import java.util.*;
import java.util.function.*;
import java.util.regex.Pattern;

import suppliers.bases.*;
import suppliers.divisors.RelativelyPrimeSupplier;
import suppliers.equations.*;
import suppliers.exponentiation.*;
import suppliers.factorials.FactorialsSupplier;
import suppliers.fractions.*;
import suppliers.gcd.*;
import suppliers.matrices.MultiplyingMatricesSupplier;
import suppliers.other.AbsoluteValueSupplier;
import suppliers.pemdas.*;
import suppliers.remainder.*;
import suppliers.roots.OtherRootsSupplier;
import suppliers.sequences.FibbonacciSupplier;
import suppliers.sets.SubsetsSupplier;

/**
 * @author Sam Hooper
 *
 */
public final class ProblemSuppliers {
	
	public static final class Info {
		
		private final Class<? extends ProblemSupplier> supplierClass;
		private final Supplier<? extends ProblemSupplier> factory;
		private final Category category;
		private final String displayName; //name can be changed, so not final
		
		private Info(Class<? extends ProblemSupplier> supplierClass, Supplier<? extends ProblemSupplier> factory, final Category category, String displayName) {
			this.supplierClass = supplierClass;
			this.displayName = displayName;
			this.category = category;
			this.factory = factory;
		}
		
//		private Info(Class<? extends ProblemSupplier> supplierClass, Supplier<? extends ProblemSupplier> factory) {
//			this(supplierClass, factory, supplierClass.getSimpleName());
//		}
		
		public Class<? extends ProblemSupplier> getSupplierClass() {
			return supplierClass;
		}
		
		public Category getCategory() {
			return category;
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
	private static final Map<Package, Category> CAT_MAP;
	private static final Pattern NAME_SPACE_LOCATIONS = Pattern.compile("(?<![A-Z])(?=[A-Z])|(?<!\\d)(?=\\d)");
	static {
		REGISTERED_SUPPLIERS = new HashMap<>();
		CAT_MAP = new HashMap<>();
		CAT_MAP.put(ToBase10Supplier.class.getPackage(), Category.BASES);
		CAT_MAP.put(RelativelyPrimeSupplier.class.getPackage(), Category.DIVISORS);
		CAT_MAP.put(RootsOfQuadraticsSupplier.class.getPackage(), Category.EQUATIONS);
		CAT_MAP.put(CubesSupplier.class.getPackage(), Category.EXPONENTIATION);
		CAT_MAP.put(FactorialsSupplier.class.getPackage(), Category.FACTORIALS);
		CAT_MAP.put(CompareFractionsSupplier.class.getPackage(), Category.FRACTIONS);
		CAT_MAP.put(GCDSupplier.class.getPackage(), Category.GCD);
		CAT_MAP.put(MultiplyingMatricesSupplier.class.getPackage(), Category.MATRICES);
		CAT_MAP.put(AbsoluteValueSupplier.class.getPackage(), Category.OTHER);
		CAT_MAP.put(FOILSupplier.class.getPackage(), Category.PEMDAS);
		CAT_MAP.put(Mod9Supplier.class.getPackage(), Category.REMAINDER);
		CAT_MAP.put(OtherRootsSupplier.class.getPackage(), Category.ROOTS);
		CAT_MAP.put(FibbonacciSupplier.class.getPackage(), Category.SEQUENCES);
		CAT_MAP.put(SubsetsSupplier.class.getPackage(), Category.SETS);
		try {
			detectSuppliers();
		}
		catch (Exception e) {
			e.printStackTrace();
			System.err.println("\nUnable to detect ProblemSuppliers. Exiting program.");
			System.exit(-1);
		}
	}
	
	private static void detectSuppliers() throws URISyntaxException, ClassNotFoundException {
		ProblemSuppliers instance = new ProblemSuppliers();
		URL res = instance.getClass().getResource(instance.getClass().getSimpleName() + ".class");
		File f = new File(res.toURI());
		File pkgFile = f.getParentFile();
		addInfosFromPackageAndSubpackages(pkgFile);
	}
	
	private static void addInfosFromPackageAndSubpackages(File pkgFile) throws ClassNotFoundException {
		for(File file : pkgFile.listFiles()) {
			if(file.isDirectory())
				addInfosFromPackageAndSubpackages(file);
			else
				addInfoFromFile(file);
		}
	}

	private static void addInfoFromFile(final File classFile) throws ClassNotFoundException {
		final String fileName = classFile.getName();
		if(fileName.endsWith("Supplier.class")) {
			final String className = fileName.substring(0, fileName.lastIndexOf('.'));
			if(EXCLUDED_SUPPLIER_NAMES.contains(className))
				return;
			final String fullyQualifiedName = getPackageName(classFile) + className;
			Class<? extends ProblemSupplier> clazz = (Class<? extends ProblemSupplier>)
					Class.forName(fullyQualifiedName);
			if(clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers()))
				return;
			addInfoFor(clazz);
		}
	}
	
	private static String getPackageName(File classFile) {
		StringBuilder sb = new StringBuilder(".");
		while(!classFile.getParentFile().getName().equals("suppliers")) {
			sb.insert(0, "." + classFile.getParentFile().getName());
			classFile = classFile.getParentFile();
		}
		return "suppliers" + sb;
	}
	
	private static void addInfoFor(final Class<? extends ProblemSupplier> clazz) {
		Constructor<?> noArgConstructor = getNoArgConstructorOrThrow(clazz.getConstructors());
		Supplier<? extends ProblemSupplier> supplier = getSupplierFromNoArgConstructor(noArgConstructor);
		String name = getDisplayNameFromSupplierClass(clazz);
		Category category = getCategory(clazz);
		addInfos(info(clazz, supplier, category, name));
	}
	
	private static Category getCategory(Class<? extends ProblemSupplier> clazz) {
		return CAT_MAP.get(clazz.getPackage());
	}
	
	
	private static String getDisplayNameFromSupplierClass(Class<?> clazz) {
		for(Annotation anno : clazz.getAnnotations())
			if(anno.annotationType() == Named.class)
				return ((Named) anno).value();
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
	
	private static Info info(final Class<? extends ProblemSupplier> supplierClass, final Supplier<? extends ProblemSupplier> factory, final Category category, final String displayName) {
		return new Info(supplierClass, factory, category, displayName);
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
	
	public static Category categoryOf(ProblemSupplier ps) {
		return categoryOf(ps.getClass());
	}
	
	public static Category categoryOf(Class<? extends ProblemSupplier> clazz) {
		return info(clazz).getCategory();
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