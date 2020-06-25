package me.deftware.installer;

import lombok.Getter;
import me.deftware.aristois.installer.InstallerAPI;
import me.deftware.installer.engine.NativeManager;
import me.deftware.installer.engine.Window;
import me.deftware.installer.resources.font.FontManager;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

/**
 * @author Deftware
 */
public class Main {

	public @Getter static String version = "1.9.2.2";
	public @Getter static Window window;

	public static void main(String[] args) {
		if (args.length != 0 && !OSUtils.isMac()) {
			System.setProperty("org.lwjgl.util.Debug", "true");
			try {
				NativeManager.loadNatives();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		System.out.println("Running Java " + System.getProperty("java.version"));
		System.out.println("OS arch " + System.getProperty("os.arch"));
		System.out.println("Installer version " + version);
		try {
			try {
				Attributes attr = new Manifest(Main.class.getResourceAsStream("/META-INF/MANIFEST.MF")).getMainAttributes();
				InstallerAPI.setDonorBuild(Boolean.parseBoolean(attr.getValue("donorBuild")));
			} catch (IOException E) {
				E.printStackTrace();
			}
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			Font customFont = Font.createFont(Font.TRUETYPE_FONT, Main.class.getResourceAsStream("/assets/sans.ttf"));
			FontManager.registerCustomFont(customFont);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		// macOS just shouldn't run opengl, lwjgl can only run on 64 bit
		if (OSUtils.isMac()) {
			Window.openLegacy();
		} else {
			try {
				window = new Window();
				window.run();
			} catch (UnsatisfiedLinkError ex) {
				System.err.println("Unsatisfied lwjgl link error, trying to fix it...");
				if (args.length == 0) {
					NativeManager.extractNatives();
					System.out.println("Restarting app...");
					restart();
				} else {
					System.out.println("Failed to load native libraries, defaulting to legacy mode...");
					Window.openLegacy();
				}
			} catch (Throwable ex) {
				ex.printStackTrace();
				Window.openLegacy();
			}
		}
	}

	public static void restart() {
		try {
			LocationUtil self = LocationUtil.getClassPhysicalLocation(Main.class);
			if (self.toFile() != null && self.toFile().exists()) {
				Runtime.getRuntime().exec("java -jar " + self.toFile().getAbsolutePath() + " --loadNatives");
				System.exit(0);
			} else {
				throw new Exception("Could not find self");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			Window.openLegacy();
		}
	}

}
