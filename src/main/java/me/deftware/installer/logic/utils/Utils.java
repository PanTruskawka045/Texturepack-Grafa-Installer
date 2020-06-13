package me.deftware.installer.logic.utils;

import me.deftware.installer.logic.InstallerAPI;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author Deftware
 */
public class Utils {

    public static void download(String uri, File fileInstance) throws Exception {
        if (!fileInstance.getParentFile().exists() && !fileInstance.getParentFile().mkdirs()) {
            InstallerAPI.getLogger().error("Error: Unable to Create File Parent Directories {}, things could go wrong if true...", fileInstance.getParentFile().getAbsolutePath());
        }
        if (fileInstance.exists() && !fileInstance.delete()) {
            InstallerAPI.getLogger().error("Could not delete {}", fileInstance.getName());
        }
        URL url = new URL(uri);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36");
        connection.setRequestMethod("GET");
        FileOutputStream out = new FileOutputStream(fileInstance);
        InputStream in = connection.getInputStream();
        int read;
        byte[] buffer = new byte[4096];
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
        in.close();
        out.close();
    }

    public static String get(String url) throws Exception {
        URL url1 = new URL(url);
        Object connection = (url.startsWith("https://") ? (HttpsURLConnection) url1.openConnection()
                : (HttpURLConnection) url1.openConnection());
        ((URLConnection) connection).setConnectTimeout(8 * 1000);
        ((URLConnection) connection).setRequestProperty("User-Agent", "EMC Installer");
        ((HttpURLConnection) connection).setRequestMethod("GET");
        BufferedReader in = new BufferedReader(new InputStreamReader(((URLConnection) connection).getInputStream()));
        StringBuilder result = new StringBuilder();
        String text;
        while ((text = in.readLine()) != null) {
            result.append(text);
        }
        in.close();
        return result.toString();
    }

    public static String getMavenUrl(String name, String url) {
        String[] data = name.split(":");
        String type = data.length > 3 ? data[3] : "";
        return String.format("%s%s/%s/%s/%s-%s%s.jar", url, data[0].replaceAll("\\.", "/"), data[1], data[2], data[1], data[2], type);
    }

}
