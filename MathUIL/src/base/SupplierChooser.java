package base;

import java.util.Objects;

import fxutils.*;
import javafx.collections.ObservableList;
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
	
	private static final String TITLE_STYLE = "title", VBOX_STYLE = "vbox", SCROLL_PANE_STYLE = "scroll-pane", FLOW_PANE_STYLE = "flow-pane",
			UI_BUTTON_STYLE = "ui-button", BOTTOM_BOX_STYLE = "bottom-box";
	/**
	 * CSS Style class name
	 */
	private static final String STYLE_CLASS_NAME = "supplier-chooser";
	
	private final VBox vBox;
	private final ScrollPane scroll;
	private final FlowPane flowPane;
	private final HBox bottomBox;
	private final Button addSelectedButton, selectAllButton;
	private final Label title;
	private final MainPane mainPane;
	
	private class SupplierButton extends Button {
		
		private final ProblemSuppliers.Info info;
		private boolean desired;
		SupplierButton(final ProblemSuppliers.Info info) {
			this.info = info;
			this.desired = false;
			this.getStyleClass().add("supplier-button");
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
		
		public void setDesired(boolean b) {
			if(b ^ desired)
				toggleDesire();
		}
		
		/**
		 * Sets the {@link #isDesired() desired} status of this {@link SupplierButton} to the given value <b>only</b> if this {@link SupplierButton} is not
		 * {@link #isDisabled() disabled}. If this {@link SupplierButton} is disabled, does nothing.
		 */
		public void setDesiredIfEnabled(boolean b) {
			if(!isDisabled())
				setDesired(b);
		}
		
		
	}
	public SupplierChooser(MainPane mainPane) {
		Objects.requireNonNull(mainPane);
		this.mainPane = mainPane;
		this.getStyleClass().add(STYLE_CLASS_NAME);
		title = new Label("Add Problem Type");
		title.getStyleClass().add(TITLE_STYLE);
		
		vBox = new VBox();
		vBox.getStyleClass().add(VBOX_STYLE);
		flowPane = new FlowPane();
		flowPane.getStyleClass().add(FLOW_PANE_STYLE);
		scroll = new ScrollPane(flowPane);
		scroll.getStyleClass().add(SCROLL_PANE_STYLE);
		for(ProblemSuppliers.Info info : ProblemSuppliers.getRegisteredInfos()) {
			final Button b = new SupplierButton(info);
			flowPane.getChildren().add(b);
		}
		
		addSelectedButton = Buttons.of("Add Selected", this::addDesired);
		addSelectedButton.getStyleClass().add(UI_BUTTON_STYLE);
		selectAllButton = Buttons.of("Select all", this::desireAll);
		addSelectedButton.getStyleClass().add(UI_BUTTON_STYLE);
		bottomBox = new HBox(addSelectedButton, selectAllButton);
		bottomBox.getStyleClass().add(BOTTOM_BOX_STYLE);
		vBox.getChildren().addAll(title, scroll, bottomBox);
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

	public void show() {
//		System.out.printf("SupplierChooser => show()%n");
//		System.out.printf("\t%s%n", mainPane.getProblemPane().getSupplier().suppliers());
		for(final Node n : buttonList()) {
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
	
	private void desireAll() {
		for(Node n : buttonList()) {
			SupplierButton b = (SupplierButton) n;
			b.setDesiredIfEnabled(true);
		}
	}
	
	private ObservableList<Node> buttonList() {
		return flowPane.getChildren();
	}
	
	public void hide() {
		this.setVisible(false);
	}
}
