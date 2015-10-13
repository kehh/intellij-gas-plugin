package com.michaelsnowden.gas;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.PsiManager;

import java.io.IOException;

/**
 * @author michael.snowden
 */
public class Download {
    public static void download(com.intellij.openapi.project.Project project, String projectId, VirtualFile chooseFile) {
        final PsiDirectory directory = PsiManager.getInstance(project).findDirectory(chooseFile);

        Project gasProject = null;
        try {
            gasProject = Project.downloadGASProject(DriveFactory.getDriveService(), projectId);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        FileType gs = FileTypeManager.getInstance().getFileTypeByExtension("gs");
        for (File gasFile : gasProject.getFiles()) {
            String source = gasFile.getSource();
            String name = gasFile.getName() + ".gs";
            final PsiFile file = PsiFileFactory.getInstance(project).createFileFromText(name, gs, source);

            ApplicationManager.getApplication().runWriteAction(new Runnable() {
                @Override
                public void run() {
                    if (directory != null) {
                        directory.add(file);
                    }
                }
            });
        }
        System.out.println("");
    }
}
