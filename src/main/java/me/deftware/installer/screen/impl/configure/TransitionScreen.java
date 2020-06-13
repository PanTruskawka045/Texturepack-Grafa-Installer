package me.deftware.installer.screen.impl.configure;

import me.deftware.installer.screen.AbstractScreen;
import me.deftware.installer.screen.components.TextComponent;

import java.util.function.Consumer;

public class TransitionScreen extends AbstractScreen {

	private String[] text;
	private String title;
	private Consumer<Boolean> callback;

	public TransitionScreen(String title, Consumer<Boolean> callback, String... text) {
		this.title = title;
		this.text = text;
		this.callback = callback;
	}

	@Override
	public void init() {
		TextComponent titleComponent = new TextComponent(0, 0, "Product Sans", 35, title);
		titleComponent.centerHorizontally().centerVertically(-100);
		TextComponent subText = new TextComponent(0, 0, "Product Sans", 25, text);
		subText.centerHorizontally();
		subText.setY(titleComponent.getY() + titleComponent.getHeight() + 10);
		subText.setAlpha(1);
		titleComponent.setAlpha(1);
		addComponent(titleComponent, subText);
		titleComponent.fadeIn(null);
		new Thread(() -> {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			subText.fadeIn(alpha -> {
				try {
					Thread.sleep(1500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				callback.accept(true);
			});
		}).start();
	}

}
