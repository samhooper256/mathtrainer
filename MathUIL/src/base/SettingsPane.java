package base;

import javafx.scene.Node;
import javafx.scene.layout.*;

/**
 * @author Sam Hooper
 *
 */
public class SettingsPane extends VBox {
	
	private MainPane mainPane;
	
	public SettingsPane(MainPane mainPane) {
		this.mainPane = mainPane;
	}
	
	public MainPane getMainPane() {
		return mainPane;
	}
	
	private static Node displayFor(Object setting) {
		return null; //TODO
		
	}
}
