package suppliers.matrices;

import static problems.Problem.*;
import static suppliers.NamedIntRange.*;

import java.util.Collections;

import math.*;
import problems.*;
import suppliers.*;

/**
 * @author Sam Hooper
 *
 */
public class MatricesTestSupplier extends SettingsProblemSupplier {
	
	
	public MatricesTestSupplier() {
		this.settings = Collections.emptyList();
	}
	
	@Override
	public Problem get() {
		return ComplexValued.of("<math><mfenced open=\"[\" close=\"]\"><mtable><mtr><mtd><mn>1</mn></mtd><mtd><mn>6</mn></mtd></mtr><mtr><mtd><mn>4</mn></mtd><mtd><mn>3</mn></mtd></mtr><mtr><mtd><mn>5</mn></mtd><mtd><mn>6</mn></mtd></mtr></mtable></mfenced></math>", new Complex(0), 3);
	}
	
	
	
	
}
