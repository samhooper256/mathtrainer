package suppliers.other;

import static problems.Problem.*;
import static suppliers.NamedIntRange.*;

import java.util.*;

import problems.*;
import suppliers.*;

/**
 * @author Sam Hooper
 *
 */
public class AbsoluteValueSupplier extends SettingsProblemSupplier {
	private static final RangeStore TERMS = RangeStore.of(1, 8, 1, 6), VALUES = RangeStore.of(1,20,1,15), BARS = RangeStore.of(1,6,2,3);
	private final NamedIntRange terms = of(TERMS, "Terms"), values = of(VALUES, "Absolute values of terms"), bars = of(BARS, "Absolute value operations");
	
	public AbsoluteValueSupplier() {
		settings(terms, values, bars);
	}

	@Override
	public Problem get() {
		int ts = intInclusive(terms), bs = intInclusive(bars);
		List<Integer> barSpots = new ArrayList<>(bs*2);
		for(int i = 0; i < bs * 2; i++)
			barSpots.add(intInclusive(1, ts));
		Collections.sort(barSpots);
		System.out.printf("barSpots sorted=%s%n", barSpots);
		List<Integer> termList = new ArrayList<>(ts);
		for(int i = 0; i < ts; i++)
			termList.add(intInclusive(values) * (Math.random() <= 0.5 ? -1 : 1));
		int[] lowBarTimes = new int[ts + 1];
		int[] highBarTimes = new int[ts + 1];
		while(!barSpots.isEmpty()) {
			int low = barSpots.remove(0);
			int high = barSpots.remove(barSpots.size() - 1);
			lowBarTimes[low - 1]++;
			highBarTimes[high]++;
		}
		System.out.printf("lowBarTimes=%s, highBarTimes=%s%n", Arrays.toString(lowBarTimes), Arrays.toString(highBarTimes));
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < termList.size(); i++) {
			sb.append("|".repeat(lowBarTimes[i]));
			sb.append(termList.get(i));
			sb.append("|".repeat(highBarTimes[i]));
			if(i != termList.size() - 1)
				sb.append(Math.random() <= 0.5 ? "+" : "-");
		}
		sb.append("|".repeat(highBarTimes[highBarTimes.length - 1]));
		System.out.printf("exp:\"%s\"%n", sb);
		return new SimpleExpression(sb.toString());
	}
	
}
