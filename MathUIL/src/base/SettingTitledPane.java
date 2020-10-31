package base;

import java.util.Objects;

import org.controlsfx.control.RangeSlider;

import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import suppliers.ProblemSupplier;
import suppliers.ProblemSuppliers;
import utils.*;

/**
 * @author Sam Hooper
 *
 */
public class SettingTitledPane extends TitledPane {
	
	private ProblemSupplier problemSupplier;
	
	/**
	 * @throws NullPointerException if {@code supplier} is {@code null}.
	 */
	public static Node displayFor(ProblemSupplier supplier) {
		return new SettingTitledPane(supplier); //throws the NPE if necessary
	}
	
	/**
	 * The given {@link ProblemSupplier} must not be {@code null}.
	 * @throws NullPointerException if {@code supplier} is {@code null}.
	 */
	private SettingTitledPane(ProblemSupplier supplier) {
		Objects.requireNonNull(supplier);
		String name = ProblemSuppliers.nameOf(supplier.getClass());
		this.setText(name);
		VBox vBox = new VBox();
		setContent(vBox);
		for(Ref ref : supplier.settings()) {
			vBox.getChildren().add(forRef(ref)); 
		}
	}
	
	private static Node forRef(Ref ref) {
		VBox vBox = new VBox(2);
		if(ref instanceof NamedSetting<?>) {
			NamedSetting<?> ns = (NamedSetting<?>) ref;
			vBox.getChildren().add(new Label(((NamedSetting<?>) ref).getName()));
			ref = ns.ref();
		}
		if(ref instanceof IntRange) {
			IntRange ir = (IntRange) ref;
			final RangeSlider rangeSlider = new IntRangeSlider(ir);
			vBox.getChildren().add(rangeSlider);
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
			this.setLowValue(range.getLow());
			this.setHighValue(range.getHigh());
			this.lowValueProperty().addListener((ov, oldV, newV) -> {
				final double val = newV.doubleValue();
				if(isInt(val))
					range.setLow((int) val);
			});
			this.highValueProperty().addListener((ov, oldV, newV) -> {
				final double val = newV.doubleValue();
				if(isInt(val))
					range.setHigh((int) val);
			});
			
			this.setShowTickLabels(true);
			this.setMajorTickUnit(1);
			this.setMinorTickCount(0);
			this.setSnapToTicks(true);
		}
	}
	
	private static boolean isInt(final double d) {
		return d % 1 != 0;
	}
	
	public ProblemSupplier getSupplier() {
		return problemSupplier;
	}
}
