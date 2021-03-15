package base;

import java.util.*;
import java.util.function.Supplier;

import org.controlsfx.control.RangeSlider;

import fxutils.*;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import math.Utils;
import suppliers.*;
import utils.*;
import utils.refs.*;

/**
 * @author Sam Hooper
 *
 */
public class SettingTitledPane extends TitledPane {
	
	private ProblemSupplier problemSupplier;
	private final SettingsPane settingsPane;
	private final Button removeButton;
	private final List<ModeRadioButton> modeRadioButtons;
	
	/**
	 * @throws NullPointerException if {@code supplier} is {@code null}.
	 */
	public static SettingTitledPane displayFor(ProblemSupplier supplier, SettingsPane settingsPane) {
		return new SettingTitledPane(supplier, settingsPane); //throws the NPE if necessary
	}
	
	/**
	 * @throws NullPointerException if {@code supplier} or {@code settingsPane} is {@code null}.
	 */
	private SettingTitledPane(final ProblemSupplier supplier, final SettingsPane settingsPane) {
		System.out.printf("NEW STP!%n");
		this.problemSupplier = Objects.requireNonNull(supplier);
		this.settingsPane = Objects.requireNonNull(settingsPane);
		this.removeButton = Buttons.of("X", this::removeSelf);
		this.modeRadioButtons = new ArrayList<>();
		this.setText(supplier.getName());
		VBox vBox = new VBox();
		setContent(vBox);
		if(supplier.settings().isEmpty()) {
			setExpanded(false);
			setCollapsible(false);
		}
		else {
			for(Ref ref : supplier.settings()) {
				vBox.getChildren().add(displayNodeForRef(ref));
			}
		}
		final EnumSetView<SupplierMode> supported = supplier.getSupportedModesUnderAnySettings();
		supplier.setMode(SupplierMode.RANDOM);
		if(supported.size() > 1) {
			final EnumSetView<SupplierMode> currentSupported = supplier.getSupportedModesUnderCurrentSettings();
			ToggleGroup group = new ToggleGroup();
			for(SupplierMode mode : supported) {
				final String buttonText = currentSupported.contains(mode) ? mode.getDisplayName() : getUnsupportedModeDisplay(mode);
				ModeRadioButton button = mode == SupplierMode.STACKED ? new StackedModeRadioButton(buttonText) : new ModeRadioButton(mode, buttonText);
				button.setOnAction(eventHandler -> setSelectedMode(mode));
				
				if(mode == SupplierMode.RANDOM) {
					button.setSelected(true);
				}
				button.setToggleGroup(group);
				modeRadioButtons.add(button);
				vBox.getChildren().add(button);
			}
//			System.out.printf("adding listener to %s%n", supplier.getModeRef());
			supplier.getModeRef().addChangeListener((ov, nv) -> {
				System.out.printf("entered mode ref change listener%n");
				setDisplayedSelectedMode(nv);
			});
		}
	}
	
	private static class ModeRadioButton extends RadioButton {

		private final SupplierMode mode;
		
		public ModeRadioButton(final SupplierMode mode, final String text) {
			super(text);
			this.mode = mode;
		}
		
		public SupplierMode getMode() {
			return mode;
		}
		
	}
	
	private static class StackedModeRadioButton extends ModeRadioButton {
	
		
		public StackedModeRadioButton(final String text) {
			super(SupplierMode.STACKED, text);
			setText(text);
		}
		
		public boolean isDisplayingUnsolvedNumber() {
			return getText().endsWith(")");
		}
		
		public void setUnsolvedNumber(int num) {
			if(isDisplayingUnsolvedNumber())
				setText(Regex.DIGITS.matcher(getText()).replaceAll(String.valueOf(num)));
			else
				setText(String.format("%s (%d)", getText(), num));
		}
		
		public void clearUnsolvedNumber() {
			if(isDisplayingUnsolvedNumber())
				setText(getText().substring(0, getText().indexOf('(') - 1));
		}
		
	}
	
	private class StackedListener implements IntChangeListener {
		
		@Override
		public void changed(int oldValue, int newValue) {
			getStackedModeRadioButton().setUnsolvedNumber(newValue);
		}
		
	}
	
	private StackedModeRadioButton getStackedModeRadioButton() {
		return (StackedModeRadioButton) modeRadioButtonForOrThrow(SupplierMode.STACKED);
	}
	
	private ModeRadioButton modeRadioButtonForOrThrow(final SupplierMode mode) {
		for(ModeRadioButton button : modeRadioButtons) {
			if(button.getMode() == mode) {
				return button;
			}
		}
		throw new IllegalArgumentException(String.format("Button for mode %s not present.", mode.getDisplayName()));
	}
	
	/** Returns the {@link ModeRadioButton} corresponding to {@code newMode}.*/
	private ModeRadioButton setDisplayedSelectedMode(SupplierMode newMode) {
		System.out.printf("[enter] setDisplayedSelectedMode(newMode=%s)%n",newMode);
		final ModeRadioButton button = modeRadioButtonForOrThrow(newMode);
		button.setSelected(true);
		return button;
	}
	
	
	private void setSelectedMode(SupplierMode newMode) {
		System.out.printf("[enter] setSelectedMode(newMode=%s)%n",newMode);
		final ModeRadioButton button = setDisplayedSelectedMode(newMode);
		if(!problemSupplier.setMode(newMode)) {
			System.out.printf("\tfalse, returning%n");
			return; //we already have the right mode.
		}
		if(newMode == SupplierMode.STACKED) {
			System.out.printf("\tnewMode == STACKED%n");
			StackedModeRadioButton stackedButton = (StackedModeRadioButton) button;
			final IntRef unsolvedRef = problemSupplier.getStackedUnsolved();
			Collection<IntChangeListener> changeListeners = unsolvedRef.getChangeListenersUnmodifiable();
			if(!Colls.containsInstanceOf(changeListeners, StackedListener.class))
				unsolvedRef.addChangeListener(new StackedListener());
			stackedButton.setUnsolvedNumber(unsolvedRef.get());
		}
		else if(problemSupplier.supportsUnderAnySettings(SupplierMode.STACKED)) {
			((StackedModeRadioButton) (modeRadioButtonForOrThrow(SupplierMode.STACKED))).clearUnsolvedNumber();
		}
	}
	
	private static String getUnsupportedModeDisplay(SupplierMode mode) {
		return String.format("%s (unsupported under current settings)", mode.getDisplayName());
	}
	
	/**
	 * <p>Creates and returns {@link Node} that will display the value(s) of the given {@link Ref} and allow the user
	 * to adjust those values.</p>
	 * <p>This method supports the following types and their subtypes:
	 * <ul>
	 * <li>{@link IntRange}</li>
	 * <li>{@link BooleanRef}</li>
	 * </ul>
	 * </p>
	 */
	public static Node displayNodeForRef(Ref ref) {
//		System.out.printf("Building display node for ref=%s%n", ref);
		VBox vBox = new VBox(2);
		String name = "";
		if(ref instanceof NamedSetting<?>) {
			NamedSetting<?> ns = (NamedSetting<?>) ref;
			name = ((NamedSetting<?>) ref).getName();
			ref = ns.ref();
		}
		if(ref instanceof IntRange) {
			vBox.getChildren().add(new Label(name));
			
			IntRange ir = (IntRange) ref;
			final Pane rangeSlider = new IntRangeBox(ir);
			vBox.getChildren().add(rangeSlider);
		}
		else if(ref instanceof BooleanRef) {
			BooleanRef br = (BooleanRef) ref;
			vBox.getChildren().add(new BoolBox(br, name));
		}
		else {
			throw new UnsupportedOperationException("Unsupported setting type: " + ref.getClass());
		}
		return vBox;
	}
	
	private static class IntRangeBox extends HBox {
		
		private final RangeSlider slider;
		private final TextField low, high;
		
		IntRangeBox(final IntRange range) {
			super();			
			this.slider = new RangeSlider();
			HBox.setHgrow(slider, Priority.ALWAYS);
			setAlignment(Pos.CENTER);
			final int rangeMax = range.getMax();
			final int textFieldWidth = 10 + 15 * Utils.magnitude(rangeMax);
			low = new TextField(Integer.toString(range.getLow()));
			low.setMinWidth(10);
			low.setPrefWidth(textFieldWidth);
			low.setMaxWidth(80);
			high = new TextField(Integer.toString(range.getHigh()));
			high.setMinWidth(10);
			high.setPrefWidth(textFieldWidth);
			high.setMaxWidth(80);
			slider.setMin(range.getMin());
			slider.setMax(rangeMax);
			slider.setHighValue(range.getHigh());
			slider.setLowValue(range.getLow());
			slider.setHighValue(range.getHigh()); //this line is intentional. 
			slider.lowValueProperty().addListener((ov, oldV, newV) -> {
				final double doubleValue = newV.doubleValue();
				if(isInt(doubleValue)) {
					final int actualValue = (int) doubleValue;
					range.setLow(actualValue);
					low.setText(Integer.toString(actualValue));
				}
			});
			slider.highValueProperty().addListener((ov, oldV, newV) -> {
				final double doubleValue = newV.doubleValue();
				if(isInt(doubleValue)) {
					final int actualValue = (int) doubleValue;
					range.setHigh(actualValue);
					high.setText(Integer.toString(actualValue));
				}
			});
			low.textProperty().addListener((ov, oldText, newText) -> {
				try {
					if(Utils.isInteger(newText)) {
						int val = Integer.parseInt(newText);
						if(val <= range.getHigh())
							slider.setLowValue(val);
						else {
							slider.setLowValue(slider.getHighValue());
						}
					}
				}
				catch(Exception e) {}
			});
			high.textProperty().addListener((ov, oldText, newText) -> {
				try {
					if(Utils.isInteger(newText)) {
						int val = Integer.parseInt(newText);
						if(val >= range.getLow())
							slider.setHighValue(val);
						else {
							slider.setHighValue(slider.getLowValue());
						}
					}
				}
				catch(Exception e) {}
			});
			
			slider.setShowTickLabels(true);
			slider.setMajorTickUnit(Math.max(1, range.maxValueRange() / 10));
			slider.setMinorTickCount(0);
			slider.setSnapToTicks(true);
			range.lowRef().addChangeListener((ov, nv) -> {
				slider.setLowValue(nv);
			});
			range.highRef().addChangeListener((ov, nv) -> {
				slider.setHighValue(nv);
			});
			
			getChildren().addAll(low, slider, high);
		}
	}
	
	private static class BoolBox extends Label {
		
		public BoolBox(final BooleanRef ref, final String text) {
			super(text);
			CheckBox checkBox = new CheckBox();
			checkBox.setSelected(ref.get());
			checkBox.selectedProperty().addListener((obs, ov, nv) -> {
				ref.set(nv);
			});
			this.setGraphic(checkBox);
			this.setContentDisplay(ContentDisplay.LEFT);
		}
	}
	
	private static boolean isInt(final double d) {
		return d % 1 == 0;
	}
	
	public ProblemSupplier getSupplier() {
		return problemSupplier;
	}

	/**
	 * Lets this {@link SettingTitledPane} be removed.
	 */
	public void allowForRemoval() {
		this.setGraphic(removeButton);
	}
	
	public void removeSelf() {
		boolean removed = settingsPane.getMainPane().getProblemPane().getCompositeSupplier().removeSupplier(problemSupplier);
		if(!removed)
			throw new IllegalStateException("Unable to remove this SettingTitledPane");
	}

	public void disallowRemoval() {
		this.setGraphic(null);
	}
}
