package com.plugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.plugin.Service.CreateEntityImpl;
import com.plugin.Service.CreateEntityInterface;
import org.jetbrains.annotations.NotNull;
import com.intellij.notification.*;

import java.util.List;

import static com.intellij.openapi.diff.impl.patch.formove.PatchApplier.showError;

public class CreateEntity extends AnAction{
    private final CreateEntityInterface createEntityInterface = new CreateEntityImpl();

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) return;

        VirtualFile selectedFolder = e.getData(CommonDataKeys.VIRTUAL_FILE);
        if (selectedFolder == null || !selectedFolder.isDirectory()) {
            createEntityInterface.notify(project,"Error","Folder Not Found!",NotificationType.ERROR);
            return;
        }
//        Find application.properties
        VirtualFile resource = createEntityInterface.findFileRecursive(
                project.getBaseDir().findFileByRelativePath("src/main/resources"),"application.properties");
        if (resource == null) {
            createEntityInterface.notify(project,"Error","application.properties!",NotificationType.ERROR);
            return;
        }
//        Get Db Properties
        List<String> dbProperties = createEntityInterface.getDbParameters(resource);
        if (dbProperties.size() < 3) {
            createEntityInterface.notify(project,"Error","Failed To Find Db Parameters!",NotificationType.ERROR);
        }
//        Check Connection
        String res = createEntityInterface.testConnection(
                dbProperties.get(0),
                dbProperties.get(1),
                dbProperties.get(2));
        if (!res.equals("Done!")) {
            createEntityInterface.notify(project,"Error",res,NotificationType.ERROR);
            return;
        }
        createEntityInterface.notify(project,"Error","Db Is Ready!",NotificationType.INFORMATION);
    }



}
