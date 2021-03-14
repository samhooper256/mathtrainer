package suppliers.other;

import static problems.Problem.*;
import static suppliers.NamedBooleanRef.*;
import static suppliers.NamedIntRange.*;

import java.math.BigDecimal;

import math.Complex;
import problems.*;
import suppliers.*;

/**
 * @author Sam Hooper
 *
 */
public class ComplexNumbersSupplier extends SettingsProblemSupplier {
	private static final RangeStore VALUE = RangeStore.of(1, 15, 1, 11);
	private final NamedIntRange value = of(VALUE, "Value of real and imaginary parts");
	
	public ComplexNumbersSupplier() {
		addAllSettings(value);
	}

	@Override
	public Problem get() {
		Complex left = new Complex(intInclusive(value), intInclusive(value)), right = new Complex(intInclusive(value), intInclusive(value));
		Complex product = left.multiply(right);
		System.out.printf("left=%s, right=%s, product=%s%n", left, right, product);
		final String qString;
		final BigDecimal answer;
		int rand = RAND.nextInt(4);
		if(rand == 0) {
			qString = "a";
			answer = product.realPart();
		}
		else if(rand == 1) {
			qString = "b";
			answer = product.imaginaryPart();
		}
		else if(rand == 2) {
			qString = "a + b";
			answer = product.realPart().add(product.imaginaryPart());
		}
		else {
			qString = "a - b";
			answer = product.realPart().subtract(product.imaginaryPart());
		}
		return ComplexValued.of(String.format("(%s)(%s) = a + bi. Find %s", left, right, qString), new Complex(answer));
	}
	
}
