package me.deftware.installer.screen.components;

import me.deftware.installer.Main;
import me.deftware.installer.engine.ColorPalette;
import me.deftware.installer.resources.RenderSystem;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * @author Deftware
 */
public class BrowsableTextBoxComponent extends TextBoxComponent {

	public BrowsableTextBoxComponent(float x, float y, float width, int fontSize, String text) {
		super(x, y, width, fontSize, text);
		maxTextLength -= height;
	}

	@Override
	public void render(float x, float y, double mouseX, double mouseY) {
		super.render(x, y, mouseX, mouseY);
		RenderSystem.drawRect(x + width - height + 1, y + 1, x + width - 1, y + height - 1, ColorPalette.BRIGHT_BACKGROUND_COLOR);
		font.drawString((int) (x + width - height + 9), (int) (y), "...");
	}

	@Override
	public boolean mouseClicked(double x, double y, int mouseButton) {
		if (x > getX() + width - height && x < x + width && y > getY() && y < getY() + height) {
			new Thread(() -> {
				Main.getWindow().shouldRun = false;
				JFrame frame = new JFrame();
				frame.setAlwaysOnTop(true);
				JFileChooser fc = new JFileChooser();
				fc.setCurrentDirectory(new File("."));
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				fc.setDialogTitle("Select minecraft directory");
				int returnVal = fc.showSaveDialog(frame);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					text = fc.getSelectedFile().getAbsolutePath();
				}
				Main.getWindow().shouldRun = true;
			}).start();
			return true;
		}
		return super.mouseClicked(x, y, mouseButton);
	}

}
