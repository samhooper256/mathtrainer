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
public class SettingsPane extends VBox {
	
	private static final String REMOVE_SUPPLIER_BUTTON_TEXT = "- Remove Problem Type";
	/**
	 * CSS Style class name
	 */
	private static final String STYLE_CLASS_NAME = "settings-pane";
	private static final int DEFAULT_PADDING = 5;
	private static final int DEFAULT_SPACING = 5;
	private static final Font SETTINGS_TITLE_FONT = Font.font(16);
	private static final Insets TITLE_PADDING = new Insets(0, 0, 0, 1);
	private final MainPane mainPane;
	private final VBox settingsBox;
	private final Label title;
	private final Button removeSupplierButton;
	
	private boolean allowingRemoval;
	public SettingsPane(MainPane mainPane) {
		super(DEFAULT_SPACING);
		this.getStyleClass().add(STYLE_CLASS_NAME);
		Objects.requireNonNull(mainPane);
		this.mainPane = mainPane;
		this.title = new Label("Settings");
		title.setFont(SETTINGS_TITLE_FONT);
		title.setPadding(TITLE_PADDING);
		this.settingsBox = new VBox(DEFAULT_SPACING);
		setPadding(new Insets(DEFAULT_PADDING));
		setBorder(Borders.of(Color.LIGHTGRAY, new CornerRadii(5)));
		ProblemPane problemPane = mainPane.getProblemPane();
		CompositeProblemSupplier supplier = problemPane.getSupplier();
		for(ProblemSupplier ps : supplier.suppliers())
			settingsBox.getChildren().add(SettingTitledPane.displayFor(ps, this));
		supplier.suppliers().addAddListener(this::supplierAdded);
		supplier.suppliers().addRemoveListener(this::supplierRemoved);
		
		this.removeSupplierButton = Buttons.of(REMOVE_SUPPLIER_BUTTON_TEXT, this::removeSupplierButtonAction);
		getChildren().addAll(title, settingsBox, createAddSupplierButton(), removeSupplierButton);
		
//		settingsBox.setBorder(Borders.of(Color.PINK));
//		this.setBorder(Borders.of(Color.ORANGE));
//		title.setBorder(Borders.of(Color.BLUE));
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
		this.allowingRemoval = true;
		removeSupplierButton.setText("Done");
		for(Node node : settingsBox.getChildren()) {
			SettingTitledPane stp = (SettingTitledPane) node;
			stp.allowForRemoval();
		}
	}
	
	private void disallowRemoval() {
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
		mainPane.showChooser();
	}
	
	public MainPane getMainPane() {
		return mainPane;
	}
}
