package base;

import java.util.*;

import fxutils.*;
import javafx.geometry.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;

/**
 * @author Sam Hooper
 *
 */
public class SettingsPane extends VBox {
	
	private MainPane mainPane;
	
	public SettingsPane(MainPane mainPane) {
		Objects.requireNonNull(mainPane);
		this.mainPane = mainPane;
		setPadding(new Insets(5));
		setBorder(Borders.of(Color.LIGHTGRAY, new CornerRadii(5)));
		ProblemPane problemPane = mainPane.getProblemPane();
		CompositeProblemSupplier supplier = problemPane.getSupplier();
		for(ProblemSupplier ps : supplier.suppliers())
			getChildren().add(SettingTitledPane.displayFor(ps));
		supplier.suppliers().addAddListener(ps -> getChildren().add(SettingTitledPane.displayFor(ps)));
		
	}
	
	public MainPane getMainPane() {
		return mainPane;
	}
}
