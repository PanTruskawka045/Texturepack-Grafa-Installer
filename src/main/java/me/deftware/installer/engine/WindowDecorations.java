package me.deftware.installer.engine;

import lombok.Getter;
import lombok.Setter;
import me.deftware.installer.Main;
import me.deftware.installer.engine.theming.ThemeEngine;
import me.deftware.installer.resources.RenderSystem;
import me.deftware.installer.resources.font.BitmapFont;
import me.deftware.installer.resources.font.FontManager;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.nio.IntBuffer;

/**
 * Custom implementation of the OS default window decorations.
 * Handles dragging the window and closing it.
 *
 * Only works on Windows, and Linux distributions without Wayland
 *
 * @author Deftware
 */
public class WindowDecorations {

	private BitmapFont font;
	private final IntBuffer xPos = BufferUtils.createIntBuffer(1), yPos = BufferUtils.createIntBuffer(1);

	private double cursorPosX, cursorPosY, cursorPosXOffset, cursorPosYOffset;
	private @Getter int windowPosX, windowPosY, buttonEvent, titleBarHeight = 40, navButtonsSize = 20;
	private @Getter @Setter String windowTitle;
	private @Getter @Setter boolean centeredTitle = false;

	public WindowDecorations(String title) {
		windowTitle = title;
		GLFW.glfwSetCursorPosCallback(Main.getWindow().getWindowHandle(), (window, x, y) -> {
			if (buttonEvent == 1) {
				cursorPosXOffset = x - cursorPosX;
				cursorPosYOffset = y - cursorPosY;
			}
		});

		GLFW.glfwSetMouseButtonCallback(Main.getWindow().getWindowHandle(), (window, button, action, mods) -> {
			Main.getWindow().mousePressed(button, action);
			if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT && action == GLFW.GLFW_PRESS) {
				buttonEvent = 1;
				cursorPosX = Math.floor(Main.getWindow().getMouseX());
				cursorPosY = Math.floor(Main.getWindow().getMouseY());
			}
			if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT && action == GLFW.GLFW_RELEASE) {
				buttonEvent = 0;
				cursorPosX = 0;
				cursorPosY = 0;
			}
		});

		font = FontManager.getFont( "Product Sans", 20, FontManager.Modifiers.ANTIALIASED);
		font.initialize(Color.white, "");
	}

	public void loop() {
		int offset = 10;
		if (buttonEvent == 1 && Main.getWindow().mouseY < titleBarHeight) {
			if (Main.getWindow().mouseX > Main.getWindow().windowWidth - offset - navButtonsSize && Main.getWindow().mouseX < Main.getWindow().windowWidth - offset &&
					Main.getWindow().mouseY > offset && Main.getWindow().mouseY < offset + navButtonsSize) {
				GLFW.glfwSetWindowShouldClose(Main.getWindow().getWindowHandle(), true);
			}
			GLFW.glfwGetWindowPos(Main.getWindow().getWindowHandle(), xPos, yPos);
			windowPosX = xPos.get();
			windowPosY = yPos.get();
			GLFW.glfwSetWindowPos(Main.getWindow().getWindowHandle(), (int) (windowPosX + cursorPosXOffset), (int) (windowPosY + cursorPosYOffset));
			yPos.clear();
			xPos.clear();
		}
		//RenderSystem.drawRect(0, 0, Main.getWindow().windowWidth, titleBarHeight, new Color(19,29,39));
		font.drawStringWithShadow(centeredTitle ? (Main.getWindow().windowWidth / 2) - (font.getStringWidth(windowTitle) / 2) : (titleBarHeight / 2) - (font.getStringHeight(windowTitle) / 2), (titleBarHeight / 2) - (font.getStringHeight(windowTitle) / 2), windowTitle);
		// Exit button
		RenderSystem.drawLine(Main.getWindow().windowWidth - navButtonsSize - offset, offset, Main.getWindow().windowWidth - offset, offset + navButtonsSize, ThemeEngine.getTheme().getOutlineColor());
		RenderSystem.drawLine(Main.getWindow().windowWidth - navButtonsSize - offset, offset + navButtonsSize, Main.getWindow().windowWidth - offset, offset, ThemeEngine.getTheme().getOutlineColor());
	}

}
