package suppliers.equations;

import static problems.Problem.*;
import static suppliers.NamedBooleanRef.*;
import static suppliers.NamedIntRange.*;

import math.Complex;
import problems.*;
import suppliers.*;

/**
 * @author Sam Hooper
 *
 */
public class SolvingLinearEquationsSupplier extends SettingsProblemSupplier {
	
	private static final RangeStore X_COEFFICIENT = RangeStore.of(1, 20, 2, 12), CONSTANT = RangeStore.of(0, 20, 0, 12);
	
	private final NamedIntRange xCoefficient = of(X_COEFFICIENT, "Coefficient of x"), constant = of(CONSTANT, "Constant term");
	
	public SolvingLinearEquationsSupplier() {
		settings(xCoefficient, constant);
	}

	@Override
	public Problem get() {
		int qxCo = intInclusive(xCoefficient);
		int x = intInclusive(-10, 10);
		int qconstTerm = intInclusive(constant);
		int result = qxCo * x + qconstTerm;
		int axCo = intInclusive(xCoefficient);
		int aconstTerm = intInclusive(constant);
		String qDisplay = displayExp(qxCo, qconstTerm), aDisplay = displayExp(axCo, aconstTerm);
		return MultiValued.of(String.format("Given that %s = %d, find %s:", qDisplay, result, aDisplay)).addResult(new Complex(axCo * x + aconstTerm));
	}
	
	private String displayExp(int xCo, int constTerm) {
		if(Math.random() <= 0.5)
			return xCo + "x" + (constTerm == 0 ? "" : constTerm > 0 ? (" + " + constTerm) : (" - " + -constTerm));
		else
			return (constTerm == 0 ? "" : constTerm + " + ") + xCo + "x";
	}

}
