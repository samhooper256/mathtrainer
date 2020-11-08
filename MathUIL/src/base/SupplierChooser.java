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
	
	private static final String TITLE_TEXT = "Add Skill";
	private static final String TITLE_STYLE = "title", VBOX_STYLE = "vbox", SCROLL_PANE_STYLE = "scroll-pane", FLOW_PANE_STYLE = "flow-pane",
			UI_BUTTON_STYLE = "ui-button", BOTTOM_BOX_STYLE = "bottom-box";
	/**
	 * CSS Style class name
	 */
	private static final String STYLE_CLASS_NAME = "supplier-chooser";
	
	/** The bottom and only layer of this {@link StackPane}.*/
	private final VBox rootVBox;
	private final ScrollPane scroll;
	private final FlowPane flowPane;
	private final HBox bottomBox;
	private final Button addSelectedButton, selectAllButton, cancelButton;
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
			return String.format("SupplierButton[info=%s, desired=%s]", info, desired);
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
		this.mainPane = Objects.requireNonNull(mainPane);
		title = new Label(TITLE_TEXT);
		rootVBox = new VBox();
		flowPane = new FlowPane();
		scroll = new ScrollPane(flowPane);
		addSelectedButton = Buttons.of("Add Selected", this::addDesired);
		selectAllButton = Buttons.of("Select all", this::desireAll);
		cancelButton = Buttons.of("Cancel", this::cancelButtonAction);
		bottomBox = new HBox(addSelectedButton, selectAllButton, cancelButton);
		createSupplierButtons();
		initStyles();
		finishInit();
	}

	private void createSupplierButtons() {
		for(ProblemSuppliers.Info info : ProblemSuppliers.getRegisteredInfos()) {
			final Button b = new SupplierButton(info);
			flowPane.getChildren().add(b);
		}
	}

	private void finishInit() {
		rootVBox.getChildren().addAll(title, scroll, bottomBox);
		getChildren().add(rootVBox);
	}

	private void initStyles() {
		getStyleClass().add(STYLE_CLASS_NAME);
		title.getStyleClass().add(TITLE_STYLE);
		rootVBox.getStyleClass().add(VBOX_STYLE);
		flowPane.getStyleClass().add(FLOW_PANE_STYLE);
		scroll.getStyleClass().add(SCROLL_PANE_STYLE);
		addSelectedButton.getStyleClass().add(UI_BUTTON_STYLE);
		addSelectedButton.getStyleClass().add(UI_BUTTON_STYLE);
		cancelButton.getStyleClass().add(UI_BUTTON_STYLE);
		bottomBox.getStyleClass().add(BOTTOM_BOX_STYLE);
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
	
	private void cancelButtonAction() {
		mainPane.hideChooser();
	}

	public void show() {
		for(final Node n : buttonList()) {
			SupplierButton b = (SupplierButton) n;
			if(mainPane.getProblemPane().hasSupplierOfClass(b.getInfo().getSupplierClass()))
				b.setDisable(true);
			else
				b.setDisable(false);
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
