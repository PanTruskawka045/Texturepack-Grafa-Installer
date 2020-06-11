package me.deftware.installer.resources.font;

import java.awt.*;
import java.util.HashMap;

/**
 * @author Deftware
 */
public class FontManager {

	private static HashMap<String, BitmapFont> fontStore = new HashMap<>();
	public static HashMap<String, Font> customFonts = new HashMap<>();

	public static BitmapFont getFont(String name, int size, int modifiers) {
		String key = name + size + modifiers;
		if (fontStore.containsKey(key)) {
			return fontStore.get(key);
		}
		fontStore.put(key, new BitmapFont(name, size, modifiers));
		return fontStore.get(key);
	}

	public static void registerCustomFont(Font font) {
		customFonts.putIfAbsent(font.getFontName(), font);
	}

	public static void removeFont(String name, int size, int modifiers) {
		fontStore.remove(name + size + modifiers);
	}

	public static void clearCache() {
		fontStore.clear();
		customFonts.clear();
	}

	public static class Modifiers {
		public static byte NONE = 0b00000000;
		public static byte ANTIALIASED = 0b00100000;
		public static byte MEMORYSAVING = 0b01000000;
	}

}
