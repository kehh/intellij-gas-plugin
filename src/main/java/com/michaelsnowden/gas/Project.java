package com.michaelsnowden.gas;

import com.google.api.client.http.GenericUrl;
import com.google.api.services.drive.Drive;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author michael.snowden
 */
public class Project {
    private final List<File> files;

    public Project(List<File> files) {
        this.files = files;
    }

    public List<File> getFiles() {
        return files;
    }

    public File getFileWithName(String name) {
        for (File file : files) {
            if ((file.getName() + ".gs").equals(name)) {
                return file;
            }
        }
        return null;
    }

    public static Project downloadGASProject(Drive drive, String projectId) throws IOException {
        InputStream content = drive.getRequestFactory().buildGetRequest(new GenericUrl(drive.files().get(projectId)
                .execute().getExportLinks().get("application/vnd.google-apps.script+json"))).execute().getContent();
        java.util.Scanner s = new java.util.Scanner(content).useDelimiter("\\A");
        String jsonString = s.hasNext() ? s.next() : "";
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = (JsonObject) jsonParser.parse(jsonString);
        JsonArray files = jsonObject.getAsJsonArray("files");
        List<File> gasFiles = new ArrayList<File>();
        for (JsonElement jsonElement : files) {
            gasFiles.add(new File(jsonElement.getAsJsonObject()));
        }
        return new Project(gasFiles);
    }
}