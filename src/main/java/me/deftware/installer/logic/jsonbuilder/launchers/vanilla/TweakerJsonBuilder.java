package me.deftware.installer.logic.jsonbuilder.launchers.vanilla;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import me.deftware.installer.logic.utils.VersionData;

/**
 * Minecraft versions 1.13 through 1.13.2
 * @author Deftware
 */
public class TweakerJsonBuilder extends SubsystemJsonBuilder {

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
		JsonObject arguments = new JsonObject();
		JsonArray game = new JsonArray();
		game.add("--tweakClass");
		game.add(data.getTweaker());
		arguments.add("game", game);
		json.add("arguments", arguments);
		return json;
	}

	public static JsonArray getLibraries(VersionData data) {
		JsonArray libraries = new JsonArray();
		data.getLibraries().forEach(l -> libraries.add(generateMavenRepo(l.getName(), l.getUrl())));
		// EMC
		libraries.add(generateMavenRepo("me.deftware:" + data.getEmc(), "https://gitlab.com/EMC-Framework/maven/raw/master/"));
		// Libraries
		libraries.add(generateMavenRepo("net.minecraft:launchwrapper:1.12"));
		libraries.add(generateMavenRepo("org.dimdev:mixin:0.7.11-SNAPSHOT", "https://gitlab.com/EMC-Framework/maven/raw/master/"));
		libraries.add(generateMavenRepo("net.jodah:typetools:0.5.0", "https://repo.maven.apache.org/maven2/"));
		libraries.add(generateMavenRepo("org.ow2.asm:asm-all:5.2", "https://repo1.maven.org/maven2/"));
		return libraries;
	}

}
