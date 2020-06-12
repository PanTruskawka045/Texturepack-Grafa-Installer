package me.deftware.installer.engine;

import lombok.Getter;
import me.deftware.installer.OSUtils;
import me.deftware.installer.resources.RenderSystem;
import me.deftware.installer.resources.font.BitmapFont;
import me.deftware.installer.resources.font.FontManager;
import me.deftware.installer.screen.AbstractScreen;
import me.deftware.installer.screen.impl.WelcomeScreen;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.awt.*;
import java.nio.DoubleBuffer;
import java.nio.IntBuffer;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author Deftware
 */
public class Window implements Runnable {

	public @Getter long windowHandle;
	public @Getter double mouseX, mouseY;
	public @Getter boolean borderlessWindow = true;
	public int windowWidth = 800, windowHeight = 500;
	private double iterations = 50, i = 0, counter = 0, increase = Math.PI / iterations;
	private final DoubleBuffer posX = BufferUtils.createDoubleBuffer(1), posY = BufferUtils.createDoubleBuffer(1);
	public AbstractScreen currentScreen, transitionScreen;
	private ScheduledFuture<?> updatedThread;
	private @Getter WindowDecorations windowDecorations;

	/**
	 * Set to false for things like opening dialogs
	 */
	public boolean shouldRun = true;

	@Override
	public void run() {
		if (OSUtils.isLinux()) {
			// Does not work with Wayland
			borderlessWindow = false;
		}

		// 60 times per second
		updatedThread = Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
			if (currentScreen != null) {
				currentScreen.update();
			}
			if (transitionScreen != null) {
				handleTransition();
			}
		}, 0, 1000 / 60, TimeUnit.MILLISECONDS);

		init();
		loop();

		updatedThread.cancel(true);
		Callbacks.glfwFreeCallbacks(windowHandle);
		GLFW.glfwDestroyWindow(windowHandle);

		GLFW.glfwTerminate();
		Objects.requireNonNull(GLFW.glfwSetErrorCallback(null)).free();
		System.exit(0);
	}

	private void handleTransition() {
		if (transitionScreen.getX() > 0) {
			if (i <= 1) {
				transitionScreen.setX((int) (transitionScreen.getX() - Math.sin(counter) * (windowWidth / iterations * counter)));
				counter += increase;
				i += 1 / iterations;
			}
			currentScreen.setX(transitionScreen.getX() - windowWidth);
		} else {
			currentScreen = transitionScreen;
			currentScreen.setX(0);
			transitionScreen = null;
		}
	}

	public void transitionTo(AbstractScreen screen) {
		counter = 0;
		i = 0;
		screen.setX(windowWidth);
		transitionScreen = screen;
	}

	public boolean isTransitioning() {
		return transitionScreen != null;
	}

	private void init() {
		GLFWErrorCallback.createPrint(System.err).set();
		if (!GLFW.glfwInit()) throw new IllegalStateException("Unable to initialize GLFW");

		GLFW.glfwDefaultWindowHints();
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_FALSE);

		if (borderlessWindow) {
			GLFW.glfwWindowHint(GLFW.GLFW_DECORATED, GLFW.GLFW_FALSE);
		}

		windowHandle = GLFW.glfwCreateWindow(windowWidth, windowHeight, "Aristois Installer", MemoryUtil.NULL, MemoryUtil.NULL);
		if (windowHandle == MemoryUtil.NULL) throw new RuntimeException("Failed to create the GLFW window");

		GLFW.glfwSetKeyCallback(windowHandle, (window, key, scancode, action, mods) -> {
			if (currentScreen != null && action == GLFW.GLFW_RELEASE && shouldRun) {
				currentScreen.keyPressed(key, mods);
			}
		});

		GLFW.glfwSetCharCallback(windowHandle, (window, codepoint) -> {
			if (currentScreen != null && shouldRun) {
				currentScreen.charTyped((char) codepoint);
			}
		});

		GLFW.glfwSetWindowCloseCallback(windowHandle, window -> {
			GLFW.glfwSetWindowShouldClose(windowHandle, true);
		});

		GLFW.glfwSetScrollCallback(windowHandle, (window, xPos, yPos) -> {
			if (currentScreen != null && shouldRun) {
				currentScreen.onScroll(xPos, yPos);
			}
		});

		if (!borderlessWindow) {
			GLFW.glfwSetMouseButtonCallback(windowHandle, (window, button, action, mods) -> {
				mousePressed(button, action);
			});
		}

		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer pWidth = stack.mallocInt(1),  pHeight = stack.mallocInt(1);

			GLFW.glfwGetWindowSize(windowHandle, pWidth, pHeight);
			GLFWVidMode vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());

			// Center the window
			GLFW.glfwSetWindowPos(
					windowHandle,
					(vidmode.width() - pWidth.get(0)) / 2,
					(vidmode.height() - pHeight.get(0)) / 2
			);
		}

		GLFW.glfwMakeContextCurrent(windowHandle);
		GLFW.glfwSwapInterval(1); // Enable v-sync
		GLFW.glfwShowWindow(windowHandle);
	}

	public void mousePressed(int button, int action) {
		if (currentScreen != null && !isTransitioning() && shouldRun) {
			if (action == GLFW.GLFW_PRESS) {
				currentScreen.mouseClicked(mouseX, mouseY, button);
			} else if (action == GLFW.GLFW_RELEASE) {
				currentScreen.mouseReleased(mouseX, mouseY, button);
			}
		}
	}

	private void setupView() {
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0.0, windowWidth, windowHeight, 0.0, -1.0, 1.0);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}

	private void loop() {
		GL.createCapabilities();
		RenderSystem.glClearColor(ColorPalette.BACKGROUND_COLOR);
		setupView();

		BitmapFont font = FontManager.getFont("Product Sans", 18, FontManager.Modifiers.ANTIALIASED);
		font.setShadowSize(1);
		font.initialize(Color.white, "");

		if (borderlessWindow) {
			windowDecorations = new WindowDecorations("Aristois Installer");
		}

		currentScreen = new WelcomeScreen();
		while (!GLFW.glfwWindowShouldClose(windowHandle)) {
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			if (borderlessWindow) windowDecorations.loop();

			GLFW.glfwGetCursorPos(windowHandle, posX, posY);
			mouseX = posX.get();
			mouseY = posY.get();
			posX.clear();
			posY.clear();

			GL11.glEnable(GL11.GL_TEXTURE_2D);
			if (currentScreen != null) {
				currentScreen.render(mouseX, mouseY);
			}
			if (transitionScreen != null) {
				transitionScreen.render(mouseX, mouseY);
			}
			font.drawString(4, windowHeight - font.getStringHeight("ABC") - 2, "aristois.net");
			GL11.glDisable(GL11.GL_TEXTURE_2D);

			GLFW.glfwSwapBuffers(windowHandle);
			GLFW.glfwPollEvents();
		}
	}

}
