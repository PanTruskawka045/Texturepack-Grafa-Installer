package me.deftware.installer.logic.jsonbuilder.launchers.custom;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.deftware.installer.logic.InstallerAPI;
import me.deftware.installer.logic.jsonbuilder.launchers.vanilla.SubsystemJsonBuilder;
import me.deftware.installer.logic.jsonbuilder.launchers.vanilla.TweakerJsonBuilder;
import me.deftware.installer.logic.jsonbuilder.launchers.vanilla.TweakerJsonBuilderLegacy;
import me.deftware.installer.logic.utils.VersionData;
import me.deftware.installer.logic.jsonbuilder.AbstractJsonBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.nio.file.Files;

/**
 * @author Deftware
 */
public class MultiMcJsonBuilder extends AbstractJsonBuilder {

	@Override
	public JsonObject build(VersionData data) {
		JsonObject json = new JsonObject(), requiresData = new JsonObject();
		json.addProperty("mainClass", data.getMainClass());
		json.addProperty("name", "Aristois" + (InstallerAPI.isDonorBuild() ? " Donor" : ""));
		json.addProperty("releaseTime", getDate());
		// Requires data
		JsonArray requiresArray = new JsonArray();
		requiresData.addProperty("equals", data.getVersion());
		requiresData.addProperty("uid", "net.minecraft");
		requiresArray.add(requiresData);
		json.add("requires", requiresArray);
		json.addProperty("uid", "me.deftware");
		json.addProperty("version", data.getEmc().split(":")[1]);
		json.addProperty("formatVersion", 1);
		// Version specific data
		if (data.getTweaker() != null) {
			// Tweaker based
			JsonArray tweakerArray = new JsonArray();
			tweakerArray.add(data.getTweaker());
			json.add("+tweakers", tweakerArray);
			if (data.getVersion().equalsIgnoreCase("1.12.2")) {
				json.add("+libraries", TweakerJsonBuilderLegacy.getLibraries(data));
			} else {
				json.add("+libraries", TweakerJsonBuilder.getLibraries(data));
			}
		} else {
			// Subsystem based
			json.add("+libraries", SubsystemJsonBuilder.getLibraries(data));
		}
		return json;
	}

	@Override
	public String install(JsonObject json, VersionData data, String rootDir) {
		File parent = new File(rootDir + "patches" + File.separator), jsonFile = new File(parent.getAbsolutePath() + File.separator + "me.deftware.json");
		if (!parent.exists() && !parent.mkdirs()) {
			InstallerAPI.getLogger().error("Failed to create {}", parent.getAbsolutePath());
		}
		if (jsonFile.exists()) {
			if (!jsonFile.delete()) {
				InstallerAPI.getLogger().error("Could not delete {}", jsonFile.getName());
			}
		}
		try (Writer writer = new FileWriter(jsonFile)) {
			new GsonBuilder().setPrettyPrinting().create().toJson(json, writer);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		addLauncherProfile(data, rootDir);
		return "Aristois has been installed, restart your MultiMC launcher and select your instance with Aristois and start playing";
	}

	@Override
	public void addLauncherProfile(VersionData data, String rootDir) {
		JsonObject json = new JsonObject();
		json.addProperty("cachedName", "Aristois " + data.getVersion());
		json.addProperty("cachedVersion", data.getVersion() + "-" + "Aristois");
		json.addProperty("uid", "me.deftware");
		File pack_json = new File(rootDir + "mmc-pack.json");
		try {
			JsonObject packJson = JsonParser.parseReader(Files.newBufferedReader(pack_json.toPath())).getAsJsonObject();
			JsonArray components = packJson.get("components").getAsJsonArray();
			components.add(json);
			try (Writer writer = new FileWriter(pack_json)) {
				new GsonBuilder().setPrettyPrinting().create().toJson(packJson, writer);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
