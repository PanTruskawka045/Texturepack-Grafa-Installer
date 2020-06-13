package me.deftware.installer.logic.modloader;

import me.deftware.installer.logic.utils.VersionData;

/**
 * @author Deftware
 */
public abstract class ModLoaderInstaller {

	public abstract String install(VersionData data, String rootDir);

}
