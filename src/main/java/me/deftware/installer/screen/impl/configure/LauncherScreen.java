package me.deftware.installer.screen.impl.configure;

import me.deftware.aristois.installer.InstallerAPI;
import me.deftware.aristois.installer.utils.VersionData;
import me.deftware.installer.Main;
import me.deftware.installer.OSUtils;
import me.deftware.installer.screen.AbstractScreen;
import me.deftware.installer.screen.components.*;
import me.deftware.installer.screen.impl.InstallingScreen;
import me.deftware.installer.screen.impl.TransitionScreen;
import me.deftware.installer.screen.impl.YesNoScreen;

import java.io.File;

/**
 * @author Deftware
 */
public class LauncherScreen extends AbstractScreen {

	private String version;

	public LauncherScreen(String version) {
		this.version = version;
	}

	@Override
	public void init() {
		componentList.clear();

		VersionData data = InstallerAPI.getVersions().get(version);
		String[] launchers = new String[data.getLaunchers().size() + data.getModLoaders().size()];
		for (int i = 0; i < data.getLaunchers().size(); i++) {
			launchers[i] = data.getLaunchers().get(i) + " launcher";
		}
		for (int i = data.getLaunchers().size(); i < data.getLaunchers().size() + data.getModLoaders().size(); i++) {
			launchers[i] = data.getModLoaders().get(i - data.getLaunchers().size()) + " mod loader";
		}

		ComboBoxComponent launcherBox = new ComboBoxComponent(0, 240, 600, 30, "Product Sans", launchers);
		launcherBox.centerHorizontally();

		TextBoxComponent minecraftPath = new BrowsableTextBoxComponent(0, 300, 600, 30, OSUtils.getMCDir());
		minecraftPath.setShadowText("Minecraft path...");
		minecraftPath.centerHorizontally();

		addComponent(new TextComponent(0, 65, "Product Sans", 40, "Launcher & Directory").centerHorizontally(),
				new TextComponent(0, 130, "Product Sans", 25, "Select your launcher and Minecraft directory.", "Press \"Continue\" for a default installation:").centerHorizontally(),
				launcherBox, new ButtonComponent(50, 400, 100, 50, "Continue", mouseButton -> {
					String launcher = launcherBox.getSelectedItem();
					if (launcher.toLowerCase().contains("mod loader")) {
						Main.getWindow().transitionForward(new YesNoScreen("Warning! This may cause issues.", confirm -> {
							if (!confirm) {
								Main.getWindow().transitionBackwards(LauncherScreen.this);
							} else {
								Main.getWindow().transitionForward(new InstallingScreen(InstallerAPI.getVersions().get(version), minecraftPath.getText(), launcher));
							}
						}, "Using a custom mod loader is not officially supported", "and you may crash or have stability issues.", "Do not report bugs when using one.", "", "If you still wish to use one, press \"Continue\""));
					} else {
						if (launcher.toLowerCase().contains("multimc")) {
							File instancesFolder = new File(minecraftPath.getText() + File.separator + "instances" + File.separator);
							if (!instancesFolder.exists()) {
								Main.getWindow().transitionForward(new TransitionScreen("Uh oh! Invalid path :(", button -> {
									Main.getWindow().transitionBackwards(LauncherScreen.this);
								}, 3000, "Please specify a valid MultiMC root directory.", "", "You will be redirected back to select it."));
							} else {
								Main.getWindow().transitionForward(new MultiMCInstanceScreen(InstallerAPI.getVersions().get(version), minecraftPath.getText(), launcher));
							}
						} else {
							File mcDir = new File(minecraftPath.getText());
							boolean proceed = true;
							if (!mcDir.exists()) {
								proceed = false;
							} else {
								// Make sure its an actual mc directory
								if (!new File(mcDir.getAbsolutePath() + File.separator + "versions" + File.separator).exists()) {
									proceed = false;
								}
							}
							if (proceed) {
								Main.getWindow().transitionForward(new InstallingScreen(InstallerAPI.getVersions().get(version), minecraftPath.getText(), launcher));
							} else {
								Main.getWindow().transitionForward(new YesNoScreen("Are you sure this is correct?", confirm -> {
									if (confirm) {
										Main.getWindow().transitionForward(new InstallingScreen(InstallerAPI.getVersions().get(version), minecraftPath.getText(), launcher));
									} else {
										Main.getWindow().transitionBackwards(LauncherScreen.this);
									}
								}, "The Minecraft directory you have chosen", "does not appear to contain the necessary files required for", "Minecraft to run, do you still want to proceed with this path?"));
							}
						}
					}
				}).centerHorizontally(), minecraftPath);
	}

}
