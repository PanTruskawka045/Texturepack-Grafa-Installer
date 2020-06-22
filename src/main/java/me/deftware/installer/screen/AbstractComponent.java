package me.deftware.installer.screen;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Deftware
 */
public abstract class AbstractComponent<T> {

	protected @Getter @Setter float x, y;
	protected @Getter @Setter boolean visible = true;

	public AbstractComponent(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public abstract float getWidth();

	public abstract float getHeight();

	public abstract void render(float x, float y, double mouseX, double mouseY);

	public abstract void update();

	public abstract boolean mouseClicked(double x, double y, int mouseButton);

	public abstract void mouseReleased(double x, double y, int mouseButton);

	public abstract void charTyped(char typedChar);

	public abstract void keyPressed(int keycode, int mods);

	public abstract void onScroll(double xPos, double yPos);

	public AbstractComponent<T> centerHorizontally(float offset) {
		x = ((AbstractScreen.getWindowWidth() / 2) - (getWidth() / 2)) + offset;
		return this;
	}

	public AbstractComponent<T> centerHorizontally() {
		return centerHorizontally(0);
	}

	public AbstractComponent<T> centerVertically(float offset) {
		y = (AbstractScreen.getWindowHeight() / 2) - (getHeight() / 2) + offset;
		return this;
	}

	public AbstractComponent<T> centerVertically() {
		return centerVertically(0);
	}

	public AbstractComponent<T> center() {
		return centerHorizontally().centerVertically();
	}

}
