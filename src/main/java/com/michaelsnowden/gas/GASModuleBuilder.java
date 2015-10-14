package com.michaelsnowden.gas;

import com.intellij.ide.util.projectWizard.*;
import com.intellij.openapi.module.JavaModuleType;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.module.StdModuleTypes;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.JavaSdkType;
import com.intellij.openapi.projectRoots.SdkTypeId;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.ui.configuration.ModulesProvider;
import com.intellij.openapi.util.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author michael.snowden
 */
public class GASModuleBuilder extends ModuleBuilder implements SourcePathsBuilder {
    private List<Pair<String, String>> sourcePaths;
    private Project project;

    @Override
    public void setupRootModel(ModifiableRootModel modifiableRootModel) throws ConfigurationException {
        project = modifiableRootModel.getProject();
    }

    @Override
    public ModuleType getModuleType() {
        return StdModuleTypes.JAVA;
    }

    @Override
    public List<Pair<String, String>> getSourcePaths() throws ConfigurationException {
        return sourcePaths;
    }

    @Override
    public void setSourcePaths(List<Pair<String, String>> list) {
        sourcePaths = list;
    }

    @Override
    public void addSourcePath(Pair<String, String> pair) {
        sourcePaths.add(pair);
    }



    public Project getProject() {
        return project;
    }


    @Override
    public String getBuilderId() {
        return getClass().getName();
    }

    @Override
    public String getPresentableName() {
        return "Google Apps Script Module";
    }


    @Override
    public ModuleWizardStep[] createWizardSteps(@NotNull WizardContext wizardContext, @NotNull ModulesProvider modulesProvider) {
        return new ModuleWizardStep[]{};
    }

    @Override
    public String getGroupName() {
        return JavaModuleType.JAVA_GROUP;
    }

    @Override
    public boolean isSuitableSdkType(SdkTypeId sdkType) {
        return sdkType instanceof JavaSdkType;
    }

    @Nullable
    @Override
    public ModuleWizardStep modifySettingsStep(@NotNull SettingsStep settingsStep) {
        return StdModuleTypes.JAVA.modifySettingsStep(settingsStep, this);
    }
}
