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

	/**
	 * 0 = Not enabled
	 * 1 = Fade in
	 * 2 = Fade out
	 */
	private @Getter @Setter int fadeStatus = 0, alpha = 255;

	private double iterations = 50, i = 0, counter = 0, increase = Math.PI / iterations;

	private Consumer<Integer> fadeCallback;

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

	public void fadeOut(Consumer<Integer> callback) {
		fadeCallback = callback;
		fadeStatus = 2;
	}

	public void fadeIn(Consumer<Integer> callback) {
		fadeCallback = callback;
		fadeStatus = 1;
	}

	public void setText(String... text) {
		this.text = Arrays.asList(text);
	}

	@Override
	public void update() {
		if (fadeStatus != 0) {
			if (i <= 1 && (fadeStatus == 2 && alpha > 3 || fadeStatus == 1 && alpha < 255)) {
				double current = Math.sin(counter) * (255 / iterations * counter);
				alpha = (int) (fadeStatus == 2 ? alpha - current : alpha + current);
				counter += increase;
				i += 1 / iterations;
			} else {
				fadeStatus = 0;
				i = 0;
				counter = 0;
				if (fadeCallback != null) {
					fadeCallback.accept(alpha);
				}
			}
		}
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
