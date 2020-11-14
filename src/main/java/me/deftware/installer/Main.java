package me.deftware.installer;

import lombok.Getter;
import me.deftware.aristois.installer.InstallerAPI;
import me.deftware.installer.engine.NativeManager;
import me.deftware.installer.engine.MainWindow;
import me.deftware.installer.resources.font.FontManager;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

/**
 * @author Deftware
 */
public class Main {

	public static String donorString = "@DONOR@";
	public @Getter static String version = "1.9.2.4";
	public @Getter static MainWindow window;

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
		System.out.println("Donor build " + donorString);
		InstallerAPI.setDonorBuild(Boolean.parseBoolean(donorString));
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			FontManager.loadFontFromAssets("/assets/sans.ttf");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		// opengl is deprecated in macOS
		if (OSUtils.isMac()) {
			MainWindow.openLegacy();
		} else {
			try {
				window = new MainWindow();
				window.run();
			} catch (UnsatisfiedLinkError ex) {
				System.err.println("Unsatisfied lwjgl link error, trying to fix it...");
				if (args.length == 0) {
					NativeManager.extractNatives();
					System.out.println("Restarting app...");
					restart();
				} else {
					System.out.println("Failed to load native libraries, defaulting to legacy mode...");
					MainWindow.openLegacy();
				}
			} catch (Throwable ex) {
				ex.printStackTrace();
				MainWindow.openLegacy();
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
			MainWindow.openLegacy();
		}
	}

}
