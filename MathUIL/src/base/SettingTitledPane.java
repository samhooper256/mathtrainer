package base;

import java.util.Objects;

import org.controlsfx.control.RangeSlider;

import fxutils.Buttons;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import suppliers.NamedSetting;
import suppliers.ProblemSupplier;
import suppliers.ProblemSuppliers;
import utils.*;

/**
 * @author Sam Hooper
 *
 */
public class SettingTitledPane extends TitledPane {
	
	private ProblemSupplier problemSupplier;
	private final SettingsPane settingsPane;
	private final Button removeButton;
	/**
	 * @throws NullPointerException if {@code supplier} is {@code null}.
	 */
	public static SettingTitledPane displayFor(ProblemSupplier supplier, SettingsPane settingsPane) {
		return new SettingTitledPane(supplier, settingsPane); //throws the NPE if necessary
	}
	
	/**
	 * The given {@link ProblemSupplier} must not be {@code null}.
	 * @throws NullPointerException if {@code supplier} or {@code settingsPane} is {@code null}.
	 */
	private SettingTitledPane(final ProblemSupplier supplier, final SettingsPane settingsPane) {
		this.problemSupplier = Objects.requireNonNull(supplier);
		this.settingsPane = Objects.requireNonNull(settingsPane);
		this.removeButton = Buttons.of("X", this::removeSelf);
		this.setText(ProblemSuppliers.nameOf(supplier.getClass()));
		VBox vBox = new VBox();
		setContent(vBox);
		if(supplier.settings().isEmpty()) {
			setExpanded(false);
			setCollapsible(false);
		}
		else
			for(Ref ref : supplier.settings())
				vBox.getChildren().add(displayNodeForRef(ref));
		
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
			final RangeSlider rangeSlider = new IntRangeSlider(ir);
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
	
	private static class IntRangeSlider extends RangeSlider {
		
		private final IntRange range;
		
		IntRangeSlider(final IntRange range) {
			this.range = range;
			this.setMin(range.getMin());
			this.setMax(range.getMax());
			this.setHighValue(range.getHigh());
			this.setLowValue(range.getLow());
			this.setHighValue(range.getHigh()); //this line is intentional. 
			this.lowValueProperty().addListener((ov, oldV, newV) -> {
				final double val = newV.doubleValue();
				if(isInt(val)) {
//					System.out.printf("setting low: %d%n", (int) val);
					range.setLow((int) val);
				}
			});
			this.highValueProperty().addListener((ov, oldV, newV) -> {
				final double val = newV.doubleValue();
				if(isInt(val)) {
//					System.out.printf("setting high: %d%n", (int) val);
					range.setHigh((int) val);
				}
			});
			
			this.setShowTickLabels(true);
			this.setMajorTickUnit(1);
			this.setMinorTickCount(0);
			this.setSnapToTicks(true);
			
			range.lowRef().addChangeListener((ov, nv) -> {
				this.setLowValue(nv);
			});
			range.highRef().addChangeListener((ov, nv) -> {
				this.setHighValue(nv);
			});
		}
	}
	
	private static class BoolBox extends Label {
		
		private final BooleanRef ref;
		
		public BoolBox(final BooleanRef ref, final String text) {
			super(text);
			this.ref = ref;
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
