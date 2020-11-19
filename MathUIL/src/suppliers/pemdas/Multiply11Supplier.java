package suppliers.pemdas;

import java.util.*;

import problems.*;
import suppliers.*;
import utils.*;

/**
 * @author Sam Hooper
 *
 */
public class Multiply11Supplier extends SettingsProblemSupplier {
	
	private static final int MIN_ONES = 2, MAX_ONES = 4, MIN_NON_ONES_DIGITS = 1, MAX_NON_ONES_DIGITS = 4;
	private static final int DEFUALT_LOW_ONES = 2, DEFAULT_HIGH_ONES = 2, DEFAULT_LOW_NON_ONES_DIGITS = 2, DEFUALT_HIGH_NON_ONES_DIGITS = 3;
	private static final boolean DEFAULT_INCLUDE_121 = true;
	
	private final NamedSetting<IntRange> onesRange;
	private final NamedSetting<IntRange> nonDigitRange;
	private final NamedSetting<BooleanRef> include121;
	
	public Multiply11Supplier() {
		this(DEFUALT_LOW_ONES, DEFAULT_HIGH_ONES, DEFAULT_LOW_NON_ONES_DIGITS, DEFUALT_HIGH_NON_ONES_DIGITS);
	}
	
	public Multiply11Supplier(final int lowOnes, final int highOnes, final int lowNon, final int highNon) {
		this.onesRange = NamedSetting.of(new IntRange(MIN_ONES, MAX_ONES, lowOnes, highOnes), "Ones");
		this.nonDigitRange = NamedSetting.of(new IntRange(MIN_NON_ONES_DIGITS, MAX_NON_ONES_DIGITS, lowNon, highNon), "Non-ones term digits");
		include121 = NamedSetting.of(new BooleanRef(DEFAULT_INCLUDE_121), "Include 121");
		settings = List.of(include121, onesRange, nonDigitRange);
	}
	
	@Override
	public Problem get() {
		System.out.printf("entered get, inc121 = %s, lowOnes=%d, high=%d, lowNon=%d, high=%d%n", include121(), lowOnes(), highOnes(), lowNonDigits(), highNonDigits());
		int ones = include121() && Math.random() < (1.0/(highOnes() - lowOnes() + 2))? 121 : Integer.parseInt("1".repeat(Problem.intInclusive(lowOnes(), highOnes())));
		int non = Problem.intWithDigits(Problem.intInclusive(lowNonDigits(), highNonDigits()));
		int first, second;
		if(Math.random() >= 0.5) {
			first = ones; second = non;
		}
		else {
			first = non; second = ones;
		}
		
		return SimpleExpression.multiplyTerms(first, second);
	}
	
	public int lowOnes() {
		return onesRange.ref().getLow();
	}
	
	public int highOnes() {
		return onesRange.ref().getHigh();
	}
	
	public int lowNonDigits() {
		return nonDigitRange.ref().getLow();
	}
	
	public int highNonDigits() {
		return nonDigitRange.ref().getHigh();
	}
	
	public boolean include121() {
		return include121.ref().get();
	}
}
