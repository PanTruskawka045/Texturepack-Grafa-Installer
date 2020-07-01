package me.deftware.installer.engine.theming;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

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
	PURPLE(new Color(19, 29, 39), new Color(36, 58, 82), new Color(36, 58, 82).brighter().brighter(), new Color(36, 58, 82).brighter(), Color.white, Color.white, Color.white.darker(), Color.black,"Product Sans", true),

	/*
		Other themes
	 */
	WHITE(Color.white, Color.white.darker(), Color.gray.brighter(), Color.gray, Color.black, Color.black, Color.gray, Color.white.darker(), "Product Sans", false);

	/*
		Various fields of the default colors, which can be modified at runtime
	 */
	private @Getter @Setter Color backgroundColor, foregroundColor, scrollerColor, scrollerBackgroundColor, outlineColor, textColor, textHighlightColor, tooltipBackground;
	private @Setter @Getter String textFont;
	private @Getter @Setter boolean textShadow;

}
