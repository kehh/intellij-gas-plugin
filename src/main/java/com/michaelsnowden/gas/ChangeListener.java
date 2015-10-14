package com.michaelsnowden.gas;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.openapi.vfs.newvfs.BulkFileListener;
import com.intellij.openapi.vfs.newvfs.events.VFileEvent;
import com.intellij.util.messages.MessageBusConnection;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author michael.snowden
 */
public class ChangeListener implements ApplicationComponent, BulkFileListener {
    private final MessageBusConnection connection;

    public ChangeListener() {
        connection = ApplicationManager.getApplication().getMessageBus().connect();
    }

    public void initComponent() {
        connection.subscribe(VirtualFileManager.VFS_CHANGES, this);
    }

    public void disposeComponent() {
        connection.disconnect();
    }

    public void before(List<? extends VFileEvent> events) {
        // ...
//        System.out.println(events);
    }

    public void after(List<? extends VFileEvent> events) {
        // ...
        System.out.println(events);
    }

    @NotNull
    @Override
    public String getComponentName() {
        return "Google Apps Script Project Directory Change Listener";
    }
}