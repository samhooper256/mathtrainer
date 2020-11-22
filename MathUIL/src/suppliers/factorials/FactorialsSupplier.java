package suppliers.factorials;

import static problems.Problem.*;
import static suppliers.NamedBooleanRef.*;
import static suppliers.NamedIntRange.*;

import java.util.*;

import problems.*;
import suppliers.*;
import utils.Colls;

/**
 * @author Sam Hooper
 *
 */
public class FactorialsSupplier extends SettingsProblemSupplier {
	private static final RangeStore VALUES = RangeStore.of(0, 12, 0, 8), TERMS = RangeStore.of(1, 4, 1, 3);
	private final NamedIntRange values = of(VALUES, "Values of terms (before factorial)"), terms = of(TERMS, "Terms");
	private final NamedBooleanRef addition = of(true, "Addition"), subtraction = of(true, "Subtraction"), multiplication = of(true, "Multiplication");
	public FactorialsSupplier() {
		settings(terms, values, addition, subtraction, multiplication);
	}

	@Override
	public Problem get() {
		List<String> opList = opList();
		StringBuilder sb = new StringBuilder();
		int termCount = opList.size() == 0 ? 1 : intInclusive(terms);
		sb.append(intInclusive(values)).append('!');
		for(int i = 1; i < termCount; i++)
			sb.append(getOp(opList)).append(intInclusive(values)).append('!');
		return new SimpleExpression(sb.toString());
	}
	
	private String getOp(List<String> opList) {
		return Colls.getRandom(opList);
	}
	
	private List<String> opList() {
		List<String> list = new ArrayList<>(3);
		if(addition.get())
			list.add("+");
		if(subtraction.get())
			list.add("-");
		if(multiplication.get())
			list.add("*");
		return list;
	}
	
}
