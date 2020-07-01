package me.deftware.installer.screen.components;

import lombok.Getter;
import lombok.Setter;
import me.deftware.installer.engine.theming.ThemeEngine;
import me.deftware.installer.resources.RenderSystem;
import me.deftware.installer.resources.ResourceUtils;
import me.deftware.installer.resources.Texture;
import me.deftware.installer.resources.font.BitmapFont;
import me.deftware.installer.resources.font.FontManager;
import me.deftware.installer.screen.AbstractComponent;

import java.awt.*;
import java.util.function.Consumer;

/**
 * A checkable combobox
 *
 * @author Deftware
 */
public class CheckBoxComponent extends AbstractComponent<CheckBoxComponent> {

	private Texture arrow;
	private final TextComponent font;
	private final @Getter float width, height;
	private @Getter @Setter boolean checked = false;
	private @Setter @Getter String text;
	private @Setter Consumer<Boolean> onCheckCallback;

	public CheckBoxComponent(float x, float y, String text, int fontSize) {
		super(x, y);
		this.font = new TextComponent(x, y, fontSize, text);
		this.width = this.font.getWidth();
		this.text = text;
		height = this.font.getHeight() + 6;
		try {
			arrow = ResourceUtils.loadTexture("/assets/down_arrow.png", 15);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void render(float x, float y, double mouseX, double mouseY) {
		font.drawString((int) (x + height + 10), (int) y + 2, ThemeEngine.getTheme().getTextColor(), text);
		RenderSystem.drawRect(x, y, x + height, y + height, ThemeEngine.getTheme().getBrightBackgroundColor());
		try {
			if (checked) {
				RenderSystem.glColor(ThemeEngine.getTheme().getTextColor());
				arrow.draw(x + ((height / 2) - (arrow.getWidth() / 2)), y + ((height / 2) - (arrow.getHeight() / 2)));
				RenderSystem.glColor(Color.white);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public boolean mouseClicked(double x, double y, int mouseButton) {
		if (x > getX() && x < getX() + width + height + 5 && y > getY() && y < getY() + height) {
			checked = !checked;
			if (onCheckCallback != null) {
				onCheckCallback.accept(checked);
			}
			return true;
		}
		return false;
	}

	@Override
	public void update() { }

	@Override
	public void mouseReleased(double x, double y, int mouseButton) { }

	@Override
	public void charTyped(char typedChar) { }

	@Override
	public void keyPressed(int keycode, int mods) { }

	@Override
	public void onScroll(double xPos, double yPos) { }

}
