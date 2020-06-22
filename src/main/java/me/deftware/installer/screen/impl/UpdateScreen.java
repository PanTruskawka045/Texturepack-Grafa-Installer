package me.deftware.installer.screen.impl;

import me.deftware.installer.Main;
import me.deftware.installer.screen.AbstractScreen;
import me.deftware.installer.screen.components.TextComponent;

public class UpdateScreen extends AbstractScreen {

	private String newVersion;

	public UpdateScreen(String newVersion) {
		this.newVersion = newVersion;
	}

	@Override
	public void init() {
		TextComponent titleComponent = new TextComponent(0, 0, "Product Sans", 35, "Uh oh! Outdated :(");
		titleComponent.centerHorizontally().centerVertically(-130);
		TextComponent subText = new TextComponent(0, 0, "Product Sans", 25, mouseButton -> {
			openLink("");
		}, "This installer is out of date,", "but fear not! Click me to download the latest.", "", "Current installer version: " + Main.getVersion(), "Latest installer version: " + newVersion);
		subText.centerHorizontally();
		subText.setY(titleComponent.getY() + titleComponent.getHeight() + 10);
		addComponent(titleComponent, subText);
	}

}
