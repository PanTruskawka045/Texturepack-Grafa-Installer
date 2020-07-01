package me.deftware.installer.screen.components;

import lombok.Getter;
import lombok.Setter;
import me.deftware.installer.Main;
import me.deftware.installer.engine.theming.ThemeEngine;
import me.deftware.installer.resources.RenderSystem;
import me.deftware.installer.resources.font.BitmapFont;
import me.deftware.installer.resources.font.FontManager;
import me.deftware.installer.screen.AbstractComponent;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.util.function.Consumer;

/**
 * A textbox
 *
 * @author Deftware
 */
public class TextBoxComponent extends AbstractComponent<TextBoxComponent> {

	protected @Getter TextComponent font;
	private long lastMs = System.currentTimeMillis();
	protected @Getter float width, height, maxTextLength;
	private @Getter boolean focused = false, cursorTick = true;
	protected @Getter String text;
	private @Getter @Setter String shadowText = "";
	private @Getter @Setter boolean readOnly = false;
	private @Setter Consumer<String> textChanged;

	public TextBoxComponent(float x, float y, float width, int fontSize, String text) {
		super(x, y);
		this.font = new TextComponent(x, y, fontSize, text);
		this.font.setCenteredText(false);
		this.width = width;
		this.maxTextLength = width;
		this.text = text;
		height = this.font.getHeight() + 6;
	}

	@Override
	public void render(float x, float y, double mouseX, double mouseY) {
		RenderSystem.drawRect(x, y, x + width, y + height, ThemeEngine.getTheme().getOutlineColor());
		RenderSystem.drawRect(x + 1, y + 1, x + width - 1, y + height - 1, ThemeEngine.getTheme().getBackgroundColor());
		String drawString = text;
		if (font.getWidth() < maxTextLength) {
			font.drawString((int) x + 6 + (text.isEmpty() && focused ? 8 : 0), (int) y + 3, text.isEmpty() ? Color.lightGray : ThemeEngine.getColorWithAlpha(ThemeEngine.getTheme().getTextColor(), alpha), text.isEmpty() ? shadowText : text);
		} else {
			int numChars = 0;
			for (int i = drawString.length(); i > -1; i--) {
				if (font.getStringWidth(drawString.substring(0, i) + "...") < maxTextLength) {
					numChars = i;
					break;
				}
			}
			drawString = drawString.substring(0, numChars) + "...";
			font.drawString((int) x + 6, (int) y + 3, ThemeEngine.getColorWithAlpha(ThemeEngine.getTheme().getTextColor(), alpha), drawString);
		}
		if (cursorTick && focused) {
			RenderSystem.drawRect(x + font.getWidth() + 6, y + 3, x + font.getWidth() + 3 + 6, y + height - 3, Color.LIGHT_GRAY);
		}
	}

	public void setText(String text) {
		this.text = text;
		font.setText(text);
		if (textChanged != null) {
			textChanged.accept(text);
		}
	}

	@Override
	public void update() {
		if (focused && lastMs + 500 < System.currentTimeMillis()) {
			lastMs = System.currentTimeMillis();
			cursorTick = !cursorTick;
		}
	}

	@Override
	public boolean mouseClicked(double x, double y, int mouseButton) {
		boolean prevState = focused;
		focused = x > getX() && x < getX() + getWidth() && y > getY() && y < getY() + getHeight() && !readOnly;
		if (!prevState && focused) {
			lastMs = System.currentTimeMillis();
			cursorTick = true;
			return true;
		}
		return false;
	}

	@Override
	public void mouseReleased(double x, double y, int mouseButton) { }

	@Override
	public void charTyped(char typedChar) {
		if (focused) {
			text += typedChar;
		}
	}

	@Override
	public void keyPressed(int keycode, int mods) {
		if (focused) {
			if (keycode == GLFW.GLFW_KEY_BACKSPACE && !text.isEmpty()) {
				text = text.substring(0, text.length() - 1);
			} else if (keycode == GLFW.GLFW_KEY_V && mods == GLFW.GLFW_MOD_CONTROL) {
				text += GLFW.glfwGetClipboardString(Main.getWindow().windowHandle);
			} else if (keycode == GLFW.GLFW_KEY_C && mods == GLFW.GLFW_MOD_CONTROL) {
				GLFW.glfwSetClipboardString(Main.getWindow().windowHandle, text);
			}
			setText(text);
		}
	}

	@Override
	public void onScroll(double xPos, double yPos) { }

}
