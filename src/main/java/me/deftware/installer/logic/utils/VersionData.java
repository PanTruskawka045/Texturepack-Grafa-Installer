package me.deftware.installer.logic.utils;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;

import me.deftware.installer.logic.InstallerAPI;
import me.deftware.installer.logic.jsonbuilder.AbstractJsonBuilder;
import me.deftware.installer.logic.jsonbuilder.launchers.custom.MultiMcJsonBuilder;
import me.deftware.installer.logic.jsonbuilder.launchers.vanilla.SubsystemJsonBuilder;
import me.deftware.installer.logic.jsonbuilder.launchers.vanilla.TweakerJsonBuilder;
import me.deftware.installer.logic.jsonbuilder.launchers.vanilla.TweakerJsonBuilderLegacy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Deftware
 */
public @Data class VersionData {

	private @SerializedName("beta") boolean beta = false;

	private @SerializedName("version") String version;

	private @SerializedName("mixin") String mixin = "0.8+build.16";

	private @SerializedName("emc") String emc;

	private @SerializedName("mappings") String mappings;

	private @SerializedName("tweaker") String tweaker;

	private @SerializedName("subsystem") String subsystem = "0.8.5";

	private @SerializedName("mainClass") String mainClass = "me.deftware.client.framework.main.Main";

	/**
	 * Available launchers currently: vanilla, multimc
	 */
	private @SerializedName("launchers") List<String> launchers = Arrays.asList("Vanilla", "MultiMC");

	/**
	 * Available mod loaders currently: forge (1.12.2 | 1.15.2)
	 */
	private @SerializedName("modLoaders") List<String> modLoaders = new ArrayList<>();

	private @SerializedName("libraries") List<VersionLibrary> libraries = Collections.singletonList(
			new VersionLibrary(String.format("me.deftware:aristois%s:loader%s", InstallerAPI.isDonorBuild() ? "-d" : "", InstallerAPI.isUniversal() ? "-universal" : ""), InstallerAPI.getAristoisMaven())
	);

	@SuppressWarnings("WeakerAccess")
	@Data @AllArgsConstructor public static class VersionLibrary {

		private @SerializedName("name") String name;

		private @SerializedName("url") String url;

	}

	public AbstractJsonBuilder getBuilder(String modLoader, String launcher) {
		if (launcher.equalsIgnoreCase("multimc")) {
			return new MultiMcJsonBuilder();
		} else if (tweaker != null) {
			String versionData = version;
			if (versionData.split("\\.").length == 2) {
				versionData += ".0";
			}
			if (Integer.parseInt(versionData.replaceAll("\\.", "")) <= 1122) {
				return new TweakerJsonBuilderLegacy();
			} else {
				return new TweakerJsonBuilder();
			}
		}
		return new SubsystemJsonBuilder();
	}

}
