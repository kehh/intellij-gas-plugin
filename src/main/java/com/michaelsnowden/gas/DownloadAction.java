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

/**
 * @author michael.snowden
 */
public class DownloadAction extends AnAction {
    public void actionPerformed(AnActionEvent actionEvent) {
        com.intellij.openapi.project.Project project = actionEvent.getProject();
        final DataContext dataContext = actionEvent.getDataContext();
        final PsiFile currentFile = DataKeys.PSI_FILE.getData(dataContext);
        assert project != null;
        final String message = "Download your GAS project";
        final String title = "Input Your GAS Project ID";
        final Icon questionIcon = Messages.getQuestionIcon();
        final String projectId = Messages.showInputDialog(project, message, title, questionIcon);
        VirtualFile chooseFile = project.getBaseDir();
        if (currentFile != null) {
            chooseFile = currentFile.getVirtualFile();
        }
        FileChooserDescriptor descriptor = FileChooserDescriptorFactory.createSingleFolderDescriptor();
        chooseFile = FileChooser.chooseFile(descriptor, project, chooseFile);
        if (chooseFile == null) {
            return;
        }
        Download.download(project, projectId, chooseFile);
    }
}
