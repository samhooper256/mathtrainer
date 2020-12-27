package suppliers;

import problems.Problem;

/**
 * <p>An enum representing different categories of {@link Problem Problems}. There is also an {@link #OTHER} category for {@code Problems}
 * that don't fit in any other.</p>
 * @author Sam Hooper
 *
 */
public enum Category {
	BASES("Bases"), DIVISORS("Divisors"), EQUATIONS("Equations"), EXPONENTIATION("Exponentiation"), FACTORIALS("Factorials"), FRACTIONS("Fractions"),
	GCD("GCD"), MATRICES("Matrices"), OTHER("Other"),  PEMDAS("PEMDAS"), REMAINDER("Remainder"), ROOTS("Roots"), SEQUENCES("Sequences"), SETS("Sets");
	
	private final String displayText;
	
	Category(final String display) {
		this.displayText = display;
	}
	
	public String display() {
		return displayText;
	}
	
}
