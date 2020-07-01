package me.deftware.installer.engine.theming;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.awt.*;

/**
 * A collection of default themes that can be used
 *
 * @author Deftware
 */
public @AllArgsConstructor enum DefaultThemes implements ITheme {

	/**
	 * The default theme used, with the Google Product Sans font included in the assets of the jar
	 */
	PURPLE(new Color(19, 29, 39), new Color(36, 58, 82), new Color(36, 58, 82).brighter().brighter(), new Color(36, 58, 82).brighter(), Color.white, Color.white, "Product Sans", true),

	/*
		Other themes
	 */
	WHITE(Color.white, Color.white.darker(), Color.gray.brighter(), Color.gray, Color.black, Color.black, "Product Sans", false);

	private final @Getter Color backgroundColor, brightBackgroundColor, scrollerColor, scrollerBackgroundColor, outlineColor, textColor;
	private final @Getter String textFont;
	private final @Getter boolean textShadow;

}
