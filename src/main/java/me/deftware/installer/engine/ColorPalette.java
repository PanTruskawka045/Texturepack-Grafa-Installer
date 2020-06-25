package me.deftware.installer.engine;

import java.awt.*;

/**
 * @author Deftware
 */
public class ColorPalette {

	public static Color BACKGROUND_COLOR = new Color(19, 29, 39);

	public static Color BRIGHT_BACKGROUND_COLOR = new Color(36, 58, 82);

	public static Color blend(float ratio, Color... colors) {
		int r = 0, g = 0, b = 0, a = 0;
		for (Color color : colors) {
			r += color.getRed() * ratio;
			g += color.getGreen() * ratio;
			b += color.getBlue() * ratio;
			a += color.getAlpha() * ratio;
		}
		return new Color(r, g, b, a);
	}

}
