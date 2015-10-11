package com.michaelsnowden.gas_plugin;

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
public class GASProject {
    private final List<GASFile> gasFiles;

    public GASProject(List<GASFile> gasFiles) {
        this.gasFiles = gasFiles;
    }

    public List<GASFile> getGasFiles() {
        return gasFiles;
    }

    public static GASProject downloadGASProject(Drive drive, String projectId) throws IOException {
        InputStream content = drive.getRequestFactory().buildGetRequest(new GenericUrl(drive.files().get(projectId)
                .execute().getExportLinks().get("application/vnd.google-apps.script+json"))).execute().getContent();
        java.util.Scanner s = new java.util.Scanner(content).useDelimiter("\\A");
        String jsonString = s.hasNext() ? s.next() : "";
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = (JsonObject) jsonParser.parse(jsonString);
        JsonArray files = jsonObject.getAsJsonArray("files");
        List<GASFile> gasFiles = new ArrayList<GASFile>();
        for (JsonElement jsonElement : files) {
            gasFiles.add(new GASFile(jsonElement.getAsJsonObject()));
        }
        return new GASProject(gasFiles);
    }
}