package suppliers.other;

import static problems.Problem.*;
import static suppliers.NamedBooleanRef.*;
import static suppliers.NamedIntRange.*;

import java.math.BigDecimal;

import math.RepeatingDecimal;
import problems.*;
import suppliers.*;

/**
 * @author Sam Hooper
 *
 */
public class RepeatingDecimalsToFractionsSupplier implements ProblemSupplier {
	//PATTERNS (repeating part in caps) : A, AB, ABC,aB, abC, abcBC
	
	//Each is element is an ordered pair: (non-repeating part of decimal, repeating part of decimal).
	private static final String[][] DECIMAL_PART = {{"", "A"}, {"", "AB"}, {"", "ABC"}, {"A", "B"}, {"AB", "C"}, {"ABC", "BC"}};
	
	public RepeatingDecimalsToFractionsSupplier() {}

	@Override
	public Problem get() {
		String beforeDecimalPoint = Math.random() <= 0.5 ? Integer.toString(intInclusive(1, 9)) : "0";
		RepeatingDecimal dec = getDecimal(beforeDecimalPoint);
		return Builder.of(Prettifier.ensureMath(Prettifier.repeating(dec))).addResult(dec.toBigFraction()).build();
	}
	
	private static RepeatingDecimal getDecimal(final String beforeDecimalPoint) {
		int index = intExclusive(DECIMAL_PART.length);
		String[] pair = DECIMAL_PART[index];
		String a = Integer.toString(intInclusive(1, 9)), b = Integer.toString(intInclusive(1, 9)), c = Integer.toString(intInclusive(1, 9));
		final String terminating = beforeDecimalPoint + "." + pair[0].replace("A", a).replace("B", b).replace("C", c);
		final String repeating = pair[1].replace("A", a).replace("B", b).replace("C", c);
		System.out.printf("(in getDecimal) terminating=%s, repeating=%s%n", terminating, repeating);
		return new RepeatingDecimal(terminating,
				repeating);
	}
	
}
