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
		
		private final Supplier<? extends ProblemSupplier> factory;
		private final Category category;
		private final String displayName;
		
		private Info(Supplier<? extends ProblemSupplier> factory, final Category category) {
			this.displayName = factory.get().getName(); //TODO Maybe... there's a better way to get the display name than creating a ProblemSupplier, getting the name, then throwing it away?
			this.category = category;
			this.factory = factory;
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
	private static final Map<String, Info> REGISTERED_SUPPLIERS; //Maps the display name of the Supplier<? extends ProblemSupplier> to its corresponding Info object.
	private static final Map<Package, Category> CAT_MAP;
	private static final File SUPPLIER_PACKAGE_FILE;
	
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
		CAT_MAP.put(ModSupplierHost.class.getPackage(), Category.REMAINDER);
		CAT_MAP.put(OtherRootsSupplier.class.getPackage(), Category.ROOTS);
		CAT_MAP.put(FibbonacciSupplier.class.getPackage(), Category.SEQUENCES);
		CAT_MAP.put(SubsetsSupplier.class.getPackage(), Category.SETS);
		
		ProblemSuppliers instance = new ProblemSuppliers();
		URL res = instance.getClass().getResource(instance.getClass().getSimpleName() + ".class"); //TODO do I need the whole "instance" thing?
		File fileForThisClass = null, supplierPackageFile = null;
		try {
			fileForThisClass = new File(res.toURI());
			supplierPackageFile = fileForThisClass.getParentFile();
		}
		catch (URISyntaxException ex) {
			fail(ex);
			
		}
		
		SUPPLIER_PACKAGE_FILE = supplierPackageFile;
		assert SUPPLIER_PACKAGE_FILE.getName().equals("suppliers");
		
		try {
			detectSuppliers();
		}
		catch (Exception ex) {
			fail(ex);
		}
	}

	public static void fail(Exception ex) {
		ex.printStackTrace();
		System.err.println("\nUnable to detect ProblemSuppliers. Exiting program.");
		System.exit(-1);
	}
	
	private static void detectSuppliers() throws URISyntaxException, ReflectiveOperationException {
		addInfosFromPackageAndSubpackages(SUPPLIER_PACKAGE_FILE);
	}
	
	private static void addInfosFromPackageAndSubpackages(File pkgFile) throws ReflectiveOperationException {
		for(File file : pkgFile.listFiles()) {
			if(file.isDirectory())
				addInfosFromPackageAndSubpackages(file);
			else
				addInfoFromFile(file);
		}
	}

	private static void addInfoFromFile(final File classFile) throws ReflectiveOperationException {
		final String fileName = classFile.getName();
		if(!fileName.endsWith(".class") || fileName.contains("$")) //the "$" check makes sure to ignore anonymous classes.
			return;
		final String className = fileName.substring(0, fileName.lastIndexOf('.'));
		if(EXCLUDED_SUPPLIER_NAMES.contains(className))
			return;
		final String fullyQualifiedName = getPackageName(classFile) + className;
		Class<?> clazz = Class.forName(fullyQualifiedName);
		if(ProblemSupplier.class.isAssignableFrom(clazz)) {
			if(clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers()))
				return;
			addInfoFor((Class<? extends ProblemSupplier>) clazz);
		}
		else if(ProblemSupplierHost.class.isAssignableFrom(clazz)) {
			if(clazz == ProblemSupplierHost.class)
				return;
			addInfoForHost((Class<? extends ProblemSupplierHost>) clazz);
		}
	}
	
	private static String getPackageName(File classFile) {
		StringBuilder sb = new StringBuilder(".");
		while(!classFile.getParentFile().getName().equals("suppliers")) {
			sb.insert(0, "." + classFile.getParentFile().getName());
			classFile = classFile.getParentFile();
		}
		return SUPPLIER_PACKAGE_FILE.getName() + sb;
	}
	
	private static void addInfoFor(final Class<? extends ProblemSupplier> clazz) {
		Supplier<? extends ProblemSupplier> supplier = getFactory(clazz);
		Category category = getCategory(clazz);
		addInfos(createInfo(supplier, category));
	}
	
	private static void addInfoForHost(final Class<? extends ProblemSupplierHost> clazz) throws ReflectiveOperationException {
		Set<Supplier<? extends ProblemSupplier>> suppliers = (Set<Supplier<? extends ProblemSupplier>>) clazz.getMethod("getFactories").invoke(null);
		Category category = getCategory(clazz);
		for(Supplier<? extends ProblemSupplier> supplier : suppliers) {
			addInfos(createInfo(supplier, category));
		}
	}
	
	private static Supplier<? extends ProblemSupplier> getFactory(Class<? extends ProblemSupplier> clazz) {
		Constructor<?>[] constructors = clazz.getConstructors();
		Constructor<?> noArg = getNoArgConstructorOrThrow(constructors);
		final Supplier<? extends ProblemSupplier> singleSupplier = getConstructorExecutor(clazz, noArg);
		return singleSupplier;
	}
	
	private static Supplier<? extends ProblemSupplier> getConstructorExecutor(Class<? extends ProblemSupplier> clazz, Constructor<?> constructor) {
		return () -> {
			try {
				return clazz.cast(constructor.newInstance());
			}
			catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException ex) {
				throw new RuntimeException("Could not execute constructor.", ex);
			}
		};
	}
	
	private static Constructor<?> getNoArgConstructorOrThrow(Constructor<?>[] constructors) {
		for(Constructor<?> con : constructors) {
			if(con.getParameterCount() == 0) {
				return con;
			}
		}
		throw new IllegalStateException(String.format("There is no no-arg constructor."));
	}
	
	
	
	private static Category getCategory(Class<?> clazz) {
		return CAT_MAP.get(clazz.getPackage());
	}
	
	private ProblemSuppliers() {}
	
	private static Info createInfo(final Supplier<? extends ProblemSupplier> factory, final Category category) {
		return new Info(factory, category);
	}
	
	private static void addInfos(Info... infos) {
		for(Info info : infos)
			REGISTERED_SUPPLIERS.put(info.getDisplayName(), info);
	}
	
	public static Info getInfoFor(String problemSupplierDisplayName) {
		checkRegistered(problemSupplierDisplayName);
		return REGISTERED_SUPPLIERS.get(problemSupplierDisplayName);
	}
	
	public static Category categoryOf(ProblemSupplier ps) {
		return categoryOf(ps.getName());
	}
	
	public static Category categoryOf(String problemSupplierDisplayName) {
		return getInfoFor(problemSupplierDisplayName).getCategory();
	}
	
	private static void checkRegistered(final String problemSupplierDisplayName) {
		if(!isRegistered(problemSupplierDisplayName))
			throw new IllegalArgumentException(problemSupplierDisplayName.getClass().getSimpleName() + " is not registered.");
	}

	/**
	 * @throws NullPointerException if the given {@link Class} object is {@code null}.
	 */
	public static boolean isRegistered(String problemSupplierDisplayName) {
		Objects.requireNonNull(problemSupplierDisplayName);
		return REGISTERED_SUPPLIERS.containsKey(problemSupplierDisplayName);
	}
	
	public static Collection<Info> getRegisteredInfos() {
		return Collections.unmodifiableCollection(REGISTERED_SUPPLIERS.values());
	}
	
}