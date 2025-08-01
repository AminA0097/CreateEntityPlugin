package com.plugin.Service;

import com.github.weisj.jsvg.S;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.plugin.CreateEntity;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class CreateEntityImpl implements CreateEntityInterface {
    @Override
    public List<String> getDbParameters(VirtualFile dbFile) {
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

    @Override
    public VirtualFile findFileRecursive(VirtualFile root, String filename) {
        for (VirtualFile child : root.getChildren()) {
            if (child.isDirectory()) {
                VirtualFile found = findFileRecursive(child, filename);
                if (found != null) return found;
            } else if (child.getName().equals(filename)) {
                return child;
            }
        }
        return null;
    }

    @Override
    public String testConnection(String url, String user, String pass) {
        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection(url, user, pass);
            return "Done!";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @Override
    public String createFiles(Project project,
                              VirtualFile selectedFolder,
                              String entityName,
                              boolean isExtended) {
        StringBuilder tableName = new StringBuilder("FRE_");
        for(int i = 0;i < entityName.length(); i++) {
            char c = entityName.charAt(i);
            if(Character.isUpperCase(c)) {
                tableName.append("_");
            }
            tableName.append(Character.toUpperCase(c));
        }
        return "";
    }
}
