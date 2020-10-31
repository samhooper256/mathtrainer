package base;

import java.util.Objects;

import fxutils.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import suppliers.*;

/**
 * @author Sam Hooper
 *
 */
public class SupplierChooser extends StackPane {
	
	private final HBox hBox;
	private final MainPane mainPane;
	
	public SupplierChooser(MainPane mainPane) {
		Objects.requireNonNull(mainPane);
		this.mainPane = mainPane;
		hBox = new HBox(5);
		hBox.setPadding(new Insets(5));
		hBox.setBorder(Borders.of(Color.RED));
		hBox.setOnMouseClicked(mouseEvent -> {
			System.out.println("hBox clicked");
		});
		this.setBackground(Backgrounds.of(Color.LIGHTGRAY));
		for(ProblemSuppliers.Info info : ProblemSuppliers.getRegisteredInfos()) {
			hBox.getChildren().add(Buttons.of(info.getDisplayName(), () -> {
				mainPane.chooseSupplier(info.getFactory().get());
			}));
		}
		getChildren().add(hBox);
	}
}
