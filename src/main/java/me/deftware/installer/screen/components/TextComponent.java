package me.deftware.installer.screen.components;

import lombok.Getter;
import lombok.Setter;
import me.deftware.installer.resources.font.BitmapFont;
import me.deftware.installer.resources.font.FontManager;
import me.deftware.installer.screen.AbstractComponent;

import java.awt.*;
import java.util.function.Consumer;

/**
 * @author Deftware
 */
public class TextComponent extends AbstractComponent {

	private BitmapFont font;
	private Consumer<Integer> clickCallback;
	private @Setter @Getter String text;

	public TextComponent(float x, float y, String font, int size, String text) {
		this(x, y, font, size, null, text);
	}

	public TextComponent(float x, float y, String font, int size, Consumer<Integer> clickCallback, String text) {
		super(x, y);
		this.text = text;
		this.clickCallback = clickCallback;
		try {
			this.font = FontManager.getFont(font, size, FontManager.Modifiers.ANTIALIASED);
			this.font.setShadowSize(1);
			this.font.initialize(Color.white, "");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public float getWidth() {
		return font.getStringWidth(text);
	}

	@Override
	public float getHeight() {
		return font.getStringHeight(text);
	}

	@Override
	public void render(float x, float y, double mouseX, double mouseY) {
		font.drawString((int) x, (int) y, text);
	}

	@Override
	public void update() { }

	@Override
	public boolean mouseClicked(double x, double y, int mouseButton) {
		if (x > this.getX() && x < this.getX() + font.getStringWidth(text) && y > this.getY() && y < this.getY() + font.getStringHeight(text) && clickCallback != null) {
			clickCallback.accept(mouseButton);
			return true;
		}
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
