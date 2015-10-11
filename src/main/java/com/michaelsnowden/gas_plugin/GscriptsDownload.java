package com.michaelsnowden.gas_plugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.impl.file.PsiDirectoryFactory;
import com.michaelsnowden.google_app_scripts_drive.GASFile;
import com.michaelsnowden.google_app_scripts_drive.GASProject;

import java.io.IOException;

/**
 * @author michael.snowden
 */
public class GscriptsDownload extends AnAction {
    interface GscriptImporter {
        void importGscriptProject(GASProject gasProject, AnActionEvent actionEvent);
    }

    static class GscriptImporterImpl1 implements GscriptImporter {
        @Override
        public void importGscriptProject(GASProject gasProject, AnActionEvent actionEvent) {
            String gscript = gasProject.getGasFiles().get(0).getSource();
            Project currentProject = DataKeys.PROJECT.getData(actionEvent.getDataContext());
            VirtualFile currentFile = DataKeys.VIRTUAL_FILE.getData(actionEvent.getDataContext());
            Editor editor = DataKeys.EDITOR.getData(actionEvent.getDataContext());
            FileType js = FileTypeManager.getInstance().getFileTypeByExtension("js");
            PsiFile fileFromText = PsiFileFactory.getInstance(currentProject).createFileFromText("hello.js", js,
                    gscript);
            PsiDirectoryFactory.getInstance(currentProject).createDirectory(currentFile).add(fileFromText);
        }
    }

    static class GscriptImporterImpl2 implements GscriptImporter {
        @Override
        public void importGscriptProject(GASProject gasProject, AnActionEvent actionEvent) {
            Project project = DataKeys.PROJECT.getData(actionEvent.getDataContext());
            PsiDirectory containingDirectory = DataKeys.PSI_FILE.getData(actionEvent.getDataContext())
                    .getContainingDirectory();
            FileType gs = FileTypeManager.getInstance().getFileTypeByExtension("gs");
            for (GASFile gasFile : gasProject.getGasFiles()) {
                String source = gasFile.getSource();
                String name = gasFile.getName() + ".gs";
                PsiFile file = PsiFileFactory.getInstance(project).createFileFromText(name, gs, source);
                containingDirectory.add(file);
            }
        }
    }


    public void actionPerformed(AnActionEvent actionEvent) {
        try {
            String gscript = GscriptPrinter.printGscript("1VYICZ3y5MiF_4oMXvfQ12Mcov33rTR1EF3RNbrR4DhEmdYbsJSRKYR06");
            Project currentProject = DataKeys.PROJECT.getData(actionEvent.getDataContext());
            VirtualFile currentFile = DataKeys.VIRTUAL_FILE.getData(actionEvent.getDataContext());
            Editor editor = DataKeys.EDITOR.getData(actionEvent.getDataContext());
            FileType js = FileTypeManager.getInstance().getFileTypeByExtension("js");
            PsiFile fileFromText = PsiFileFactory.getInstance(currentProject).createFileFromText("hello.js", js,
                    gscript);
            PsiDirectoryFactory.getInstance(currentProject).createDirectory(currentFile).add(fileFromText);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}
