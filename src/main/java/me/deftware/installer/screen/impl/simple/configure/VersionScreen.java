package me.deftware.installer.screen.impl.simple.configure;

import me.deftware.aristois.installer.InstallerAPI;
import me.deftware.installer.Main;
import me.deftware.installer.screen.AbstractScreen;
import me.deftware.installer.screen.components.*;
import me.deftware.installer.screen.impl.simple.TransitionScreen;

/**
 * @author Deftware
 */
public class VersionScreen extends AbstractScreen {

	@Override
	public void init() {
		componentList.clear();

		String[] versions = InstallerAPI.getMinecraftVersions();
		ComboBoxComponent versionsBox = new ComboBoxComponent(0, 200, 600, 30, versions);
		versionsBox.centerHorizontally();

		addComponent(versionsBox, new TextComponent(0, 65,  40, "Which Minecraft version?").centerHorizontally(),
				new TextComponent(0, 130,  25, "Select which Minecraft version you would like to use Aristois with:").centerHorizontally(),
				new TextComponent(0, 280,  25, "We support all versions between", versions[0] + " and " + versions[versions.length - 1], "(1.12.2 through 1.8.9 in beta)").centerHorizontally(),
				new ButtonComponent(50, 400, 100, 50, "Continue", mouseButton -> {
			String version = versionsBox.getSelectedItem().substring("Minecraft ".length());
					Main.getWindow().transitionForward(new TransitionScreen("Got it, Minecraft " + version + " it is.", button -> {
						Main.getWindow().transitionForward(new LauncherScreen(version));
					}, 1700, "Next you'll choose your launcher and Minecraft directory."));
		}).centerHorizontally());
	}

}
