package fxutils;

import javafx.scene.layout.*;
import javafx.scene.paint.Paint;

/**
 * Provides utility methods for making {@link Border}s.
 * @author Sam Hooper
 *
 */
public final class Borders {
	private Borders() {}
	
	/**
	 * Creates and returns a new {@link Border} with the given {@link Paint} as its texture. It will have
	 * {@link BorderStrokeStyle#SOLID}, {@link CornerRadii#EMPTY}, and {@link BorderWidths#DEFAULT}.
	 * @param paint
	 * @return a new {@link Border} with the given {@link Paint} as its texture. 
	 */
	public static Border of(Paint paint) {
		return of(paint, BorderWidths.DEFAULT);
	}
	
	/**
	 * Creates and returns a new {@link Border} with the given {@link Paint} as its texture and {@code width} as
	 * its width. It will have {@link BorderStrokeStyle#SOLID} and {@link CornerRadii#EMPTY}.
	 * @param paint
	 * @param width
	 * @return a new {@link Border} with the given {@link Paint} as its texture and {@code width} as its width.
	 */
	public static Border of(Paint paint, int width) {
		return of(paint, new BorderWidths(width));
	}
	
	/**
	 * Creates and returns a new {@link Border} with the given {@link Paint} as its texture and {@link BorderWidths}
	 * as its width. It will have {@link BorderStrokeStyle#SOLID} and {@link CornerRadii#EMPTY}.
	 * @param paint
	 * @param width
	 * @return a new {@link Border} with the given {@link Paint} as its texture and the given {@link BorderWidths} as
	 * its width.
	 */
	public static Border of(Paint paint, BorderWidths width) {
		return new Border(new BorderStroke(paint, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, width));
	}
	
	public static Border of(Paint paint, CornerRadii radii) {
		return new Border(new BorderStroke(paint, BorderStrokeStyle.SOLID, radii, BorderWidths.DEFAULT));
	}
}
