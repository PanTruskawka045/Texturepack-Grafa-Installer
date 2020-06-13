package me.deftware.installer.logic.jsonbuilder.launchers.vanilla;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import me.deftware.installer.logic.utils.VersionData;

/**
 * Minecraft version 1.12.2
 * @author Deftware
 */
public class TweakerJsonBuilderLegacy extends TweakerJsonBuilder {

	@Override
	public JsonObject build(VersionData data) {
		JsonObject json = new JsonObject();
		json.addProperty("inheritsFrom", data.getVersion());
		json.addProperty("id", data.getVersion() + "-Aristois");
		json.addProperty("time", getDate());
		json.addProperty("releaseTime", getDate());
		json.addProperty("type", "release");
		json.addProperty("minecraftArguments", "--username ${auth_player_name} " +
				"--version ${version_name} " +
				"--gameDir ${game_directory} " +
				"--assetsDir ${assets_root} " +
				"--assetIndex ${assets_index_name} " +
				"--uuid ${auth_uuid} " +
				"--accessToken ${auth_access_token} " +
				"--userType ${user_type} " +
				"--tweakClass " + data.getTweaker());
		json.addProperty("mainClass", data.getMainClass());
		json.add("libraries", TweakerJsonBuilderLegacy.getLibraries(data));
		return json;
	}

	public static JsonArray getLibraries(VersionData data) {
		JsonArray libraries = TweakerJsonBuilder.getLibraries(data);
		libraries.add(generateMavenRepo("com.mojang:brigadier:1.0.17", "https://libraries.minecraft.net/"));
		return libraries;
	}

}
