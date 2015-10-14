package com.michaelsnowden.gas;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;

import java.io.IOException;

/**
 * @author michael.snowden
 */
public class CreateProjectAction extends AnAction {
    public void actionPerformed(AnActionEvent e) {
        try {
            final com.google.api.services.drive.model.File project = CreateProject.createProject();
            final String projectId = project.getId();
            Messages.showMessageDialog(e.getProject(), projectId, "Your" +
                    " New Project ID", Messages.getInformationIcon());
        } catch (IOException e1) {
            Messages.showErrorDialog(e.getProject(), "Could not create project", e1.getMessage());
            e1.printStackTrace();
        }
    }
}
