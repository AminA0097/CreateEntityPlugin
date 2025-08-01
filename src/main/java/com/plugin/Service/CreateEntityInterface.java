package com.plugin.Service;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;

import java.util.List;

public interface CreateEntityInterface {
    public List<String> getDbParameters(VirtualFile dbFile);
    public VirtualFile findFileRecursive(VirtualFile root, String filename);
    public String testConnection(String url, String user, String pass);
    default void notify(Project project,String title, String content, NotificationType type){
        Notification notification = NotificationGroupManager.getInstance()
                .getNotificationGroup("CreateEntity Notification Group")
                .createNotification(title, content, type);
        notification.notify(project);
    };
    public String createFiles(Project project,VirtualFile selectedFolder,String entityName,boolean isExtended);
}
