package com.michaelsnowden.gas;

import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.util.projectWizard.WizardContext;

import javax.swing.*;

/**
 * @author michael.snowden
 */
public class GASModuleWizardStep extends ModuleWizardStep {
    private final GASModuleBuilder moduleBuilder;
    private final WizardContext wizardContext;

    public GASModuleWizardStep(GASModuleBuilder moduleBuilder, WizardContext wizardContext) {
        this.moduleBuilder = moduleBuilder;
        this.wizardContext = wizardContext;
    }

    @Override
    public JComponent getComponent() {
        return new JPanel();
    }

    @Override
    public void updateDataModel() {

    }
}
