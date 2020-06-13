package me.deftware.installer.screen.impl;

import me.deftware.installer.screen.AbstractScreen;
import me.deftware.installer.screen.components.TextComponent;

/**
 * @author Deftware
 */
public class InstallingScreen extends AbstractScreen {

	private String version, path, launcher;
	private TextComponent textComponent, subText;

	public InstallingScreen(String version, String path, String launcher) {
		this.version = version;
		this.path = path;
		this.launcher = launcher;
	}

	@Override
	public void init() {
		componentList.clear();
		textComponent = new TextComponent(0, 0, "Product Sans", 60, "Installing, please wait...");
		textComponent.center();
		subText = new TextComponent(0, 0, "Product Sans", 40, "");
		subText.setAlpha(1);
		addComponent(textComponent, subText);

		new Thread(() -> {
			if (launcher.toLowerCase().contains("forge")) {

			} else if (launcher.toLowerCase().contains("multimc")) {

			} else {

			}

			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			textComponent.fadeOut(alpha -> {
				textComponent.setText("You're all set!");
				subText.setText("Start Minecraft and select", "\"release " + version + "-Aristois\"");
				textComponent.centerHorizontally();
				textComponent.setY(150);
				subText.center();
				subText.setY(textComponent.getY() + textComponent.getHeight() + 10);
				textComponent.fadeIn(null);
				subText.fadeIn(null);
			});
		}).start();
	}

}
