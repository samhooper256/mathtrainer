package base;

import javafx.scene.*;
/**
 * @author Sam Hooper
 *
 */
public class MainScene extends Scene {
	
	public static MainScene create(final int width, final int height) {
		return new MainScene(makeRoot(), width, height);
	}
	
	private static MainPane makeRoot() {
		return new MainPane();
	}
	
	private final MainPane root;
	private MainScene(final MainPane root, final int width, final int height) {
		super(root, width, height);
		this.root = root;
		rootProperty().addListener((x, y, z) -> {
			throw new UnsupportedOperationException("The root of a MainScene cannot be changed.");
		});
	}
	
	public MainPane getMainPane() {
		return root;
	}
	
}
