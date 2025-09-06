package com.plugin.Service;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;

import java.util.List;
import java.util.Map;

public interface CreateEntityInterface {
    public VirtualFile findFile(Project project, String filename);
    public Map<String, String> getDbParameters(VirtualFile dbFile);
    public ConnectionResult testConnection(String url, String user, String pass);
    record ConnectionResult(boolean success, String message) {}
    default void notify(Project project, String title, String content, NotificationType type) {
        com.intellij.notification.NotificationGroupManager.getInstance()
                .getNotificationGroup("CreateEntity Notification Group")
                .createNotification(title, content, type)
                .notify(project);
    }
}
