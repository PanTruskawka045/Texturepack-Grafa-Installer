package me.deftware.installer.screen;

import lombok.Getter;
import lombok.Setter;
import me.deftware.installer.Main;

import java.awt.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Deftware
 */
public abstract class AbstractScreen {

	private @Getter @Setter int x = 0, y = 0;
	protected @Getter List<AbstractComponent> componentList = new ArrayList<>();
	protected @Getter @Setter boolean initialized = false;

	protected void addComponent(AbstractComponent... components) {
		componentList.addAll(Arrays.asList(components));
	}

	public void render(double mouseX, double mouseY) {
		for (int i = componentList.size() - 1; i > -1; i--) {
			AbstractComponent component = componentList.get(i);
			if (component.isVisible()) {
				component.render(x + component.getX(), y + component.getY(), mouseX, mouseY);
			}
		}
	}

	public void update() {
		for (AbstractComponent component : componentList) {
			component.update();
		}
	}

	public void mouseClicked(double x, double y, int button) {
		for (AbstractComponent component : componentList) {
			if (component.mouseClicked(x, y, button)) {
				break;
			}
		}
	}

	public void mouseReleased(double x, double y, int button) {
		for (AbstractComponent component : componentList) {
			component.mouseReleased(x, y, button);
		}
	}

	public void onScroll(double xPos, double yPos) {
		for (AbstractComponent component : componentList) {
			component.onScroll(xPos, yPos);
		}
	}

	public void keyPressed(int keyCode, int mods) {
		for (AbstractComponent component : componentList) {
			component.keyPressed(keyCode, mods);
		}
	}

	public void charTyped(char charTyped) {
		for (AbstractComponent component : componentList) {
			component.charTyped(charTyped);
		}
	}

	public static void openLink(String link) {
		try {
			if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
				Desktop.getDesktop().browse(new URI(link));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void onWindowResize() {
		init();
	}

	public static float getWindowWidth() {
		return Main.window.windowWidth;
	}

	public static float getWindowHeight() {
		return Main.window.windowHeight;
	}

	public abstract void init();

	public void initSuper() {
		if (!initialized) {
			initialized = true;
			init();
		}
	}
}
