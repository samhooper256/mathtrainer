package suppliers;

/**
 * 
 * @author Sam Hooper
 */
public enum SupplierMode {
	
	/** Generates problems in a random order.*/
	RANDOM("Random"),
	/** Generates problems drawn randomly from a finite set of problems and does not generate a problem again until every problem in that set has been solved.*/
	STACKED("Stacked");
	
	private final String displayName;
	
	SupplierMode(String displayName) {
		this.displayName = displayName;
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
}
