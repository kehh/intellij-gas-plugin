package com.michaelsnowden.gas;

import com.intellij.ide.util.projectWizard.ModuleBuilder;
import com.intellij.ide.util.projectWizard.SourcePathsBuilder;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.module.StdModuleTypes;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.util.Pair;

import java.util.List;

/**
 * @author michael.snowden
 */
public class GASModuleBuilder extends ModuleBuilder implements SourcePathsBuilder {
    private List<Pair<String, String>> sourcePaths;

    @Override
    public void setupRootModel(ModifiableRootModel modifiableRootModel) throws ConfigurationException {

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
}
