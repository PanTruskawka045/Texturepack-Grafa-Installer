package me.deftware.installer.screen.components;

import lombok.Getter;
import lombok.Setter;
import me.deftware.installer.engine.ColorPalette;
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
public class ComboBoxComponent extends AbstractComponent {

	private Texture arrow;
	private BitmapFont font;
	private @Getter float width, height;
	private @Getter @Setter int index = 0, hoverIndex = 0, maxItems = 5, indexOffset = 0;
	private String[] items;
	private boolean expanded = false;

	public ComboBoxComponent(float x, float y, float width, int size, String font, String... items) {
		super(x, y);
		this.font = FontManager.getFont(font, size, FontManager.Modifiers.ANTIALIASED);
		this.font.setShadowSize(1);
		this.font.initialize(Color.white, "");
		this.width = width;
		height = this.font.getStringHeight("ABC") + 6;
		this.items = items;
		maxItems = items.length + 1 < maxItems ? items.length : maxItems;
		try {
			arrow = ResourceUtils.loadTexture("/assets/down_arrow.png", 25);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

 	public String getSelectedItem() {
		return items[index];
	}

	@Override
	public void render(float x, float y, double mouseX, double mouseY) {
		RenderSystem.drawRect(x, y, x + width, y + (height * (expanded ? maxItems + 1 : 1)), Color.LIGHT_GRAY);
		RenderSystem.drawRect(x + 1, y + 1, x + width - 1, y + (height * (expanded ? maxItems + 1 : 1)) - 1, ColorPalette.BACKGROUND_COLOR);
		font.drawString((int) x + 6, (int) y + 3, getSelectedItem());
		RenderSystem.drawRect(x + width - height + 1, y + 1, x + width - 1, y + height - 1, ColorPalette.BRIGHT_BACKGROUND_COLOR);
		try {
			arrow.draw(x + width - height + ((height / 2) - (arrow.getWidth() / 2)), y + ((height / 2) - (arrow.getHeight() / 2)));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if (expanded) {
			y = y + 6 + font.getStringHeight(getSelectedItem());
			int loopIndex = 0;
			hoverIndex = -1;
			for (String item : items) {
				if (loopIndex >= indexOffset && loopIndex - indexOffset < maxItems) {
					if (mouseY > y - 1 && mouseY < y + 6 + font.getStringHeight(getSelectedItem()) && mouseX > x && mouseX < x + width) {
						RenderSystem.drawRect(x + 1, y - 1, x + width - 1, y + 5 + font.getStringHeight(getSelectedItem()), ColorPalette.BRIGHT_BACKGROUND_COLOR);
						hoverIndex = loopIndex;
					}
					font.drawString((int) x + 6, (int) y, item);
					y += 6 + font.getStringHeight(getSelectedItem());
				}
				loopIndex++;
			}
		}
	}

	@Override
	public void onScroll(double xPos, double yPos) {
		if (expanded && maxItems < items.length + 1) {
			if (indexOffset + -yPos > -1 && indexOffset + -yPos < items.length + 1 - maxItems) {
				indexOffset += -yPos;
			}
		}
	}

	@Override
	public void update() { }

	@Override
	public boolean mouseClicked(double x, double y, int mouseButton) {
		boolean prevValue = expanded;
		if (x > getX() && x < getX() + width && y > getY() && y < getY() + height) {
			expanded = !expanded;
		} else if (expanded) {
			if (hoverIndex != -1) {
				index = hoverIndex;
			}
			expanded = false;
			return true;
		}
		if (prevValue && !expanded) {
			indexOffset = 0;
		}
		return false;
	}

	@Override
	public void mouseReleased(double x, double y, int mouseButton) { }

	@Override
	public void charTyped(char typedChar) { }

	@Override
	public void keyPressed(int keycode, int mods) { }

}
