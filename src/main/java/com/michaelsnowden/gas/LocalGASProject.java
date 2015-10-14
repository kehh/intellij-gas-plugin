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
public class LocalGASProject {
    private final List<LocalGASFile> files;
    private final Integer id;

    public LocalGASProject(List<LocalGASFile> files) {
        this(files, null);
    }

    public LocalGASProject(List<LocalGASFile> files, Integer id) {
        this.files = files;
        this.id = id;
    }

    public List<LocalGASFile> getFiles() {
        return files;
    }

    public LocalGASFile getFileWithName(String name) {
        for (LocalGASFile file : files) {
            if ((file.getName() + ".gs").equals(name)) {
                return file;
            }
        }
        return null;
    }

    public static LocalGASProject downloadGASProject(Drive drive, String projectId) throws IOException {
        InputStream content = drive.getRequestFactory().buildGetRequest(new GenericUrl(drive.files().get(projectId)
                .execute().getExportLinks().get("application/vnd.google-apps.script+json"))).execute().getContent();
        java.util.Scanner s = new java.util.Scanner(content).useDelimiter("\\A");
        String jsonString = s.hasNext() ? s.next() : "";
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = (JsonObject) jsonParser.parse(jsonString);
        JsonArray files = jsonObject.getAsJsonArray("files");
        List<LocalGASFile> gasFiles = new ArrayList<LocalGASFile>();
        for (JsonElement jsonElement : files) {
            gasFiles.add(new LocalGASFile(jsonElement.getAsJsonObject()));
        }
        return new LocalGASProject(gasFiles);
    }
}