package com.michaelsnowden.gas;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;

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
        VirtualFile chooseFile = project.getBaseDir();
        if (currentFile != null) {
            chooseFile = currentFile.getVirtualFile();
        }
        FileChooserDescriptor descriptor = FileChooserDescriptorFactory.createSingleFolderDescriptor();
        chooseFile = FileChooser.chooseFile(descriptor, project, chooseFile);
        if (chooseFile == null) {
            return;
        }

        try {
            Upload.upload(project, projectId, chooseFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
