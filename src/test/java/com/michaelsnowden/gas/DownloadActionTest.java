package com.michaelsnowden.gas;

import com.intellij.testFramework.builders.JavaModuleFixtureBuilder;
import com.intellij.testFramework.fixtures.*;

/**
 * @author michael.snowden
 */
public class DownloadActionTest extends LightCodeInsightFixtureTestCase {
    public void testDownload() throws Exception {
        final TestFixtureBuilder<IdeaProjectTestFixture> projectBuilder = IdeaTestFixtureFactory.getFixtureFactory()
                .createFixtureBuilder(getName());

        // repeat the following line for each module
        projectBuilder.addModule(JavaModuleFixtureBuilder.class);

        myFixture = JavaTestFixtureFactory.getFixtureFactory().createCodeInsightFixture(projectBuilder.getFixture());
        setUp();
        DownloadAction.download(getProject(), "1crSY1vSic8NYvCosnGvgCs60oVJDpvka-EEiBi3hQnBEq29xNDcQ58Gb", getProject
                ().getBaseDir());
        System.out.println("");
    }
}