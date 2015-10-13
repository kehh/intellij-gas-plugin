package com.michaelsnowden.gas;

import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;

import javax.swing.*;
import java.io.IOException;

/**
 * @author michael.snowden
 */
public class UploadAction extends AnAction {
    public void actionPerformed(AnActionEvent actionEvent) {
        final DataContext dataContext = actionEvent.getDataContext();
        final PsiFile currentFile = DataKeys.PSI_FILE.getData(dataContext);
        com.intellij.openapi.project.Project project = actionEvent.getProject();
        assert project != null;
        final String message = "Upload files to your GAS project";
        final String title = "Input Your GAS Project ID";
        final Icon questionIcon = Messages.getQuestionIcon();
        final String projectId = Messages.showInputDialog(project, message, title, questionIcon);
        if (projectId == null) {
            return;
        }
        final Project gasProject;
        try {
            gasProject = Project.downloadGASProject(DriveFactory.getDriveService(), projectId);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        VirtualFile chooseFile = project.getBaseDir();
        if (currentFile != null) {
            chooseFile = currentFile.getVirtualFile();
        }
        FileChooserDescriptor descriptor = FileChooserDescriptorFactory.createSingleFolderDescriptor();
        PsiManager psiManager = PsiManager.getInstance(project);
        chooseFile = FileChooser.chooseFile(descriptor, project, chooseFile);
        if (chooseFile == null) {
            return;
        }
        final PsiDirectory directory = psiManager.findDirectory(chooseFile);
        ApplicationManager.getApplication().runReadAction(new Runnable() {
            @Override
            public void run() {
                if (directory != null) {
                    final PsiFile[] files = directory.getFiles();
                    for (PsiFile newPsiFile : files) {
                        final String extension = newPsiFile.getVirtualFile().getExtension();
                        if (com.michaelsnowden.gas.FileType.getByExtension(extension) != null) {
                            Drive drive;
                            try {
                                drive = DriveFactory.getDriveService();
                            } catch (IOException e) {
                                e.printStackTrace();
                                return;
                            }
                            try {
                                final String mimeType = "application/vnd.google-apps.script+json";
                                final File gasFile = gasProject.getFileWithName(newPsiFile.getName());

                                if (gasFile != null) {

                                    final com.google.api.services.drive.model.File oldProjectFile = drive.files().get
                                            (projectId).execute();
                                    // File's new content.
                                    java.io.File newFile = new java.io.File(newPsiFile.getVirtualFile().getPath());
                                    FileContent newMediaContent = new FileContent(mimeType, newFile);

                                    // Send the request to the API.
                                    drive.files().update(oldProjectFile.getId(), oldProjectFile, newMediaContent).execute();
                                } else {
                                    System.out.println("Couldn't upload file: " + newPsiFile.getName() + " because it doesn't exist yet");
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                                return;
                            }
                        } else {
                            System.out.println("Skipping " + newPsiFile.getName() + " because only files with the gs " +
                                    "extension are currently supported");
                        }
                    }
                }
            }
        });
        System.out.println("");
    }
}
