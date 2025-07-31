package com.plugin;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import org.jetbrains.annotations.NotNull;

public class CreateEntity extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) return;
        VirtualFile selectedFolder = e.getData(CommonDataKeys.VIRTUAL_FILE);
        if (selectedFolder == null || !selectedFolder.isDirectory()) {
            com.intellij.openapi.ui.Messages.showErrorDialog(project, "Please select a folder", "Error");
            return;
        }

        String entityName = com.intellij.openapi.ui.Messages.showInputDialog(
                project,
                "Enter entity name:",
                "Your Creating In" + selectedFolder.getPath(),
                com.intellij.openapi.ui.Messages.getQuestionIcon()
        );
        String fileName = entityName.trim() + "Entity.java";
    }
}
