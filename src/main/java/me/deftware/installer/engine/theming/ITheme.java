package me.deftware.installer.engine.theming;

import java.awt.*;

/**
 * The main theme interface, to create a new theme extend a class by this interface
 *
 * @author Deftware
 */
public interface ITheme {

	/**
	 * The main background
	 */
	Color getBackgroundColor();

	/**
	 * A brighter version of the background color, used for highlighting components and the like
	 */
	Color getBrightBackgroundColor();

	/**
	 * The color of the scroller in the scrollbar
	 */
	Color getScrollerColor();

	/**
	 * The color of the scroller background
	 */
	Color getScrollerBackgroundColor();

	/**
	 * The color used for outlining in components
	 */
	Color getOutlineColor();

	/**
	 * The color of text components
	 */
	Color getTextColor();

	/**
	 * If text should have shadow by default
	 */
	boolean isTextShadow();

	/**
	 * The font used throughout various components
	 * Custom .ttf fonts can be used, however, they must be pre-loaded and stored in the
	 * jars /assets/ folder
	 */
	String getTextFont();

}
