package com.michaelsnowden.gas;

import com.google.api.client.http.ByteArrayContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;

import java.io.IOException;

/**
 * @author michael.snowden
 */
public class CreateProject {
    public static File createProject() throws IOException {
        final Drive drive = DriveFactory.getDriveService();
        final File file = new File();
        file.setMimeType("application/vnd.google-apps.script+json");
        final ByteArrayContent content = new ByteArrayContent("application/vnd.google-apps.script+json", ("{\n" +
                "  \"files\": [\n" +
                "    {\n" +
                "      \"name\":\"Code\",\n" +
                "      \"type\":\"server_js\",\n" +
                "      \"source\":\"\"\n" +
                "    }\n" +
                "  ]\n" +
                "}").getBytes());
        return drive.files().insert(file, content).execute();
    }
}
