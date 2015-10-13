package com.michaelsnowden.gas;

import com.google.api.client.http.ByteArrayContent;
import com.google.api.services.drive.Drive;
import com.google.common.io.Files;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author michael.snowden
 */
public class Upload {
    static void upload(com.intellij.openapi.project.Project project, final String projectId, VirtualFile
            chooseFile) throws IOException {
        final Project gasProject;
        final PsiDirectory directory = PsiManager.getInstance(project).findDirectory(chooseFile);
        gasProject = Project.downloadGASProject(DriveFactory.getDriveService(), projectId);
        final Drive drive;
        drive = DriveFactory.getDriveService();
        ApplicationManager.getApplication().runReadAction(new Runnable() {
            @Override
            public void run() {
                if (directory == null) {
                    return;
                }
                final PsiFile[] files = directory.getFiles();
                com.google.api.services.drive.model.File oldProjectFile = null;
                try {
                    oldProjectFile = drive.files().get
                            (projectId).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                JsonObject jsonObject = new JsonObject();
                JsonArray jsonArray = new JsonArray();
                for (PsiFile newPsiFile : files) {
                    final String extension = newPsiFile.getVirtualFile().getExtension();
                    final FileType byExtension = FileType.getByExtension(extension);
                    if (byExtension != null) {
                        final File gasFile = gasProject.getFileWithName(newPsiFile.getName());

                        final VirtualFile newPsiFileVirtualFile = newPsiFile.getVirtualFile();
                        java.io.File newFile = new java.io.File(newPsiFileVirtualFile.getPath());
                        JsonObject scriptFile = new JsonObject();
                        if (gasFile != null) {
                            scriptFile.addProperty("id", gasFile.getId());
                        }
                        // File's new content.
                        scriptFile.addProperty("name", newPsiFileVirtualFile.getName());
                        scriptFile.addProperty("type", byExtension.getType());
                        try {
                            scriptFile.addProperty("source", Files.toString(newFile, StandardCharsets.UTF_8));
                        } catch (IOException e) {
                            e.printStackTrace();
                            continue;
                        }
                        jsonArray.add(scriptFile);
                        // Send the request to the API.
                    } else {
                        System.out.println("Skipping " + newPsiFile.getName() + " because only files with the gs " +
                                "extension are currently supported");
                    }
                }
                jsonObject.add("files", jsonArray);
                ByteArrayContent content = new ByteArrayContent("application/json", jsonObject.toString().getBytes());
                try {
                    drive.files().update(projectId, oldProjectFile, content).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
