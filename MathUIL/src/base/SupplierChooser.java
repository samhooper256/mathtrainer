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
	private final FlowPane flowPane;
	private final HBox bottomBox;
	private final Button addSelectedButton;
	private final Label title;
	private final MainPane mainPane;
	
	private class SupplierButton extends Button {
		
		private final ProblemSuppliers.Info info;
		private boolean desired;
		SupplierButton(final ProblemSuppliers.Info info) {
			this.info = info;
			this.desired = false;
			this.setText(info.getDisplayName());
			this.setOnAction(actionEvent -> toggleDesire());
		}
		
		void toggleDesire() {
			if(desired) {
				desired = false;
				this.setStyle("");				
			}
			else {
				desired = true;
				this.setStyle("-fx-border-color: green; -fx-border-width: 3;");
			}
		}
		
		ProblemSuppliers.Info getInfo() {
			return info;
		}
		
		boolean isDesired() {
			return desired;
		}

		@Override
		public String toString() {
			return "SupplierButton[info=" + info + ", desired=" + desired + "]";
		}
		
		
	}
	public SupplierChooser(MainPane mainPane) {
		Objects.requireNonNull(mainPane);
		this.mainPane = mainPane;
		this.getStyleClass().add(STYLE_CLASS_NAME);
		title = new Label("Add Problem Type");
		title.getStyleClass().add(TITLE_STYLE_CLASS);
		
		vBox = new VBox(10);
		flowPane = new FlowPane();
		flowPane.setPadding(new Insets(5));
		for(ProblemSuppliers.Info info : ProblemSuppliers.getRegisteredInfos()) {
			final Button b = new SupplierButton(info);
			flowPane.getChildren().add(b);
		}
		
		addSelectedButton = Buttons.of("Add Selected", this::addDesired);
		bottomBox = new HBox(addSelectedButton);
		
		vBox.getChildren().addAll(title, flowPane, bottomBox);
		getChildren().add(vBox);
	}
	
	private void addDesired() {
		mainPane.hideChooser();
		for(Node n : flowPane.getChildren()) {
			SupplierButton sb = (SupplierButton) n;
			if(sb.isDesired()) {
				mainPane.addSupplierOrThrow(sb.getInfo().getFactory().get());
				sb.toggleDesire();
			}
		}
	}

	/**
	 * 
	 */
	public void show() {
		System.out.printf("SupplierChooser => show()%n");
		System.out.printf("\t%s%n", mainPane.getProblemPane().getSupplier().suppliers());
		for(final Node n : flowPane.getChildren()) {
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
