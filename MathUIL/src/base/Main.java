package base;

import javafx.application.*;
import javafx.scene.*;
import javafx.stage.*;

/**
 * @author Sam Hooper
 *
 */
public class Main extends Application {
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		Scene scene = new Scene(new MainPane(), 600, 400);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

}
