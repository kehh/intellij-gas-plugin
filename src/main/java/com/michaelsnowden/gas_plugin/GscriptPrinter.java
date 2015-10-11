package com.michaelsnowden.gas_plugin;

import com.google.api.services.drive.Drive;
import com.michaelsnowden.google_app_scripts_drive.GASProject;

import java.io.IOException;

/**
 * @author michael.snowden
 */
public class GscriptPrinter {
    public static void printGscript(String projectId) throws IOException {
        Drive driveService = DriveQuickstart.getDriveService();
        GASProject gasProject = GASProject.downloadGASProject(driveService, projectId);
        System.out.println(gasProject);
    }
}
