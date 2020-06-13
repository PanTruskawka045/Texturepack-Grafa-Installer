package me.deftware.installer.logic.jsonbuilder;

import com.google.gson.JsonObject;
import me.deftware.installer.logic.utils.VersionData;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Deftware
 */
public abstract class AbstractJsonBuilder {

    protected static String getDate(String... args) {
        if (args != null && args.length != 0) {
            String arguments = Arrays.toString(args).toLowerCase();
            if (arguments.contains("ms")) {
                return new SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ss.SSS'Z'").format(new Date());
            }
        }
        try {
            return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").format(new Date());
        } catch (Exception e) {
            return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(new Date());
        }
    }

    public abstract JsonObject build(VersionData data);

    public abstract String install(JsonObject json, VersionData data, String rootDir);

    public abstract void addLauncherProfile(VersionData data, String rootDir);

    public static JsonObject generateMavenRepo(String... data) {
        JsonObject obj = new JsonObject();
        obj.addProperty("name", data[0]);
        if (data.length > 1) {
            obj.addProperty("url", data[1]);
        }
        return obj;
    }

}
