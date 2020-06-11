package me.deftware.installer;

import lombok.Getter;
import me.deftware.installer.engine.Window;
import me.deftware.installer.resources.ResourceUtils;
import me.deftware.installer.resources.font.FontManager;

import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import java.util.Objects;

/**
 * @author Deftware
 */
public class Main {

	public @Getter static me.deftware.installer.engine.Window window;

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			InputStream fontStream = ResourceUtils.getStreamFromResources("assets/sans.ttf");
			Font customFont = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(fontStream));
			FontManager.registerCustomFont(customFont);
			fontStream.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		window = new Window();
		window.run();
	}

}