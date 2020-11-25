package suppliers.equations;

import static problems.Problem.*;
import static suppliers.NamedIntRange.*;

import math.BigFraction;
import problems.*;
import suppliers.*;

/**
 * @author Sam Hooper
 *
 */
public class SlopeOfLineFromPointsSupplier extends SettingsProblemSupplier {
	
	private static final RangeStore X_COORDS = RangeStore.of(-25, 25, -20, 20), Y_COORDS = RangeStore.of(-25, 25, -20, 20);
	
	private final NamedIntRange xCoords = of(X_COORDS, "X coordinates"), yCoords = of(Y_COORDS, "Y coordinates");

	public SlopeOfLineFromPointsSupplier() {
		settings(xCoords, yCoords);
	}

	@Override
	public Problem get() {
		int x1 = intInclusive(xCoords), x2 = intInclusive(xCoords), y1 = intInclusive(yCoords), y2 = intInclusive(yCoords);
		if(x1 == x2) x2++; //so that slope is not undefined.
		BigFraction slope = BigFraction.of(y2 - y1, x2 - x1);
		return MultiValued.of(String.format("The slope of the line containing the points (%d, %d) and (%d, %d) is:", x1, y1, x2, y2))
				.addResult(slope);
	}

}
