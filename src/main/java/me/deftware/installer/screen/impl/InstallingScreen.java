package me.deftware.installer.screen.impl;

import me.deftware.installer.screen.AbstractScreen;
import me.deftware.installer.screen.components.TextComponent;

/**
 * @author Deftware
 */
public class InstallingScreen extends AbstractScreen {

	private String version, path, launcher;
	private TextComponent textComponent, subText, subText2;

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
		subText2 = new TextComponent(0, 0, "Product Sans", 40, "");
		addComponent(textComponent, subText, subText2);

		// DEMO
		new Thread(() -> {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			textComponent.setText("You're all set!");
			subText.setText("Start Minecraft and select");
			subText2.setText("\"release " + version + "-Aristois\"");
			textComponent.centerHorizontally();
			textComponent.setY(150);
			subText.center();
			subText.setY(textComponent.getY() + textComponent.getHeight() + 10);
			subText2.center();
			subText2.setY(textComponent.getY() + textComponent.getHeight() + subText.getHeight() + 10);
		}).start();
	}

}
