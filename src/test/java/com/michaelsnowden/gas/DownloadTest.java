package com.michaelsnowden.gas;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.testFramework.builders.JavaModuleFixtureBuilder;
import com.intellij.testFramework.fixtures.*;

/**
 * @author michael.snowden
 */
public class DownloadTest extends LightCodeInsightFixtureTestCase {
    public void testDownload() throws Exception {
        final TestFixtureBuilder<IdeaProjectTestFixture> projectBuilder = IdeaTestFixtureFactory.getFixtureFactory()
                .createFixtureBuilder(getName());

        // repeat the following line for each module
        projectBuilder.addModule(JavaModuleFixtureBuilder.class);

        myFixture = JavaTestFixtureFactory.getFixtureFactory().createCodeInsightFixture(projectBuilder.getFixture());
        setUp();
        Download.download(getProject(), "1crSY1vSic8NYvCosnGvgCs60oVJDpvka-EEiBi3hQnBEq29xNDcQ58Gb", getProject
                ().getBaseDir());
        final VirtualFile[] children = getProject().getBaseDir().getChildren();
        assertNotNull(children);
        assertEquals(children.length, 1);
        assertEquals(children[0].getName(), "Code.gs");
    }
}