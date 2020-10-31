package suppliers;

import java.util.*;

import base.*;
import utils.*;

/**
 * @author Sam Hooper
 *
 */
public abstract class SingleOpSupplier implements ProblemSupplier {
	
	protected final NamedSetting<IntRange> termRange, digitRange;
	protected final List<Ref> settings;
	
	public SingleOpSupplier(int minTerms, int maxTerms, int lowTerms, int highTerms, int minDigits, int maxDigits, int lowDigits, int highDigits) {
		termRange = NamedSetting.of(new IntRange(minTerms, maxTerms, lowTerms, highTerms), "Terms");
		digitRange = NamedSetting.of(new IntRange(minDigits, maxDigits, lowDigits, highDigits), "Digits");
		this.settings = new ArrayList<>();
		Collections.addAll(settings, termRange, digitRange);
	}
	
	public int minTerms() {
		return termRange.ref().getLow();
	}
	
	public int maxTerms() {
		return termRange.ref().getHigh();
	}
	
	public int minDigits() {
		return digitRange.ref().getLow();
	}
	
	public int maxDigits() {
		return digitRange.ref().getHigh();
	}
	
	@Override
	public List<Ref> settings() {
		return settings;
	}
}
