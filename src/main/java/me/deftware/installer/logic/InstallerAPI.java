package me.deftware.installer.logic;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.Getter;
import me.deftware.installer.Main;
import me.deftware.installer.logic.utils.Utils;
import me.deftware.installer.logic.utils.VersionData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

public class InstallerAPI {

	private @Getter static boolean donorBuild = false, universal = false;
	private @Getter static HashMap<String, VersionData> versions = new HashMap<>();
	private @Getter static String dataUrl = "https://maven.aristois.net/versions.json", aristoisMaven = "https://maven.aristois.net/";
	private @Getter static Logger logger = LogManager.getLogger("Aristois Installer");
	private @Getter static JsonObject jsonData;

	public static void fetchData(boolean betaVersions) {
		try {
			Attributes attr = new Manifest(((URLClassLoader) Main.class.getClassLoader()).findResource("META-INF/MANIFEST.MF").openStream()).getMainAttributes();
			donorBuild = Boolean.parseBoolean(attr.getValue("donorBuild"));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		try {
			getLogger().info("Fetching data from {}...", dataUrl);
			jsonData = new Gson().fromJson(Utils.get(dataUrl), JsonObject.class);
			populateVersions(betaVersions);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static String[] getMinecraftVersions() {
		String[] data = new String[versions.size()];
		List<String> versions = new ArrayList<>();
		getVersions().keySet().forEach(k -> {
			if (k.split("\\.").length == 2) {
				k += ".0";
			}
			versions.add(k);
		});
		versions.sort(Comparator.comparingInt(value -> Integer.parseInt(value.replaceAll("\\.", ""))));
		Collections.reverse(versions);
		for (int i = 0; i < versions.size(); i++) {
			data[i] = "Minecraft " + versions.get(i).replace(".0", "");
		}
		return data;
	}

	private static void populateVersions(boolean betaVersions) {
		versions.clear();
		Arrays.stream(new Gson().fromJson(jsonData.get("versions").getAsJsonArray(), VersionData[].class)).forEach(v -> {
			if (!v.isBeta() || v.isBeta() && betaVersions) {
				versions.put(v.getVersion(), v);
			}
		});
	}

}
