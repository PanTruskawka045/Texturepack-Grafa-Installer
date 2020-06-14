package me.deftware.installer;

import lombok.Getter;
import me.deftware.aristois.installer.InstallerAPI;
import me.deftware.installer.engine.Window;
import me.deftware.installer.resources.ResourceUtils;
import me.deftware.installer.resources.font.FontManager;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLClassLoader;
import java.util.Objects;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

/**
 * @author Deftware
 */
public class Main {

	public @Getter static String version = "1.9.2.2";
	public @Getter static Window window;

	public static void main(String[] args) {
		try {
			try {
				Attributes attr =
						new Manifest(((URLClassLoader) InstallerAPI.class.getClassLoader()).findResource("META-INF/MANIFEST.MF").openStream()).getMainAttributes();
				InstallerAPI.setDonorBuild(Boolean.parseBoolean(attr.getValue("donorBuild")));
			} catch (IOException E) {
				E.printStackTrace();
			}
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