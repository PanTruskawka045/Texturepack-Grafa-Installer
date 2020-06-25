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
import java.util.function.Consumer;

/**
 * @author Deftware
 */
public class ComboBoxComponent extends AbstractComponent {

	private Texture arrow;
	private BitmapFont font;
	private @Getter float width, height, scrollbarWidth = 17, scrollbarHeight = 45, scrollbarY = 0;
	private @Getter @Setter int index = 0, hoverIndex = 0, maxItems = 5, indexOffset = 0;
	private String[] items;
	private boolean expanded = false, scrollbarDrag = false;
	private @Setter Consumer<String> itemChangedCallback;
	private double prevMouseY = 0;
	private long lastMouseCompare = System.currentTimeMillis();

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
		if (itemChangedCallback != null) {
			itemChangedCallback.accept(getSelectedItem());
		}
	}

	public void updateItems(String... items) {
		index = indexOffset = 0;
		this.items = items;
		maxItems = items.length + 1 < 5 ? items.length : 5;
	}

 	public String getSelectedItem() {
		return items[index];
	}

	public boolean hasScrolling() {
		return items.length + 1 >= 5;
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
			float oriY = y;
			y = y + 6 + font.getStringHeight(getSelectedItem());
			int loopIndex = 0;
			hoverIndex = -1;
			for (String item : items) {
				if (loopIndex >= indexOffset && loopIndex - indexOffset < maxItems) {
					if (mouseY > y - 1 && mouseY < y + 6 + font.getStringHeight(getSelectedItem()) && mouseX > x && mouseX < x + width - scrollbarWidth) {
						RenderSystem.drawRect(x + 1, y - 1, x + width - 1, y + 5 + font.getStringHeight(getSelectedItem()), ColorPalette.BRIGHT_BACKGROUND_COLOR);
						hoverIndex = loopIndex;
					}
					font.drawString((int) x + 6, (int) y, item);
					y += 6 + font.getStringHeight(getSelectedItem());
				}
				loopIndex++;
			}
			RenderSystem.drawLine(x, oriY + height - 1, x + width, oriY + height);
			if (hasScrolling()) {
				y = oriY;
				RenderSystem.drawRect(x + width - scrollbarWidth, y + height + 1, x + width - 1, y + (height * (maxItems + 1)) - 1, ColorPalette.BRIGHT_BACKGROUND_COLOR.brighter());
				if (indexOffset != 0) {
					float offset = maxItems * height / items.length;
					y += indexOffset * offset;
				}
				scrollbarY = y;
				RenderSystem.drawRect(x + width - scrollbarWidth, y + height + 1, x + width - 1, y + height + 3 + scrollbarHeight, ColorPalette.BRIGHT_BACKGROUND_COLOR.brighter().brighter());
			}
		}
		prevMouseY = mouseY;
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
	public void update() {
		if (scrollbarDrag) {
			if (!(prevMouseY > scrollbarY + height && prevMouseY < scrollbarY + height + scrollbarHeight)) {
				if (scrollbarY + height < prevMouseY) {
					onScroll(0, -1);
				} else {
					onScroll(0, 1);
				}
			}
		}
	}

	@Override
	public boolean mouseClicked(double x, double y, int mouseButton) {
		boolean prevValue = expanded;
		if (x > getX() && x < getX() + width && y > getY() && y < getY() + height) {
			expanded = !expanded;
		} else if (expanded) {
			if (hoverIndex != -1) {
				index = hoverIndex;
				if (itemChangedCallback != null) {
					itemChangedCallback.accept(getSelectedItem());
				}
				expanded = false;
			} else if (x > getX() + width - scrollbarWidth && x < getX() + width && y > getY() + height && y < getY() + (height * (maxItems + 1))) {
				scrollbarDrag = true;
			} else {
				expanded = false;
			}
			return true;
		}
		if (prevValue && !expanded) {
			indexOffset = 0;
		}
		return false;
	}

	@Override
	public void mouseReleased(double x, double y, int mouseButton) {
		scrollbarDrag = false;
	}

	@Override
	public void charTyped(char typedChar) { }

	@Override
	public void keyPressed(int keycode, int mods) { }

}
