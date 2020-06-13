package me.deftware.installer.screen.impl;

import me.deftware.installer.Main;
import me.deftware.installer.logic.InstallerAPI;
import me.deftware.installer.screen.AbstractComponent;
import me.deftware.installer.screen.AbstractScreen;
import me.deftware.installer.screen.components.ButtonComponent;
import me.deftware.installer.screen.components.TextureComponent;
import me.deftware.installer.screen.impl.configure.VersionScreen;

import java.awt.*;
import java.net.URI;

/**
 * @author Deftware
 */
public class WelcomeScreen extends AbstractScreen {

	private boolean loaded = false;
	private ButtonComponent button;

	@Override
	public void init() {
		componentList.clear();
		AbstractComponent logoShadow = new TextureComponent(0, 2, "/assets/logo_shadow.png", 4).centerHorizontally(2);
		addComponent(new TextureComponent(0, 0, "/assets/logo.png", 4).centerHorizontally(), logoShadow);
		button = new ButtonComponent(50, 350, 150, 50, "Fetching data...", mouseButton -> {
			if (loaded) {
				Main.getWindow().transitionForward(new VersionScreen());
			}
		});
		button.centerHorizontally();

		TextureComponent gitIcon = new TextureComponent(0, 0, "/assets/git.png", 10, mouseButton -> {
			try {
				if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
					Desktop.getDesktop().browse(new URI("https://gitlab.com/Aristois/Installer"));
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});
		gitIcon.setX(Main.getWindow().windowWidth - gitIcon.getWidth() - 10);
		gitIcon.setY(Main.getWindow().windowHeight - gitIcon.getHeight() - 10);

		addComponent(button, gitIcon);

		new Thread(() -> {
			InstallerAPI.fetchData(false);
			loaded = true;
			button.setText("Continue");
		}).start();
	}

	@Override
	public void render(double mouseX, double mouseY) {
		super.render(mouseX, mouseY);
	}

}
