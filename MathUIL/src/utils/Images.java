package utils;

import base.Main;
import javafx.scene.image.Image;

/**
 * Utility class for creating {@link javafx.scene.image. Image} objects from resource files.
 * @author Sam Hooper
 *
 */
public final class Images {
	
	private Images() {}

	/**
	 * Returns the image given by {@code filename} by invoking {@link Image#Image(java.io.InputStream)} with
	 * the appropriate {@link InputStream}. The file indicated by {@code filename} must be in the "resources"
	 * folder.
	 * @return the image given by {@code filename}
	 */
	public static Image getImage(String filename) {
		return new Image(Main.getResourceStream(filename));
	}
	
	/**
	 * Returns the image given by {@code filename} with the given properties. The file indicated by
	 * {@code filename} must be in the "resources" folder.
	 * See {@link Image#Image(String, double, double, boolean, boolean) for details on the arguments.
	 * @return the {@link Image} described by the given filename with the given properties.
	 */
	public static Image getImage(	String filename,
									double requestedWidth,
									double requestedHeight,
									boolean preserveRatio,
									boolean smooth) {
		return new Image(Main.getResourceStream(filename), requestedWidth, requestedHeight, preserveRatio, smooth);
	}
}
