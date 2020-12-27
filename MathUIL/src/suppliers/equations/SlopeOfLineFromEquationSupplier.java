package suppliers.equations;

import static problems.Problem.*;
import static suppliers.NamedBooleanRef.*;
import static suppliers.NamedIntRange.*;

import math.BigFraction;
import problems.*;
import suppliers.*;

/**
 * @author Sam Hooper
 *
 */
public class SlopeOfLineFromEquationSupplier extends SettingsProblemSupplier {
	
	private static final RangeStore X_COEFFICIENT = RangeStore.of(-20, 20, -10, 10), Y_COEFFICIENT = RangeStore.of(-20, 20, -10, 10);
	
	private final NamedIntRange xCoefficient = of(X_COEFFICIENT, "Coefficient of x"), yCoefficient = of(Y_COEFFICIENT, "Coefficient of y");
	
	public SlopeOfLineFromEquationSupplier() {
		settings(xCoefficient, yCoefficient);
	}
	
	@Override
	public Problem get() {
		int xCo = intInclusive(xCoefficient), yCo = intInclusive(yCoefficient), constant = intInclusive(-100, 100); //constant's value is irrelevant
		if(xCo == 0) xCo = 1;
		if(yCo == 0) yCo = 1;
		String exp = displayExp(xCo, yCo);
		String eq = Math.random() <= 0.5 ? exp + " = " + constant : constant + " = " + exp;
		BigFraction slope = BigFraction.of(-xCo, yCo);
		return Builder.of(String.format("Find the slope of the line %s:", eq)).addResult(slope).build();
	}
	
	private static String displayExp(final int xCo, final int yCo) {
		if(Math.random() <= 0.5)
			return (xCo == 1 ? "" : xCo) + "x" + (yCo == 1 ? " + " : yCo == -1 ? " - " : yCo > 0 ? " + " + yCo : " - " + -yCo) + "y";
		else
			return (yCo == 1 ? "" : yCo) + "y" + (xCo == 1 ? " + " : xCo == -1 ? " - " : xCo > 0 ? " + " + xCo : " - " + -xCo) + "x";
	}

}
