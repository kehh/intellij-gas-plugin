package com.michaelsnowden.gas_plugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

import java.io.IOException;
import java.util.Properties;

/**
 * @author michael.snowden
 */
public class GscriptsDownload extends AnAction {
    public void actionPerformed(AnActionEvent e) {
        Properties properties = new Properties();
        try {
            properties.load(GscriptPrinter.class.getResourceAsStream("/config.properties"));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        try {
            GscriptPrinter.printGscript(properties.getProperty("exampleScriptFileId"));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}
