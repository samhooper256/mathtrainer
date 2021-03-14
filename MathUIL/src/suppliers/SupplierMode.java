package suppliers;

import problems.Problem;

/**
 * 
 * @author Sam Hooper
 */
public enum SupplierMode {
	
	/** Generates random {@link Problem Problems}.*/
	RANDOM("Random"),
	/** Generates {@link Problem Problems} drawn randomly from a finite set of {@code Problems} and does not generate a {@code Problems} again until every {@code Problems}
	 * in that set has been solved. As a general rule, {@link ProblemSupplier ProblemSuppliers} should not
	 * {@link ProblemSupplier#supportsUnderCurrentSettings(SupplierMode) support} this mode if the aforementioned finite set would contain more than 1000 {@code Problems}.*/
	STACKED("Stacked");
	
	private final String displayName;
	
	SupplierMode(String displayName) {
		this.displayName = displayName;
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
}
