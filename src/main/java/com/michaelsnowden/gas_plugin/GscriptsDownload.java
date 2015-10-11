package com.michaelsnowden.gas_plugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.PsiManager;
import com.intellij.psi.impl.file.PsiDirectoryFactory;

import java.io.IOException;

/**
 * @author michael.snowden
 */
public class GscriptsDownload extends AnAction {
    public void actionPerformed(AnActionEvent actionEvent) {
        try {
            String projectId = "1VYICZ3y5MiF_4oMXvfQ12Mcov33rTR1EF3RNbrR4DhEmdYbsJSRKYR06";
            GASProject gasProject = GASProject.downloadGASProject(DriveQuickstart.getDriveService(),
                    projectId);
            new PresentChooser().importGscriptProject(gasProject, actionEvent);
            System.out.println("");
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

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

    static class PresentChooser implements GscriptImporter {
        @Override
        public void importGscriptProject(GASProject gasProject, AnActionEvent actionEvent) {
            Project project = actionEvent.getProject();
            assert project != null;



            VirtualFile chooseFile = project.getBaseDir();
            PsiFile currentFile = DataKeys.PSI_FILE.getData(actionEvent.getDataContext());
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

            FileType gs = FileTypeManager.getInstance().getFileTypeByExtension("gs");
            for (GASFile gasFile : gasProject.getGasFiles()) {
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
        }
    }
}
