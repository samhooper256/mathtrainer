package base;

import java.util.*;

import fxutils.*;
import javafx.collections.ObservableList;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import suppliers.CompositeProblemSupplier;
import suppliers.ProblemSupplier;

/**
 * @author Sam Hooper
 *
 */
public class SettingsPane extends VBox {
	
	private static final int DEFAULT_PADDING = 5;
	private static final int DEFAULT_SPACING = 5;
	private final MainPane mainPane;
	
	public SettingsPane(MainPane mainPane) {
		super(DEFAULT_SPACING);
		Objects.requireNonNull(mainPane);
		this.mainPane = mainPane;
		setPadding(new Insets(DEFAULT_PADDING));
		setBorder(Borders.of(Color.LIGHTGRAY, new CornerRadii(5)));
		ProblemPane problemPane = mainPane.getProblemPane();
		CompositeProblemSupplier supplier = problemPane.getSupplier();
		for(ProblemSupplier ps : supplier.suppliers())
			getChildren().add(SettingTitledPane.displayFor(ps));
		supplier.suppliers().addAddListener(this::supplierAdded);
		
		getChildren().add(createAddSupplierButton());
	}
	
	private void supplierAdded(final ProblemSupplier ps) {
		final ObservableList<Node> children = getChildren();
		int addIndex = children.size() - 1;
		while(!(children.get(addIndex) instanceof SettingTitledPane)) {
			addIndex--;
		}
		children.add(addIndex + 1, SettingTitledPane.displayFor(ps));
	}
	
	private Node createAddSupplierButton() {
		Button b = Buttons.of("+ Add Problem Type", this::showChooser);
		return b;
	}
	
	private void showChooser() {
		mainPane.showChooser();
	}
}
