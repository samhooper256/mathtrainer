package fxutils;

import javafx.event.*;
import javafx.scene.control.*;

/**
 * @author Sam Hooper
 *
 */
public final class Buttons {
	
	private Buttons() {}
	
	public static Button of(String text, EventHandler<ActionEvent> event) {
		Button b = new Button(text);
		b.setOnAction(event);
		return b;
	}
	
	/**
	 * Returns a new {@link Button} with the given text that, when clicked, runs the given {@link Runnable}.
	 */
	public static Button of(String text, Runnable r) {
		return of(text, e -> r.run());
	}
	
}
