package base;

import javafx.scene.*;
/**
 * @author Sam Hooper
 *
 */
public class MainScene extends Scene {
	
	public static MainScene create(final int width, final int height) {
		return new MainScene(new MainPane(), width, height);
	}
	
	private MainScene(final MainPane root, final int width, final int height) {
		super(root, width, height);
		rootProperty().addListener((x, y, z) -> {
			throw new UnsupportedOperationException("The root of a MainScene cannot be changed.");
		});
	}
	
	public MainPane getMainPane() {
		return (MainPane) getRoot();
	}
	
}
