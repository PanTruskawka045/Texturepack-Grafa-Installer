package me.deftware.installer.screen.components;

import lombok.Getter;
import lombok.Setter;
import me.deftware.installer.Main;
import me.deftware.installer.engine.ColorPalette;
import me.deftware.installer.resources.RenderSystem;
import me.deftware.installer.resources.font.BitmapFont;
import me.deftware.installer.resources.font.FontManager;
import me.deftware.installer.screen.AbstractComponent;
import org.lwjgl.glfw.GLFW;

import java.awt.*;

/**
 * @author Deftware
 */
public class TextBoxComponent extends AbstractComponent {

	protected BitmapFont font;
	private long lastMs = System.currentTimeMillis();
	protected @Getter float width, height, maxTextLength;
	private @Getter boolean focused = false, cursorTick = true;
	protected @Getter @Setter String text, shadowText = "";

	public TextBoxComponent(float x, float y, float width, int fontSize, String text) {
		this(x, y, width, fontSize, "Product Sans", text);
	}

	public TextBoxComponent(float x, float y, float width, int fontSize, String font, String text) {
		super(x, y);
		this.font = FontManager.getFont(font, fontSize, FontManager.Modifiers.ANTIALIASED);
		this.font.setShadowSize(1);
		this.font.initialize(Color.white, "");
		this.width = width;
		this.maxTextLength = width;
		this.text = text;
		height = this.font.getStringHeight("ABC") + 6;
	}

	@Override
	public void render(float x, float y, double mouseX, double mouseY) {
		RenderSystem.drawRect(x, y, x + width, y + height, Color.LIGHT_GRAY);
		RenderSystem.drawRect(x + 1, y + 1, x + width - 1, y + height - 1, ColorPalette.BACKGROUND_COLOR);
		String drawString = text;
		if (font.getStringWidth(text) < maxTextLength) {
			font.drawString((int) x + 6 + (text.isEmpty() && focused ? 8 : 0), (int) y + 3, text.isEmpty() ? shadowText : text, text.isEmpty() ? Color.lightGray : Color.white);
		} else {
			int numChars = 0;
			for (int i = drawString.length(); i > -1; i--) {
				if (font.getStringWidth(drawString.substring(0, i) + "...") < maxTextLength) {
					numChars = i;
					break;
				}
			}
			drawString = drawString.substring(0, numChars) + "...";
			font.drawString((int) x + 6, (int) y + 3, drawString, Color.white);
		}
		if (cursorTick && focused) {
			RenderSystem.drawRect(x + font.getStringWidth(drawString) + 6, y + 3, x + font.getStringWidth(drawString) + 3 + 6, y + height - 3, Color.LIGHT_GRAY);
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
		focused = x > getX() && x < getX() + getWidth() && y > getY() && y < getY() + getHeight();
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
		}
	}

	@Override
	public void onScroll(double xPos, double yPos) { }

}
