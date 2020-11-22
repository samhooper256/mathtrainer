package suppliers.other;

import static problems.Problem.*;
import static suppliers.NamedIntRange.*;

import java.util.*;
import java.util.stream.*;

import math.*;
import problems.*;
import suppliers.*;
import utils.Colls;

/**
 * @author Sam Hooper
 *
 */
public class TypesOfNumbersSupplier extends SettingsProblemSupplier {
	private static final RangeStore OPTIONS = RangeStore.of(2, 6, 3, 3), VALUES = RangeStore.of(1, 10_000, 1, 500);
	private final NamedIntRange options = of(OPTIONS, "Number of options to choose from"), values = of(VALUES, "Values of numbers");
	
	public TypesOfNumbersSupplier() {
		settings(options);
	}

	@Override
	public Problem get() {
		final int choices = intInclusive(options);
		List<Integer> nums = RAND.ints(choices, values.low(), values.high()).boxed().collect(Collectors.toList());
		List<EnumSet<NumberAdjectives>> adjs = nums.stream().map(NumberAdjectives::describe).collect(Collectors.toList());
//		System.out.printf("before:%n%s%n", IntStream.range(0, nums.size()).mapToObj(i -> String.format("%s=%s", nums.get(i), adjs.get(i)) ).collect(Collectors.joining("\n\t")));
		Colls.removeIntersection(adjs);
//		System.out.printf("after:%n\t%s%n", IntStream.range(0, nums.size()).mapToObj(i -> String.format("%s=%s", nums.get(i), adjs.get(i)) ).collect(Collectors.joining("\n\t")));
		if(Colls.allAreEmpty(adjs))
			return get(); // try again
		final int correctIndex = Colls.uniqueIndex(adjs);
		if(correctIndex == -1)
			return get(); //try again
		final String adjListString = prettyAdjList(adjs.get(correctIndex));
		final String article = getArticle(adjListString);
		final Integer correctNum = nums.get(correctIndex);
		Collections.shuffle(nums);
		return ComplexValued.of(
			String.format("Which of the following is %s %s number: %s?", article, adjListString,
					nums.stream().map(String::valueOf).collect(Collectors.joining(", "))),
			new Complex(correctNum)
		);
	}
	
	private String getArticle(String adjListString) {
		return adjListString.substring(0, 1).matches("[aeiou]") ? "an" : "a";
	}

	private static String prettyAdjList(EnumSet<NumberAdjectives> set) {
		StringJoiner j = new StringJoiner(", ");
		for(NumberAdjectives adj : set)
			j.add(adj.toString().toLowerCase());
		return j.toString();
	}

	
	
}
