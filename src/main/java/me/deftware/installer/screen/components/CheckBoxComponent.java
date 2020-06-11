package me.deftware.installer.screen.components;

import lombok.Getter;
import lombok.Setter;
import me.deftware.installer.resources.RenderSystem;
import me.deftware.installer.resources.ResourceUtils;
import me.deftware.installer.resources.Texture;
import me.deftware.installer.resources.font.BitmapFont;
import me.deftware.installer.resources.font.FontManager;
import me.deftware.installer.screen.AbstractComponent;

import java.awt.*;

/**
 * @author Deftware
 */
public class CheckBoxComponent extends AbstractComponent {

	private Texture arrow;
	private BitmapFont font;
	private @Getter float width, height;
	private @Getter @Setter boolean checked = false;
	private String text;

	public CheckBoxComponent(float x, float y, String text, String font, int fontSize) {
		super(x, y);
		this.font = FontManager.getFont(font, fontSize, FontManager.Modifiers.ANTIALIASED);
		this.font.setShadowSize(1);
		this.font.initialize(Color.white, "");
		this.width = this.font.getStringWidth(text) ;
		this.text = text;
		height = this.font.getStringHeight("ABC") + 6;
		try {
			arrow = ResourceUtils.loadTexture("/assets/down_arrow.png", 15);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void render(float x, float y, double mouseX, double mouseY) {
		font.drawString((int) (x + height + 10), (int) y + 2, text);
		RenderSystem.drawRect(x, y, x + height, y + height, Color.darkGray);
		try {
			if (checked) {
				arrow.draw(x + ((height / 2) - (arrow.getWidth() / 2)), y + ((height / 2) - (arrow.getHeight() / 2)));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public boolean mouseClicked(double x, double y, int mouseButton) {
		if (x > getX() && x < getX() + width + height + 5 && y > getY() && y < getY() + height) {
			checked = !checked;
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
