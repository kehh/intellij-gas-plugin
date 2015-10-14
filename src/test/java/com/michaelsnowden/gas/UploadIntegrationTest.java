package com.michaelsnowden.gas;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.testFramework.builders.JavaModuleFixtureBuilder;
import com.intellij.testFramework.fixtures.*;

import java.io.IOException;

/**
 * @author michael.snowden
 */
public class UploadIntegrationTest extends LightCodeInsightFixtureTestCase {
    public void testUpload() throws Exception {
        final TestFixtureBuilder<IdeaProjectTestFixture> projectBuilder = IdeaTestFixtureFactory.getFixtureFactory()
                .createFixtureBuilder(getName());

        // repeat the following line for each module
        projectBuilder.addModule(JavaModuleFixtureBuilder.class);

        myFixture = JavaTestFixtureFactory.getFixtureFactory().createCodeInsightFixture(projectBuilder.getFixture());
        setUp();
        final VirtualFile baseDir = getProject()
                .getBaseDir();
        final String projectId = "1crSY1vSic8NYvCosnGvgCs60oVJDpvka-EEiBi3hQnBEq29xNDcQ58Gb";
        Download.download(getProject(), projectId, baseDir);
        final PsiDirectory directory = PsiManager.getInstance(getProject()).findDirectory(baseDir);
        com.intellij.openapi.fileTypes.FileType gs = FileTypeManager.getInstance().getFileTypeByExtension("gs");
        final String source = "function moreFunction() { return 42; }";
        final String name = "MoreCode.gs";
        final PsiFile fileFromText = PsiFileFactory.getInstance(getProject()).createFileFromText(name, gs, source);
        assert directory != null;
        ApplicationManager.getApplication().runWriteAction(new Runnable() {
            @Override
            public void run() {
                directory.add(fileFromText);
                try {
                    Upload.upload(getProject(), projectId, baseDir);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                for (PsiElement psiElement : directory.getChildren()) {
                    psiElement.delete();
                }
                assertEquals(directory.getChildren().length, 0);
                Download.download(getProject(), projectId, baseDir);

                final PsiElement[] children = directory.getChildren();
                assertNotNull(children);
                assertEquals(children.length, 2);

                for (PsiElement psiElement : directory.getChildren()) {
                    if (psiElement.getContainingFile().getName().equals(name)) {
                        psiElement.delete();
                    }
                }

                try {
                    Upload.upload(getProject(), projectId, directory.getVirtualFile());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
