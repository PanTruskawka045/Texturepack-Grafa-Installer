package me.deftware.installer.screen.components;

import lombok.Getter;
import lombok.Setter;
import me.deftware.installer.engine.ColorPalette;
import me.deftware.installer.resources.RenderSystem;
import me.deftware.installer.resources.font.BitmapFont;
import me.deftware.installer.resources.font.FontManager;
import me.deftware.installer.screen.AbstractComponent;

import java.awt.*;
import java.util.function.Consumer;

/**
 * @author Deftware
 */
public class ButtonComponent extends AbstractComponent {

	private @Getter float width, height;
	private BitmapFont font;
	private @Getter @Setter String text;
	private Consumer<Integer> onClick;
	private @Setter boolean visible = true;
	private Color currentColor = ColorPalette.BRIGHT_BACKGROUND_COLOR;
	private boolean mouseOver = false;
	private float ratio = 0.5f;

	public ButtonComponent(float x, float y, float width, float height, String text, Consumer<Integer> onClick) {
		super(x, y);
		this.width = width;
		this.height = height;
		this.onClick = onClick;
		this.text = text;
		font = FontManager.getFont("Product Sans", 23, FontManager.Modifiers.ANTIALIASED);
		font.initialize(Color.white, "");
	}

	@Override
	public void render(float x, float y, double mouseX, double mouseY) {
		if (visible) {
			Color bgColor = new Color(currentColor.getRed(), currentColor.getGreen(), currentColor.getBlue(), alpha);
			RenderSystem.drawRect(x, y, x + width, y + height, bgColor);
			RenderSystem.drawCircle(x, y + 25, 25, bgColor);
			RenderSystem.drawCircle(x + width, y + 25, 25, bgColor);
			font.drawString((int) (x + ((width / 2) - (font.getStringWidth(text) / 2))), (int) (y + ((height / 2) - (font.getStringHeight(text) / 2))), text, new Color(255, 255, 255, alpha));
			mouseOver = mouseX > getX() && mouseX < getX() + width && mouseY > getY() && mouseY < getY() + height;
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
		currentColor = ColorPalette.blend(ratio, ColorPalette.BACKGROUND_COLOR.brighter().brighter(), ColorPalette.BRIGHT_BACKGROUND_COLOR);
	}

	@Override
	public boolean mouseClicked(double x, double y, int mouseButton) {
		if (x > getX() && x < getX() + width && y > getY() && y < getY() + height && visible) {
			onClick.accept(mouseButton);
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
