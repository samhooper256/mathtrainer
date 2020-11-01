package base;

import java.util.Objects;

import fxutils.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import suppliers.*;

/**
 * @author Sam Hooper
 *
 */
public class SupplierChooser extends StackPane {
	
	private static final String TITLE_STYLE_CLASS = "title";
	/**
	 * CSS Style class name
	 */
	private static final String STYLE_CLASS_NAME = "supplier-chooser";
	
	private final VBox vBox;
	private final HBox hBox;
	private final Label title;
	private final MainPane mainPane;
	
	private class SupplierButton extends Button {
		
		private final ProblemSuppliers.Info info;
		
		SupplierButton(final ProblemSuppliers.Info info) {
			this.info = info;
			this.setText(info.getDisplayName());
			this.setOnAction(actionEvent -> {
				mainPane.chooseSupplier(info.getFactory().get());
			});
		}
		
		ProblemSuppliers.Info getInfo() {
			return info;
		}
	}
	public SupplierChooser(MainPane mainPane) {
		Objects.requireNonNull(mainPane);
		this.mainPane = mainPane;
		this.getStyleClass().add(STYLE_CLASS_NAME);
		title = new Label("Add Problem Type");
		title.getStyleClass().add(TITLE_STYLE_CLASS);
		
		vBox = new VBox(10);
		hBox = new HBox(5);
		hBox.setPadding(new Insets(5));
		for(ProblemSuppliers.Info info : ProblemSuppliers.getRegisteredInfos()) {
			final Button b = new SupplierButton(info);
			hBox.getChildren().add(b);
		}
		
		vBox.getChildren().addAll(title, hBox);
		getChildren().add(vBox);
	}

	/**
	 * 
	 */
	public void show() {
		System.out.printf("SupplierChooser => show()%n");
		System.out.printf("\t%s%n", mainPane.getProblemPane().getSupplier().suppliers());
		for(final Node n : hBox.getChildren()) {
			SupplierButton b = (SupplierButton) n;
			if(mainPane.getProblemPane().hasSupplierOfClass(b.getInfo().getSupplierClass())) {
				b.setDisable(true);
			}
			else {
				b.setDisable(false);
			}
		}
		this.setVisible(true);
	}
	
	public void hide() {
		this.setVisible(false);
	}
}
