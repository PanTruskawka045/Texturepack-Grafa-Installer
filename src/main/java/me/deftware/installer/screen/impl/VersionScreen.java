package me.deftware.installer.screen.impl;

import me.deftware.installer.Main;
import me.deftware.installer.OSUtils;
import me.deftware.installer.screen.AbstractScreen;
import me.deftware.installer.screen.components.*;

/**
 * @author Deftware
 */
public class VersionScreen extends AbstractScreen {

	@Override
	public void init() {
		componentList.clear();
		ComboBoxComponent versionsBox = new ComboBoxComponent(0, 200, 600, 30, "Product Sans", "Minecraft 1.16", "Minecraft 1.15.2");
		versionsBox.centerHorizontally();
		TextBoxComponent minecraftPath = new BrowsableTextBoxComponent(0, 260, 600, 30, OSUtils.getMCDir());
		minecraftPath.setShadowText("Minecraft path...");
		minecraftPath.centerHorizontally();
		ComboBoxComponent launcherBox = new ComboBoxComponent(0, 320, 600, 30, "Product Sans", "Official Mojang launcher", "MultiMC launcher");
		launcherBox.centerHorizontally();

		addComponent(new TextComponent(0, 65, "Product Sans", 70, "Configure install").centerHorizontally(), versionsBox, minecraftPath, launcherBox, new ButtonComponent(50, 400, 100, 50, "Continue", mouseButton -> {
			Main.getWindow().transitionTo(new InstallingScreen(versionsBox.getSelectedItem().substring("Minecraft ".length()), minecraftPath.getText(), launcherBox.getSelectedItem()));
		}).centerHorizontally());
	}

}
