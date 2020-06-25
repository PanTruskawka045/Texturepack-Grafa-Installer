package me.deftware.installer.screen.components;

import lombok.Getter;
import lombok.Setter;
import me.deftware.installer.resources.font.BitmapFont;
import me.deftware.installer.resources.font.FontManager;
import me.deftware.installer.screen.AbstractComponent;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author Deftware
 */
@SuppressWarnings("FieldMayBeFinal")
public class TextComponent extends AbstractComponent {

	private BitmapFont font;
	private @Setter Consumer<Integer> clickCallback;
	private @Getter List<String> text;
	private @Getter @Setter boolean centeredText = true;

	public TextComponent(float x, float y, String font, int size, String... text) {
		this(x, y, font, size, null, text);
	}

	public TextComponent(float x, float y, String font, int size, Consumer<Integer> clickCallback, String... text) {
		super(x, y);
		this.text = Arrays.asList(text);
		this.clickCallback = clickCallback;
		try {
			this.font = FontManager.getFont(font, size, FontManager.Modifiers.ANTIALIASED);
			this.font.setShadowSize(1);
			this.font.initialize(Color.white, "");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void setText(String... text) {
		this.text = Arrays.asList(text);
	}

	@Override
	public void update() {
		super.update();
	}

	@Override
	public float getWidth() {
		int maxLength = 0;
		for (String line : text) {
			if (maxLength < font.getStringWidth(line)) {
				maxLength = font.getStringWidth(line);
			}
		}
		return maxLength;
	}

	@Override
	public float getHeight() {
		return font.getStringHeight("ABC123") * text.size();
	}

	@Override
	public void render(float x, float y, double mouseX, double mouseY) {
		for (String line : text) {
			if (!centeredText) {
				font.drawString((int) x, (int) y, line,  new Color(255, 255, 255, alpha));
			} else {
				font.drawString((int) (x + ((getWidth() / 2) - (font.getStringWidth(line) / 2))), (int) y, line,  new Color(255, 255, 255, alpha));
				y += font.getStringHeight(line);
			}
		}
	}

	@Override
	public boolean mouseClicked(double x, double y, int mouseButton) {
		if (x > this.getX() && x < this.getX() + getWidth() && y > this.getY() && y < this.getY() + getWidth() && clickCallback != null) {
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
