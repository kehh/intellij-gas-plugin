package com.michaelsnowden.gas;

import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;

import java.io.IOException;

/**
 * @author michael.snowden
 */
public class CreateProjectTest extends LightCodeInsightFixtureTestCase {
    public void testCreateProject() throws IOException {
        final com.google.api.services.drive.model.File project = CreateProject.createProject();
        final String title = project.getTitle();
        assertEquals(title, "Untitled");
    }
}
