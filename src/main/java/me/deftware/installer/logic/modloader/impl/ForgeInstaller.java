package me.deftware.installer.logic.modloader.impl;

import me.deftware.installer.logic.InstallerAPI;
import me.deftware.installer.logic.modloader.ModLoaderInstaller;
import me.deftware.installer.logic.utils.Utils;
import me.deftware.installer.logic.utils.VersionData;

import java.io.File;

/**
 * @author Deftware
 */
public class ForgeInstaller extends ModLoaderInstaller {

	@Override
	public String install(VersionData data, String rootDir) {
		File modsDir = new File(rootDir + "mods" + File.separator + data.getVersion() + File.separator), emc = new File(modsDir.getAbsolutePath() + File.separator + "EMC.jar");
		if (!modsDir.exists() && !modsDir.mkdirs()) {
			InstallerAPI.getLogger().error("Could not create {}", modsDir.getAbsolutePath());
		}
		File emcDir = new File(rootDir + "libraries" + File.separator + "EMC" + File.separator + data.getVersion() + File.separator), aristois = new File(emcDir.getAbsolutePath() + File.separator + "Aristois.jar");
		if (!emcDir.exists() && !emcDir.mkdirs()) {
			InstallerAPI.getLogger().error("Could not create {}", emcDir.getAbsolutePath());
		}
		try {
			// EMC
			Utils.download(Utils.getMavenUrl(String.format("me.deftware:%s-Forge:%s", data.getEmc().split(":")[0], data.getEmc().split(":")[1]), "https://gitlab.com/EMC-Framework/maven/raw/master/"), emc);
			// Aristois
			Utils.download(Utils.getMavenUrl(String.format("me.deftware:aristois%s:latest-%s", InstallerAPI.isDonorBuild() ? "-d" : "", data.getVersion()), "https://maven.aristois.net/"), aristois);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "Aristois for Forge has been installed, start your Forge instance and play";
	}

}
