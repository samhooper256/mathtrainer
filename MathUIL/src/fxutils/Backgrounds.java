package fxutils;

import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;

/**
 * @author Sam Hooper
 *
 */
public final class Backgrounds {
	
	private Backgrounds() {}
	
	/**
	 * Creates and returns a new {@link Background} with the given {@link Paint} as its texture. The
	 * background will have {@link CornerRadii#EMPTY} and {@link Insets#EMPTY}.
	 * @param paint
	 * @return a {@link Background} with the given {@link Paint} as its texture.
	 */
	public static Background of(Paint paint) {
		return new Background(new BackgroundFill(paint, CornerRadii.EMPTY, Insets.EMPTY));
	}
}
