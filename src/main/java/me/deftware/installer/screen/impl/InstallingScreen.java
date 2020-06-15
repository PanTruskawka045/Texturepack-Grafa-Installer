package me.deftware.installer.screen.impl;

import me.deftware.aristois.installer.jsonbuilder.AbstractJsonBuilder;
import me.deftware.aristois.installer.modloader.impl.ForgeInstaller;
import me.deftware.aristois.installer.utils.VersionData;
import me.deftware.installer.Main;
import me.deftware.installer.screen.AbstractScreen;
import me.deftware.installer.screen.components.ButtonComponent;
import me.deftware.installer.screen.components.TextComponent;

import java.io.File;

/**
 * @author Deftware
 */
public class InstallingScreen extends AbstractScreen {

	private VersionData version;
	private String path, launcher;
	private TextComponent textComponent, subText;
	private ButtonComponent button;
	private String[] installStatus;
	private int count = 0;

	public InstallingScreen(VersionData version, String path, String launcher) {
		this.version = version;
		this.path = path;
		this.launcher = launcher;
	}

	private void updateStatus(String... status) {
		installStatus = status;

	}

	@Override
	public void init() {
		componentList.clear();
		textComponent = new TextComponent(0, 0, "Product Sans", 60, "Greatness is coming");
		textComponent.centerHorizontally().centerVertically(-20);
		subText = new TextComponent(0, textComponent.getY() + textComponent.getHeight() + 10, "Product Sans", 40, "for Aristois is being installed...");
		subText.centerHorizontally();
		button = new ButtonComponent(0, Main.getWindow().windowHeight - 110, 220, 50, "Install another version", mouseButton -> {
			Main.getWindow().transitionForward(new WelcomeScreen());
		});
		button.setVisible(false);
		addComponent(textComponent, subText, button.centerHorizontally());

		new Thread(() -> {
			Thread.currentThread().setName("Installer thread");
			String result = "";
			if (launcher.toLowerCase().contains("forge")) {
				result = new ForgeInstaller().install(version, path + File.separator);
			} else {
				AbstractJsonBuilder builder = version.getBuilder(launcher.substring(0, launcher.length() - " launcher".length()), launcher.substring(0, launcher.length() - " launcher".length()));
				result = builder.install(builder.build(version), version, path + File.separator);
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			System.out.println(result);
			subText.fadeOut(alpha -> fadeIn());
			textComponent.fadeOut(alpha -> fadeIn());
		}).start();
	}

	private void fadeIn() {
		count++;
		if (count == 2) {
			textComponent.setText("You're all set!");
			if (launcher.toLowerCase().contains("vanilla")) {
				subText.setText("Start Minecraft and select", "\"release " + version.getVersion() + "-Aristois\"");
			} else if (launcher.toLowerCase().contains("forge")) {
				subText.setText("Start Minecraft and select", "Forge " + version.getVersion() + " to start", "Aristois");
			}
			textComponent.centerHorizontally().centerVertically(-60);
			subText.center();
			subText.setY(textComponent.getY() + textComponent.getHeight() + 10);
			textComponent.fadeIn(aplha -> {
				button.setVisible(true);
			});
			subText.fadeIn(null);
		}
	}

}
