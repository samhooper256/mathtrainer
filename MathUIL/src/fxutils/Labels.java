package fxutils;

import javafx.scene.control.Label;
import javafx.scene.text.Font;

/**
 * @author Sam Hooper
 *
 */
public final class Labels {
	
	private Labels() {}
	
	public static Label of(final Font font) {
		Label l = new Label();
		l.setFont(font);
		return l;
	}
	
	public static Label of(final String text, final Font font) {
		Label l = new Label(text);
		l.setFont(font);
		return l;
	}
	
	public static Label of(final String text, final double fontSize) {
		Label l = new Label(text);
		l.setFont(Font.font(fontSize));
		return l;
	}
}
