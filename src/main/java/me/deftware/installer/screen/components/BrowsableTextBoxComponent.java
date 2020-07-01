package me.deftware.installer.screen.components;

import me.deftware.installer.engine.theming.ThemeEngine;
import me.deftware.installer.resources.RenderSystem;
import org.lwjgl.util.tinyfd.TinyFileDialogs;

/**
 * @author Deftware
 */
public class BrowsableTextBoxComponent extends TextBoxComponent {

	public BrowsableTextBoxComponent(float x, float y, float width, int fontSize, String text) {
		super(x, y, width, fontSize, text);
		setReadOnly(true);
		maxTextLength -= height;
	}

	@Override
	public void render(float x, float y, double mouseX, double mouseY) {
		super.render(x, y, mouseX, mouseY);
		RenderSystem.drawRect(x + width - height + 1, y + 1, x + width - 1, y + height - 1, ThemeEngine.getTheme().getForegroundColor());
		font.drawString((int) (x + width - height + 9), (int) (y), ThemeEngine.getColorWithAlpha(ThemeEngine.getTheme().getTextColor(), alpha),"...");
	}

	@Override
	public boolean mouseClicked(double x, double y, int mouseButton) {
		if (x > getX() + width - height && x < getX() + width && y > getY() && y < getY() + height && mouseButton == 0) {
			String folder = TinyFileDialogs.tinyfd_selectFolderDialog("Select path", "");
			if (folder != null && !folder.isEmpty()) {
				text = folder;
			}
			return true;
		}
		return super.mouseClicked(x, y, mouseButton);
	}

}
