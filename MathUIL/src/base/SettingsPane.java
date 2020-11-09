package base;

import java.util.*;

import fxutils.*;
import javafx.collections.ObservableList;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import suppliers.ProblemSupplier;
import utils.ListRef;

/**
 * @author Sam Hooper
 *
 */
public class SettingsPane extends StackPane {
	
	
	private static final String ADD_SKILL_BUTTON_TEXT = "+ Add Skill",
								REMOVE_SUPPLIER_BUTTON_TEXT = "- Remove Skill",
								REMOVE_ALL_SUPPLIERS_BUTTON_TEXT = "- Remove All";
	
	private static final String STYLE_CLASS_NAME = "settings-pane";
	private static final String VBOX_STYLE_CLASS_NAME = "settings-pane-vbox";
	private static final String SETTINGS_BOX_STYLE_CLASS_NAME = "settings-box";
	private static final String TITLE_STYLE_CLASS_NAME = "settings-pane-title";
	private static final String SCROLL_PANE_STYLE_CLASS_NAME = "settings-scroll-pane";
	private final MainPane mainPane;
	private final VBox settingsBox;
	private final Label title;
	private final Button addSupplier, removeSupplier, removeAllSuppliers;
	
	private final VBox vBox;
	private final ScrollPane scrollPane;
	
	private boolean allowingRemoval;
	
	public SettingsPane(final MainPane mainPane) {
		this.mainPane = Objects.requireNonNull(mainPane);
		title = new Label("Settings");
		settingsBox = new VBox();
		removeSupplier = Buttons.of(REMOVE_SUPPLIER_BUTTON_TEXT, this::removeSupplierButtonAction);
		removeAllSuppliers = Buttons.of(REMOVE_ALL_SUPPLIERS_BUTTON_TEXT, this::removeAllSuppliersButtonAction);
		addSupplier = Buttons.of(ADD_SKILL_BUTTON_TEXT, this::showChooser);
		vBox = new VBox(title, addSupplier, removeSupplier, settingsBox);
		scrollPane = new ScrollPane(vBox);
		initSettings();
		initStyle();
		finishInit();
	}


	private void initSettings() {
		ListRef<ProblemSupplier> suppliers = mainPane.getProblemPane().getCompositeSupplier().suppliers();
		for(ProblemSupplier ps : suppliers)
			settingsBox.getChildren().add(SettingTitledPane.displayFor(ps, this));
		suppliers.addAddListener(this::supplierAdded);
		suppliers.addRemoveListener(this::supplierRemoved);
	}


	private void finishInit() {
		getChildren().add(scrollPane);
		minWidthProperty().bind(vBox.minWidthProperty());
		prefWidthProperty().bind(vBox.prefWidthProperty());
	}


	private void initStyle() {
		getStyleClass().add(STYLE_CLASS_NAME);
		vBox.getStyleClass().add(VBOX_STYLE_CLASS_NAME);
		title.getStyleClass().add(TITLE_STYLE_CLASS_NAME);
		settingsBox.getStyleClass().add(SETTINGS_BOX_STYLE_CLASS_NAME);
		scrollPane.getStyleClass().add(SCROLL_PANE_STYLE_CLASS_NAME);
	}


	private void supplierAdded(final ProblemSupplier ps) {
		settingsBox.getChildren().add(SettingTitledPane.displayFor(ps, this));
	}
	
	private void supplierRemoved(final ProblemSupplier ps) {
		for(Iterator<Node> iterator = settingsBox.getChildren().iterator(); iterator.hasNext();) {
			if(ps == ((SettingTitledPane) iterator.next()).getSupplier()) {
				iterator.remove();
				return;
			}
		}
		throw new IllegalStateException("Could not remove " + ps);
	}
	
	private void removeSupplierButtonAction() {
		if(isAllowingRemoval())
			disallowRemoval();
		else
			allowForRemoval();
	}
	
	private void removeAllSuppliersButtonAction() {
		ObservableList<Node> list = settingTitledPanesList();
		while(!list.isEmpty())
			((SettingTitledPane) list.get(0)).removeSelf(); //will trigger the removal of an element from list, so we use "!list.isEmpty" and "list.get(0)"
			//to avoid ConcurrentModificationException
		disallowRemoval();
	}
	
	private void allowForRemoval() {
		if(this.allowingRemoval)
			return;
		this.allowingRemoval = true;
		removeSupplier.setText("Done");
		for(Node node : settingsBox.getChildren())
			((SettingTitledPane) node).allowForRemoval();
		int addSupplierIndex = vBox.getChildren().indexOf(addSupplier);
		vBox.getChildren().set(addSupplierIndex, removeAllSuppliers);
	}
	
	private void disallowRemoval() {
		if(!this.allowingRemoval)
			return;
		this.allowingRemoval = false;
		removeSupplier.setText(REMOVE_SUPPLIER_BUTTON_TEXT);
		for(Node node : settingsBox.getChildren())
			((SettingTitledPane) node).disallowRemoval();
		int removeAllSuppliersIndex = vBox.getChildren().indexOf(removeAllSuppliers);
		vBox.getChildren().set(removeAllSuppliersIndex, addSupplier);
	}
	
	public boolean isAllowingRemoval() {
		return allowingRemoval;
	}
	
	private void showChooser() {
		disallowRemoval();
		mainPane.showChooser();
	}
	
	private ObservableList<Node> settingTitledPanesList() {
		return settingsBox.getChildren();
	}
	public MainPane getMainPane() {
		return mainPane;
	}
}
