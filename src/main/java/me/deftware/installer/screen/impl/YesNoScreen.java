package me.deftware.installer.screen.impl;

import lombok.Setter;
import me.deftware.installer.screen.AbstractScreen;
import me.deftware.installer.screen.components.ButtonComponent;
import me.deftware.installer.screen.components.TextComponent;

import java.util.function.Consumer;

public class YesNoScreen extends AbstractScreen {

	private @Setter String rightButton = "Continue", leftButton = "Go back";

	private String[] text;
	private String title;
	private Consumer<Boolean> callback;

	public YesNoScreen(String title, Consumer<Boolean> callback, String... text) {
		this.title = title;
		this.text = text;
		this.callback = callback;
	}

	@Override
	public void init() {
		TextComponent titleComponent = new TextComponent(0, 0, "Product Sans", 35, title);
		titleComponent.centerHorizontally().centerVertically(-130);
		TextComponent subText = new TextComponent(0, 0, "Product Sans", 25, text);
		subText.centerHorizontally();
		subText.setY(titleComponent.getY() + titleComponent.getHeight() + 10);
		addComponent(titleComponent, subText);
		addComponent(new ButtonComponent(0, 0, 100, 50, leftButton, mouseButton -> callback.accept(false)).centerVertically(170).centerHorizontally(-100));
		addComponent(new ButtonComponent(0, 0, 100, 50, rightButton, mouseButton -> callback.accept(true)).centerVertically(170).centerHorizontally(100));
	}

}
