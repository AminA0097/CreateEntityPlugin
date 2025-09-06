package com.plugin;

import com.google.common.util.concurrent.ServiceManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.plugin.Service.CreateEntityImpl;
import com.plugin.Service.CreateEntityInterface;
import com.plugin.Ui.CreateEntityUi;
import org.jetbrains.annotations.NotNull;
import com.intellij.notification.*;

import java.util.List;
import java.util.Map;

import static com.intellij.openapi.diff.impl.patch.formove.PatchApplier.showError;

public class CreateEntity extends AnAction{
    private final CreateEntityInterface createEntityInterface;
    public CreateEntity() {
        createEntityInterface = new CreateEntityImpl();
    }
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) return;

        VirtualFile selectedFolder = e.getData(CommonDataKeys.VIRTUAL_FILE);
        if (selectedFolder == null || !selectedFolder.isDirectory()) {
            createEntityInterface.notify(project, "Error", "Folder Not Found!", NotificationType.ERROR);
            return;
        }
//        Find application.properties
        VirtualFile dbFile = createEntityInterface.findFile(
                project, "db.properties");
        if (dbFile == null) {
            createEntityInterface.notify(project, "DB Resource Error", "db.properties not found!", NotificationType.ERROR);
            return;
        }
//        Get Db Properties
        Map<String, String> params = createEntityInterface.getDbParameters(dbFile);
        if (!params.containsKey("url") || !params.containsKey("user") || !params.containsKey("password")) {
            createEntityInterface.notify(project, "DB Error", "Missing DB parameters in db.properties", NotificationType.ERROR);
            return;
        }
//        Check Connection
        CreateEntityInterface.ConnectionResult res = createEntityInterface.testConnection(
                params.get("url"),
                params.get("user"),
                params.get("password"));
        if (!res.success()) {
            createEntityInterface.notify(project, "DB Error", res.message(), NotificationType.ERROR);
            return;
        }
        createEntityInterface.notify(project, "Success", "Database connection successful!", NotificationType.INFORMATION);
    }

}
