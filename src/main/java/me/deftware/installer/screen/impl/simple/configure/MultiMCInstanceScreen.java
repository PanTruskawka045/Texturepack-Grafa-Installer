package me.deftware.installer.screen.impl.simple.configure;

import me.deftware.aristois.installer.utils.VersionData;
import me.deftware.installer.Main;
import me.deftware.installer.screen.AbstractScreen;
import me.deftware.installer.screen.components.ButtonComponent;
import me.deftware.installer.screen.components.ComboBoxComponent;
import me.deftware.installer.screen.components.TextComponent;
import me.deftware.installer.screen.impl.simple.InstallingScreen;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Deftware
 */
public class MultiMCInstanceScreen extends AbstractScreen {


	private VersionData version;
	private String path, launcher;

	public MultiMCInstanceScreen(VersionData version, String path, String launcher) {
		this.version = version;
		this.path = path;
		this.launcher = launcher;
	}

	@Override
	public void init() {
		componentList.clear();

		List<String> instanceList = new ArrayList<>();
		for (String instance : Objects.requireNonNull(new File(path + File.separator + "instances" + File.separator).list((current, name) -> new File(current, name).isDirectory()))) {
			if (instance.toLowerCase().contains("temp")) {
				continue;
			}
			instanceList.add(instance);
		}

		ComboBoxComponent instancesBox = new ComboBoxComponent(0, 200, 600, 30,  instanceList.toArray(new String[instanceList.size()]));
		instancesBox.centerHorizontally();

		addComponent(instancesBox, new TextComponent(0, 65,  40, "MultiMC instance").centerHorizontally(),
				new TextComponent(0, 130,  25, "Select which MultiMC instance you would like to use Aristois with:").centerHorizontally(),
				new TextComponent(0, 300,  25, "The instance must be Minecraft version " + version.getVersion()).centerHorizontally(),
				new ButtonComponent(50, 400, 100, 50, "Continue", mouseButton -> {
					Main.getWindow().transitionForward(new InstallingScreen(version, path + File.separator + "instances" + File.separator + instancesBox.getSelectedItem() + File.separator, launcher));
				}).centerHorizontally());
	}

}

