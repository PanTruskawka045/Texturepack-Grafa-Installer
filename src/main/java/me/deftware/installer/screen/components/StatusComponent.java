package me.deftware.installer.screen.components;

import lombok.Getter;
import lombok.Setter;
import me.deftware.installer.screen.AbstractComponent;

/**
 * @author Deftware
 */
public class StatusComponent extends AbstractComponent {

	private GifTextureComponent gifRenderer;
	private TextureComponent textureComponent;
	private TextComponent textComponent;

	private @Getter @Setter boolean status = false;
	private @Getter float width, height;

	public StatusComponent(float x, float y, String text) {
		super(x, y);
		gifRenderer = new GifTextureComponent(x, y, "assets/spinner.gif", 10, -1);
		textureComponent = new TextureComponent(x, y, "/assets/checkmark.png", 10);
		textComponent = new TextComponent(x + gifRenderer.getWidth(), y, "Product Sans", 30, text);
	}

	@Override
	public void render(float x, float y, double mouseX, double mouseY) {
		textComponent.render(x + gifRenderer.getWidth() + 10, y + 6, mouseX, mouseY);
		if (status) {
			textureComponent.render(x, y, mouseX, mouseY);
		} else {
			gifRenderer.render(x, y, mouseX, mouseY);
		}
	}

	@Override
	public void update() { }

	@Override
	public boolean mouseClicked(double x, double y, int mouseButton) {
		return false;
	}

	@Override
	public void mouseReleased(double x, double y, int mouseButton) { }

	@Override
	public void charTyped(char typedChar) { }

	@Override
	public void keyPressed(int keycode, int mods) { }

	@Override
	public void onScroll(double xPos, double yPos) { }

}
