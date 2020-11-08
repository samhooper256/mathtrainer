package base;

import java.io.InputStream;
import java.util.Optional;

import javafx.application.*;
import javafx.scene.*;
import javafx.stage.*;

/**
 * @author Sam Hooper
 *
 */
public class Main extends Application {
	
	private static final String SCENE_STYLESHEET_FILENAME = "base/basestyle.css";
	private static final String RESOURCES_PREFIX = "/resources/";
	private static Stage primaryStage;
	
	/**
	 * @throws AssertionError if assertions are not enabled.
	 */
	private static void ensureAssertionsAreEnabled() {
		boolean enabled = false;
		assert (enabled = true) == true;
		if(!enabled)
			throw new AssertionError(String.format(
					"%n%nAssertions are not enabled. The program currently requires assertons to be enabled.%n"
					+ "Enable them by adding the '-ea' JVM argument.%n"
					+ "In Eclipse, this can be done by clicking Run > Run Configurations > Arguments tab. Then, enter "
					+ "'-ea' into the 'VM Arguments' box.%n"
			));
	}
	public static void main(String[] args) {
		ensureAssertionsAreEnabled();
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		Main.primaryStage = primaryStage;
		Scene scene = MainScene.create(600, 400);
		scene.getStylesheets().add(SCENE_STYLESHEET_FILENAME);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public Stage getPrimaryStage() {
		return primaryStage;
	}
	
	public MainScene getMainScene() {
		return (MainScene) getPrimaryStage().getScene();
	}
	
	/**
	 * Produces an {@link Optional} of the {@link InputStream} for a resource in the "resources" folder.
	 * If the resource could not be located, the returned {@code Optional} will be empty. Otherwise, it
	 * will contain the {@code InputStream}.
	 * @param filename the name of the resource file, including its file extension. Must be in the "resources" folder.
	 * @return an {@link Optional} possibly containing the {@link InputStream}.
	 */
	public static Optional<InputStream> getOptionalResourceStream(String filename) {
		return Optional.ofNullable(Main.class.getResourceAsStream(RESOURCES_PREFIX + filename));
	}
	
	/**
	 * Produces the {@link InputStream} for a resource in the "resources" folder.
	 * @param filename the name of the file, including its file extension. Must be in the "resources" folder.
	 * @return the {@link InputStream} for the resource indicated by the given filename.
	 * @throws IllegalArgumentException if the file does not exist.
	 */
	public static InputStream getResourceStream(String filename) {
		Optional<InputStream> stream = getOptionalResourceStream(filename);
		if(stream.isEmpty())
			throw new IllegalArgumentException("The resource at " + RESOURCES_PREFIX + filename + " does not exist");
		return stream.get();
	}
	
}
