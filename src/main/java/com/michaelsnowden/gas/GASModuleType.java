package com.michaelsnowden.gas;

import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * @author michael.snowden
 */
public class GASModuleType extends ModuleType<GASModuleBuilder> {
    public GASModuleType(@NotNull String id) {
        super(id);
    }

    @NotNull
    @Override
    public GASModuleBuilder createModuleBuilder() {
        return new GASModuleBuilder();
    }

    @NotNull
    @Override
    public String getName() {
        return "Google Apps ScriptModule";
    }

    @NotNull
    @Override
    public String getDescription() {
        return getName();
    }

    @Override
    public Icon getBigIcon() {
        return Messages.getInformationIcon();
    }

    @Override
    public Icon getNodeIcon(@Deprecated boolean b) {
        return Messages.getInformationIcon();
    }
}
