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
	protected @Getter @Setter boolean readOnly = false;
	private @Setter Consumer<String> textChanged;
	private boolean highlighted = false;

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
	public int cursorTest(double x, double y) {
		if (x > getX() && x < getX() + getWidth() && y > getY() && y < getY() + getHeight() && !readOnly) {
			return GLFW.GLFW_IBEAM_CURSOR;
		}
		return GLFW.GLFW_ARROW_CURSOR;
	}

	@Override
	public void render(float x, float y, double mouseX, double mouseY) {
		RenderSystem.drawRect(x, y, x + width, y + height, ThemeEngine.getTheme().getOutlineColor());
		RenderSystem.drawRect(x + 1, y + 1, x + width - 1, y + height - 1, ThemeEngine.getTheme().getBackgroundColor());
		String drawString = text;
		if (font.getStringWidth(text) < maxTextLength) {
			font.drawString((int) x + 6 + (text.isEmpty() && focused ? shadowText.isEmpty() ? 8 : 4 : 0), (int) y + 3, text.isEmpty() ? Color.lightGray : ThemeEngine.getColorWithAlpha(ThemeEngine.getTheme().getTextColor(), alpha), text.isEmpty() ? shadowText : text);
		} else {
			int numChars = 0, offsetIndex = 0;
			for (int i = 0; i < drawString.length(); i++) {
				if (font.getStringWidth(drawString.substring(0, i + 1) + "....") > maxTextLength) {
					if (numChars == 0) {
						numChars = i;
					}
				}
			}
			for (int i = drawString.length(); i > -1; i--) {
				if (font.getStringWidth(drawString.substring(i - 2)) > maxTextLength) {
					offsetIndex = i;
					break;
				}
			}
			if (!focused) {
				if (drawString.substring(0, numChars - 1).equalsIgnoreCase(" ")) {
					numChars--;
				}
				drawString = drawString.substring(0, numChars) + "...";
			} else {
				drawString = drawString.substring(offsetIndex);
			}
			font.drawString((int) x + 6, (int) y + 3, ThemeEngine.getColorWithAlpha(ThemeEngine.getTheme().getTextColor(), alpha), drawString);
		}
		if (!text.isEmpty() && highlighted) {
			RenderSystem.drawRect(x + 1, y + 1, x + font.getStringWidth(drawString) + 12, y + height - 1, ThemeEngine.getColorWithAlpha(ThemeEngine.getTheme().getTextHighlightColor(), 100));
		}
		if (cursorTick && focused) {
			RenderSystem.drawRect(x + font.getStringWidth(drawString) + 6, y + 3, x + font.getStringWidth(drawString) + 3 + 6, y + height - 3, Color.LIGHT_GRAY);
		}
	}

	@Override
	public void deFocus() {
		focused = false;
	}

	public void setText(String text) {
		font.setText(text);
		this.text = text;
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
		if (focused && mouseButton == 2) {
			text += GLFW.glfwGetClipboardString(Main.getWindow().windowHandle);
		} else {
			focused = focused && mouseButton == 0;
			highlighted = false;
		}
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
				highlighted = false;
				text = text.substring(0, text.length() - 1);
				if (mods == GLFW.GLFW_MOD_CONTROL) {
					text = "";
				}
			} else if (keycode == GLFW.GLFW_KEY_V && mods == GLFW.GLFW_MOD_CONTROL) {
				text += GLFW.glfwGetClipboardString(Main.getWindow().windowHandle);
			} else if ((keycode == GLFW.GLFW_KEY_C || keycode == GLFW.GLFW_KEY_X) && mods == GLFW.GLFW_MOD_CONTROL && highlighted) {
				GLFW.glfwSetClipboardString(Main.getWindow().windowHandle, text);
				highlighted = false;
				if (keycode == GLFW.GLFW_KEY_X) {
					text = "";
				}
			} else if (keycode == GLFW.GLFW_KEY_A && mods == GLFW.GLFW_MOD_CONTROL) {
				highlighted = true;
			} else if (keycode == GLFW.GLFW_KEY_ESCAPE) {
				highlighted = false;
			}
			setText(text);
		}
	}

	@Override
	public void onScroll(double xPos, double yPos) { }

}
