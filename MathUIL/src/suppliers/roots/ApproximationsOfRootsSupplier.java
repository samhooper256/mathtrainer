package suppliers.roots;

import static problems.Problem.*;
import static suppliers.NamedBooleanRef.*;
import static suppliers.NamedIntRange.*;
import static problems.Prettifier.*;

import java.math.BigDecimal;
import java.util.*;

import problems.*;
import suppliers.*;

/**
 * @author Sam Hooper
 *
 */
public class ApproximationsOfRootsSupplier extends SettingsProblemSupplier {
	private static final RangeStore TERMS = RangeStore.of(1, 4, 1, 3), SQRT_DIGITS = RangeStore.of(1, 10, 1, 8), OTHER_DIGITS = RangeStore.of(1, 4, 1, 3);
	private final NamedIntRange terms = of(TERMS, "Terms"), sqrtDigits = of(SQRT_DIGITS, "Digits in radicands"), otherDigits = of(OTHER_DIGITS, "Digits in other terms");
	
	public ApproximationsOfRootsSupplier() {
		settings(terms, sqrtDigits, otherDigits);
	}

	@Override
	public Problem get() {
		final int ts = Problem.intInclusive(terms);
		int sqrts = Problem.intInclusive(1, terms.high());
		ArrayList<Boolean> types = new ArrayList<>(ts);
		for(int i = 1; i <= ts; i++)
			types.add(i <= sqrts);
		Collections.shuffle(types, RAND);
		StringBuilder sb = new StringBuilder();
		sb.append("<math>");
		BigDecimal product = BigDecimal.ONE;
		for(Iterator<Boolean> iterator = types.iterator(); iterator.hasNext();) {
			Boolean b = iterator.next();
			if(b) {
				int rc = Problem.intWithDigits(sqrtDigits);
				sb.append(sqrt(rc));
				product = product.multiply(BigDecimal.valueOf(rc).sqrt(math.Utils.INTERMEDIATE_CONTEXT));
			}
			else {
				int rv = Problem.intWithDigits(otherDigits);
				sb.append(num(rv));
				product = product.multiply(BigDecimal.valueOf(rv));
			}
			if(iterator.hasNext())
				sb.append(op('*'));
		}
		sb.append("</math>");
		return new SimpleApproximation(sb.toString(), product);
	}
	
}
