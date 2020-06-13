package me.deftware.installer.logic.jsonbuilder.launchers.vanilla;

import com.google.gson.*;
import me.deftware.installer.logic.InstallerAPI;
import me.deftware.installer.logic.jsonbuilder.AbstractJsonBuilder;
import me.deftware.installer.logic.utils.VersionData;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.nio.file.Files;

/**
 * Minecraft versions 1.14 through 1.15.2
 * @author Deftware
 */
public class SubsystemJsonBuilder extends AbstractJsonBuilder {

	@Override
	public JsonObject build(VersionData data) {
		JsonObject json = new JsonObject();
		json.addProperty("inheritsFrom", data.getVersion());
		json.addProperty("id", data.getVersion() + "-Aristois");
		json.addProperty("time", getDate());
		json.addProperty("releaseTime", getDate());
		json.addProperty("type", "release");
		json.addProperty("mainClass", data.getMainClass());
		json.add("libraries", getLibraries(data));
		return json;
	}

	public static JsonArray getLibraries(VersionData data) {
		JsonArray libraries = new JsonArray();
		data.getLibraries().forEach(l -> libraries.add(generateMavenRepo(l.getName(), l.getUrl())));
		// EMC
		libraries.add(generateMavenRepo("me.deftware:" + data.getEmc(), "https://gitlab.com/EMC-Framework/maven/raw/master/"));
		libraries.add(generateMavenRepo("me.deftware:subsystem:" + data.getSubsystem(), "https://gitlab.com/EMC-Framework/maven/raw/master/"));
		// Fabric
		libraries.add(generateMavenRepo("net.fabricmc:sponge-mixin:" + data.getMixin(), "https://maven.fabricmc.net/"));
		libraries.add(generateMavenRepo("net.fabricmc:tiny-remapper:0.2.0.52", "https://maven.fabricmc.net/"));
		libraries.add(generateMavenRepo("net.fabricmc:tiny-mappings-parser:0.2.0.11", "https://maven.fabricmc.net/"));
		libraries.add(generateMavenRepo("net.fabricmc:fabric-loader-sat4j:2.3.5.4", "https://maven.fabricmc.net/"));
		libraries.add(generateMavenRepo("com.google.jimfs:jimfs:1.1", "https://maven.fabricmc.net/"));
		libraries.add(generateMavenRepo("org.ow2.asm:asm:8.0", "https://maven.fabricmc.net/"));
		libraries.add(generateMavenRepo("org.ow2.asm:asm-analysis:8.0", "https://maven.fabricmc.net/"));
		libraries.add(generateMavenRepo("org.ow2.asm:asm-commons:8.0", "https://maven.fabricmc.net/"));
		libraries.add(generateMavenRepo("org.ow2.asm:asm-tree:8.0", "https://maven.fabricmc.net/"));
		libraries.add(generateMavenRepo("org.ow2.asm:asm-util:8.0", "https://maven.fabricmc.net/"));
		libraries.add(generateMavenRepo("com.google.guava:guava:21.0", "https://maven.fabricmc.net/"));
		// Mappings
		libraries.add(generateMavenRepo("net.fabricmc:yarn:" + data.getMappings(), "https://maven.fabricmc.net/"));
		return libraries;
	}

	@Override
	public String install(JsonObject json, VersionData data, String rootDir) {
		File parent = new File(rootDir + "versions" + File.separator + data.getVersion() + "-Aristois" + File.separator), jsonFile = new File(parent.getAbsolutePath() + File.separator + data.getVersion() + "-Aristois.json");
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
		return "Aristois has been installed, restart your Minecraft launcher and select \"release " + data.getVersion() + "-Aristois\" and hit play";
	}

	@Override
	public void addLauncherProfile(VersionData data, String rootDir) {
		JsonObject json = new JsonObject();
		json.addProperty("name", "Aristois " + data.getVersion());
		json.addProperty("type", "custom");
		json.addProperty("created", getDate("ms"));
		json.addProperty("lastUsed", getDate("ms"));
		json.addProperty("icon", "Diamond_Block");
		json.addProperty("lastVersionId", data.getVersion() + "-Aristois");
		File profiles_json = new File(rootDir + "launcher_profiles.json");
		try {
			if (profiles_json.exists()) {
				JsonObject launcherJson = JsonParser.parseReader(Files.newBufferedReader(profiles_json.toPath())).getAsJsonObject();
				JsonObject profiles = launcherJson.get("profiles").getAsJsonObject();
				if (profiles.has("Aristois " + data.getVersion())) {
					profiles.remove("Aristois " + data.getVersion());
				}
				profiles.add("Aristois " + data.getVersion(), json);
				launcherJson.addProperty("selectedProfile", "Aristois " + data.getVersion());
				try (Writer writer = new FileWriter(profiles_json)) {
					new GsonBuilder().setPrettyPrinting().create().toJson(launcherJson, writer);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
