package com.plugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class CreateEntity extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) return;

        VirtualFile selectedFolder = e.getData(CommonDataKeys.VIRTUAL_FILE);
        if (selectedFolder == null || !selectedFolder.isDirectory()) {
            Messages.showErrorDialog(project, "Please select a folder", "Error");
            return;
        }

        VirtualFile resourcesFolder = project.getBaseDir().findFileByRelativePath("src/main/resources");
        if (resourcesFolder == null) {
            Messages.showErrorDialog(project, "resources folder not found", "Error");
            return;
        }

        VirtualFile dbFile = findDbFile(resourcesFolder);
        if (dbFile == null) {
            Messages.showErrorDialog(project, "application.properties file not found in resources", "Error");
            return;
        }

        List<String> dbParameters = getDbParameters(dbFile);
        if (dbParameters == null || dbParameters.size() < 3) {
            Messages.showErrorDialog(project, "Could not load DB parameters", "Error");
            return;
        }

        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection(
                    dbParameters.get(0),
                    dbParameters.get(1),
                    dbParameters.get(2)
            );
            if (connection != null && !connection.isClosed()) {
                Messages.showInfoMessage(project, "✅ Database connection established!", "Success");
            } else {
                Messages.showErrorDialog(project, "❌ Database connection failed!", "Failure");
            }
        } catch (Exception ex) {
            Messages.showErrorDialog(project, "❌ Error connecting to database:\n" + ex.getMessage(), "Exception");
        }
    }

    private List<String> getDbParameters(VirtualFile dbFile) {
        List<String> parameters = new ArrayList<>();
        try (InputStream inputStream = dbFile.getInputStream()) {
            Properties props = new Properties();
            props.load(inputStream);

            String url = props.getProperty("spring.datasource.url");
            String user = props.getProperty("spring.datasource.username");
            String pass = props.getProperty("spring.datasource.password");

            if (url != null && user != null && pass != null) {
                parameters.add(url);
                parameters.add(user);
                parameters.add(pass);
            } else {
                return null;
            }
        } catch (IOException ex) {
            return null;
        }
        return parameters;
    }

    private VirtualFile findDbFile(VirtualFile dir) {
        for (VirtualFile child : dir.getChildren()) {
            if ("application.properties".equals(child.getName())) {
                return child;
            }
        }
        return null;
    }
}
