package com.michaelsnowden.gas;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;

import java.io.IOException;

/**
 * @author michael.snowden
 */
public class Download {
    public static void download(com.intellij.openapi.project.Project project, String projectId, VirtualFile
            chooseFile) {
        final PsiDirectory directory = PsiManager.getInstance(project).findDirectory(chooseFile);

        LocalGASProject gasProject;
        try {
            gasProject = LocalGASProject.downloadGASProject(DriveFactory.getDriveService(), projectId);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        FileType gs = FileTypeManager.getInstance().getFileTypeByExtension("gs");
        for (final LocalGASFile gasFile : gasProject.getFiles()) {
            String source = gasFile.getSource();
            String name = gasFile.getName() + ".gs";
            final PsiFile file = PsiFileFactory.getInstance(project).createFileFromText(name, gs, source);

            ApplicationManager.getApplication().runWriteAction(new Runnable() {
                @Override
                public void run() {
                    if (directory != null) {
                        for (PsiElement psiElement : directory.getChildren()) {
                            if (file.getName().equals(psiElement.getContainingFile().getName())) {
                                psiElement.delete();
                            }
                        }

                        directory.add(file);
                    }
                }
            });
        }
        System.out.println("");
    }
}
