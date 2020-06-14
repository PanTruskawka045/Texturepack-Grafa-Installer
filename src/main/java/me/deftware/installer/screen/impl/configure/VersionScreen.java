package me.deftware.installer.screen.impl.configure;

import me.deftware.aristois.installer.InstallerAPI;
import me.deftware.installer.Main;
import me.deftware.installer.screen.AbstractScreen;
import me.deftware.installer.screen.components.*;

/**
 * @author Deftware
 */
public class VersionScreen extends AbstractScreen {

	@Override
	public void init() {
		componentList.clear();

		String[] versions = InstallerAPI.getMinecraftVersions();
		ComboBoxComponent versionsBox = new ComboBoxComponent(0, 200, 600, 30, "Product Sans", versions);
		versionsBox.centerHorizontally();

		addComponent(versionsBox, new TextComponent(0, 65, "Product Sans", 50, "Which Minecraft version?").centerHorizontally(),
				new TextComponent(0, 130, "Product Sans", 25, "Select which Minecraft version you would like to use Aristois with:").centerHorizontally(),
				new TextComponent(0, 300, "Product Sans", 25, "We support all versions between", versions[0] + " and " + versions[versions.length - 1]).centerHorizontally(),
				new ButtonComponent(50, 400, 100, 50, "Continue", mouseButton -> {
			String version = versionsBox.getSelectedItem().substring("Minecraft ".length());
					Main.getWindow().transitionForward(new TransitionScreen("Got it, Minecraft " + version + " it is", button -> {
						Main.getWindow().transitionForward(new LauncherScreen(version));
					}, "Next, you will select launcher and Minecraft home directory", "and maybe use a custom mod loader~"));
		}).centerHorizontally());
	}

}
