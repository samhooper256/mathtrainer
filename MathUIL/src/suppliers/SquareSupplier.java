package suppliers;

import java.util.*;

import base.*;
import problems.*;
import utils.*;

/**
 * @author Sam Hooper
 *
 */
public class SquareSupplier implements ProblemSupplier {
	
	private static final int MIN_BASE = 0, MAX_BASE = 200;
	public static final int DEFAULT_MIN_BASE = 1, DEFAULT_MAX_BASE = 30;
	
	private final NamedSetting<IntRange> baseRange;
	private final List<Ref> settings;
	
	public SquareSupplier() {
		this(DEFAULT_MIN_BASE, DEFAULT_MAX_BASE);
	}
	
	public SquareSupplier(int minBase, int maxBase) {
		this.baseRange = NamedSetting.of(new IntRange(MIN_BASE, MAX_BASE, minBase, maxBase), "Base");
		this.settings = new ArrayList<>(1);
		Collections.addAll(settings, baseRange);
	}

	@Override
	public SinglePower get() {
		return new SinglePower((int) (Math.random() * (maxBase() + 1 - minBase()) + minBase()), 2);
	}
	
	@Override
	public List<Ref> settings() {
		return settings;
	}
	
	public int minBase() {
		return baseRange.ref().getLow();
	}
	
	public int maxBase() {
		return baseRange.ref().getHigh();
	}
}
