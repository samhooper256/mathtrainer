package base;

import java.util.*;

import fxutils.*;
import javafx.collections.ObservableList;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.text.Font;
import suppliers.CompositeProblemSupplier;
import suppliers.ProblemSupplier;

/**
 * @author Sam Hooper
 *
 */
public class SettingsPane extends StackPane {
	
	private static final String REMOVE_SUPPLIER_BUTTON_TEXT = "- Remove Problem Type";
	
	private static final String STYLE_CLASS_NAME = "settings-pane";
	private static final String VBOX_STYLE_CLASS_NAME = "settings-pane-vbox";
	private static final String SETTINGS_BOX_STYLE_CLASS_NAME = "settings-box";
	private static final String TITLE_STYLE_CLASS_NAME = "settings-pane-title";
	private static final String SCROLL_PANE_STYLE_CLASS_NAME = "settings-scroll-pane";
	private final MainPane mainPane;
	private final VBox settingsBox;
	private final Label title;
	private final Button removeSupplierButton;
	
	private final VBox vBox;
	private final ScrollPane scrollPane;
	
	private boolean allowingRemoval;
	public SettingsPane(MainPane mainPane) {
		this.getStyleClass().add(STYLE_CLASS_NAME);
		vBox = new VBox();
		Objects.requireNonNull(mainPane);
		vBox.getStyleClass().add(VBOX_STYLE_CLASS_NAME);
		this.mainPane = mainPane;
		this.title = new Label("Settings");
//		title.getStyleClass().add(TITLE_STYLE_CLASS_NAME);
		this.settingsBox = new VBox();
//		this.settingsBox.getStyleClass().add(SETTINGS_BOX_STYLE_CLASS_NAME);
		ProblemPane problemPane = mainPane.getProblemPane();
		CompositeProblemSupplier supplier = problemPane.getCompositeSupplier();
		for(ProblemSupplier ps : supplier.suppliers())
			settingsBox.getChildren().add(SettingTitledPane.displayFor(ps, this));
		supplier.suppliers().addAddListener(this::supplierAdded);
		supplier.suppliers().addRemoveListener(this::supplierRemoved);
		
		this.removeSupplierButton = Buttons.of(REMOVE_SUPPLIER_BUTTON_TEXT, this::removeSupplierButtonAction);
		vBox.getChildren().addAll(title, settingsBox, createAddSupplierButton(), removeSupplierButton);
		this.scrollPane = new ScrollPane(vBox);
		this.scrollPane.getStyleClass().add(SCROLL_PANE_STYLE_CLASS_NAME);
		getChildren().add(scrollPane);
		this.minWidthProperty().bind(vBox.minWidthProperty());
		this.prefWidthProperty().bind(vBox.prefWidthProperty());
		vBox.setBorder(Borders.of(Color.BLUE));
	}


	@Override
	protected double computePrefHeight(double width) {
		// TODO Auto-generated method stub
		return super.computePrefHeight(width);
	}


	private void supplierAdded(final ProblemSupplier ps) {
		settingsBox.getChildren().add(SettingTitledPane.displayFor(ps, this));
	}
	
	private void supplierRemoved(final ProblemSupplier ps) {
		for(Iterator<Node> iterator = settingsBox.getChildren().iterator(); iterator.hasNext();) {
			Node n = iterator.next();
			SettingTitledPane stp = (SettingTitledPane) n;
			if(ps == stp.getSupplier()) {
				iterator.remove();
				return;
			}
		}
		throw new IllegalStateException("Could not remove " + ps);
	}
	
	private Node createAddSupplierButton() {
		Button b = Buttons.of("+ Add Problem Type", this::showChooser);
		return b;
	}
	
	private void removeSupplierButtonAction() {
		if(allowingRemoval())
			disallowRemoval();
		else
			allowForRemoval();
	}
	private void allowForRemoval() {
		if(this.allowingRemoval)
			return;
		this.allowingRemoval = true;
		removeSupplierButton.setText("Done");
		for(Node node : settingsBox.getChildren()) {
			SettingTitledPane stp = (SettingTitledPane) node;
			stp.allowForRemoval();
		}
	}
	
	private void disallowRemoval() {
		if(!this.allowingRemoval)
			return;
		this.allowingRemoval = false;
		removeSupplierButton.setText(REMOVE_SUPPLIER_BUTTON_TEXT);
		for(Node node : settingsBox.getChildren()) {
			SettingTitledPane stp = (SettingTitledPane) node;
			stp.disallowRemoval();
		}
	}
	
	public boolean allowingRemoval() {
		return allowingRemoval;
	}
	
	private void showChooser() {
		disallowRemoval();
		mainPane.showChooser();
	}
	
	public MainPane getMainPane() {
		return mainPane;
	}
}
