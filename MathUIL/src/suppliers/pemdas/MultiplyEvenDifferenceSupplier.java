package suppliers.pemdas;

import java.util.List;

import problems.*;
import suppliers.*;
import utils.*;

/**
 * @author Sam Hooper
 *
 */
public class MultiplyEvenDifferenceSupplier extends SettingsProblemSupplier {
	private static final int MIN_DISTANCE = 0, MAX_DISTANCE = 12, LOW_DISTANCE = 0, HIGH_DISTANCE = 6;
	private static final int MIN_CENTER = 6, MAX_CENTER = 120, LOW_CENTER = 10, HIGH_CENTER = 100;
	
	private final NamedSetting<IntRange> dist, center;
	
	public MultiplyEvenDifferenceSupplier() {
		this(LOW_DISTANCE, HIGH_DISTANCE, LOW_CENTER, HIGH_CENTER);
	}
	
	public MultiplyEvenDifferenceSupplier(int lowDist, int highDist, int lowCenter, int highCenter) {
		dist = NamedSetting.of(new IntRange(MIN_DISTANCE, MAX_DISTANCE, lowDist, highDist) {

			@Override
			public void setLow(int newLow) {
				super.setLow(newLow);
				if(highCenter() - newLow < 0) {
					if(lowCenter() > newLow)
						center.ref().setLow(newLow);
					center.ref().setHigh(newLow);
				}
			}
			
		}, "Distance from center");
		center = NamedSetting.of(new IntRange(MIN_CENTER, MAX_CENTER, lowCenter, highCenter), "Center");
		settings = List.of(dist, center);
	}

	@Override
	public Problem get() {
//		System.out.printf("(enter) get, lowDist=%d, high=%d, lowCenter=%d, high=%d%n", lowDist(), highDist(), lowCenter(), highCenter());
		//center - minDist >= 0
		int center = -1;
		while(center - lowDist() < 0) {
			center = Problem.intInclusive(lowCenter(), highCenter());
		}
		int highDist = Math.min(highDist(), center);
		int realDist = Problem.intInclusive(lowDist(), highDist);
		
		return SimpleExpression.multiplyTerms(Problem.shuffled(center - realDist, center + realDist));
	}
	
	public int lowDist() {
		return dist.ref().getLow();
	}
	
	public int highDist() {
		return dist.ref().getHigh();
	}
	
	public int lowCenter() {
		return center.ref().getLow();
	}
	
	public int highCenter() {
		return center.ref().getHigh();
	}
	
	
}
