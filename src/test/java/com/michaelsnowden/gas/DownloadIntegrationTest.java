package com.michaelsnowden.gas;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.testFramework.builders.JavaModuleFixtureBuilder;
import com.intellij.testFramework.fixtures.*;

/**
 * @author michael.snowden
 */
public class DownloadIntegrationTest extends LightCodeInsightFixtureTestCase {
    public void testDownload() throws Exception {
        final TestFixtureBuilder<IdeaProjectTestFixture> projectBuilder = IdeaTestFixtureFactory.getFixtureFactory()
                .createFixtureBuilder(getName());

        // repeat the following line for each module
        projectBuilder.addModule(JavaModuleFixtureBuilder.class);

        myFixture = JavaTestFixtureFactory.getFixtureFactory().createCodeInsightFixture(projectBuilder.getFixture());
        setUp();
        final com.google.api.services.drive.model.File newProject = CreateProject.createProject();
        final String projectId = newProject.getId();
        Download.download(getProject(), projectId, getProject
                ().getBaseDir());
        final VirtualFile[] children = getProject().getBaseDir().getChildren();
        assertNotNull(children);
        assertEquals(children.length, 1);
        assertEquals(children[0].getName(), "Code.gs");
    }
}