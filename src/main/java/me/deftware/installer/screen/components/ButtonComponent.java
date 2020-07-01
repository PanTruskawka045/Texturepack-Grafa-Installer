package me.deftware.installer.screen.components;

import lombok.Getter;
import lombok.Setter;
import me.deftware.installer.engine.theming.ThemeEngine;
import me.deftware.installer.resources.RenderSystem;
import me.deftware.installer.screen.AbstractComponent;

import java.awt.*;
import java.util.function.Consumer;

/**
 * A clickable button
 *
 * @author Deftware
 */
public class ButtonComponent extends AbstractComponent<ButtonComponent> {

	private @Getter final TextComponent font;
	private @Getter final float width, height;
	private @Getter String text;
	private final Consumer<Integer> onClick;
	private @Setter boolean visible = true;
	private Color currentColor = ThemeEngine.getTheme().getForegroundColor();
	private boolean mouseOver = false;
	private float ratio = 0.5f;

	public ButtonComponent(float x, float y, float width, float height, String text, Consumer<Integer> onClick) {
		super(x, y);
		this.width = width;
		this.height = height;
		this.onClick = onClick;
		this.text = text;
		font = new TextComponent(x, y, 23, text);
	}

	@Override
	public void render(float x, float y, double mouseX, double mouseY) {
		if (visible) {
			Color bgColor = new Color(currentColor.getRed(), currentColor.getGreen(), currentColor.getBlue(), alpha);
			RenderSystem.drawRect(x, y, x + width, y + height, bgColor);
			RenderSystem.drawCircle(x, y + 25, 25, bgColor);
			RenderSystem.drawCircle(x + width, y + 25, 25, bgColor);
			font.drawString((int) (x + ((width / 2) - (font.getWidth()/ 2))), (int) (y + ((height / 2) - (font.getHeight() / 2))), ThemeEngine.getColorWithAlpha(ThemeEngine.getTheme().getTextColor(), alpha), text);
			mouseOver = mouseX > getX() - 25 && mouseX < getX() + width + 25 && mouseY > getY() && mouseY < getY() + height;
		}
	}

	@Override
	public void update() {
		super.update();
		if (mouseOver) {
			if (ratio > 0.4f) {
				ratio -= 0.02f;
			}
		} else {
			if (ratio < 0.5f) {
				ratio += 0.02f;
			}
		}
		currentColor = ThemeEngine.blend(ratio, ThemeEngine.getTheme().getBackgroundColor().brighter().brighter(), ThemeEngine.getTheme().getForegroundColor());
	}

	@Override
	public boolean mouseClicked(double x, double y, int mouseButton) {
		if (x > getX() - 25 && x < getX() + width + 25 && y > getY() && y < getY() + height && visible && mouseButton == 0) {
			onClick.accept(mouseButton);
			return true;
		}
		return false;
	}

	public void setText(String text) {
		this.text = text;
		font.setText(text);
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
