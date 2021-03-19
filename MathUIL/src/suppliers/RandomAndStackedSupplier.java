package suppliers;

import java.util.*;

import problems.Problem;
import utils.EnumSetView;
import utils.refs.*;

/**
 * 
 * @author Sam Hooper
 */
public abstract class RandomAndStackedSupplier extends MultiModeSupplier {

	private static final EnumSetView<SupplierMode> RANDOM_AND_STACKED = EnumSetView.of(SupplierMode.RANDOM, SupplierMode.STACKED);
	
	protected final MutableIntRef stackedProblemsMaxIndex;
	protected int lastStackedProblemsIndex;
	protected List<Problem> stackedProblems;
	private final MutableBooleanRef stackedSupported;
	
	public RandomAndStackedSupplier() {
		stackedProblemsMaxIndex = new MutableIntRef(-1);
		lastStackedProblemsIndex = -1;
		stackedProblems = null;
		stackedSupported = new MutableBooleanRef(false);
	}

	
	@Override
	protected void settingsAdded() {
		stackedSupported.set(supportsStacked());
	}

	@Override
	public final EnumSetView<SupplierMode> getSupportedModesUnderAnySettings() {
		return RANDOM_AND_STACKED;
	}
	
	@Override
	public IntRef getStackedUnsolved() {
		if(getMode() != SupplierMode.STACKED) {
			throw new UnsupportedOperationException(String.format("Not in " + SupplierMode.STACKED.getDisplayName() + " mode"));
		}
		return stackedProblemsMaxIndex;
	}
	
	@Override
	public void strictlySolved(Problem p) {
		if(currentProblemMode == SupplierMode.STACKED) {
			if(stackedProblemsMaxIndex.get() <= 1) {
				stackedProblemsMaxIndex.set(stackedProblems.size());
			}
			else {
				stackedProblemsMaxIndex.decrement();
				Collections.swap(stackedProblems, lastStackedProblemsIndex, stackedProblemsMaxIndex.get());
			} 
		}
	}
	
	@Override
	public final Problem get() {
		currentProblemMode = getMode();
		return switch(currentProblemMode) {
			case RANDOM -> getRandom();
			case STACKED -> getStacked();
			default -> throw new IllegalStateException(String.format("Should not be in mode: %s", currentProblemMode));
		};
	}
	
	private Problem getStacked() {
		lastStackedProblemsIndex = Problem.intExclusive(stackedProblemsMaxIndex);
		return stackedProblems.get(lastStackedProblemsIndex);
	}
	
	@Override
	public BooleanRef supportsRef(SupplierMode mode) {
		return switch(mode) {
			case RANDOM -> BooleanRef.of(true);
			case STACKED -> stackedSupported;
			default -> BooleanRef.of(false);
		};
	}
	
	@Override
	public EnumSetView<SupplierMode> getSupportedModesUnderCurrentSettings() {
		return supportsStacked() ? RANDOM_AND_STACKED : RANDOM_ONLY;
	}
	
	@Override
	public boolean supportsUnderCurrentSettings(SupplierMode mode) {
		return mode == SupplierMode.RANDOM || mode == SupplierMode.STACKED && supportsStacked();
	}
	
	@Override
	public void settingsChanged() {
		super.settingsChanged();
		stackedSupported.set(supportsStacked());
	}

	protected abstract Problem getRandom();
	
	/** {@code true} if this {@link RandomAndStackedSupplier} supports {@link SupplierMode#STACKED} under its
	 * current settings, {@code false} otherwise.*/
	protected abstract boolean supportsStacked();
	
	protected abstract List<Problem> generateAllPossibleProblems();
	
	@Override
	public boolean setMode(SupplierMode newMode) {
		if(getMode() == newMode)
			return false;
		if(!supportsUnderCurrentSettings(newMode))
			throw new UnsupportedOperationException(String.format("%s is unsupported under the current settings"));
		switch(newMode) {
			case RANDOM -> stackedProblems = null; //set to null to conserve memory
			case STACKED -> {
				stackedProblems = generateAllPossibleProblems();
				stackedProblemsMaxIndex.set(stackedProblems.size());
			}
			default -> throw new UnsupportedOperationException(String.format("%s is unsupported under any settings", newMode));
		}
		modeRef.set(newMode);
		return true;
	}
}
