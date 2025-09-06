package com.plugin.Service;

import com.github.weisj.jsvg.S;
import com.ibm.icu.impl.coll.Collation;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.openapi.diagnostic.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.*;


public class CreateEntityImpl implements CreateEntityInterface {
    private static final Logger LOG = Logger.getInstance(CreateEntityImpl.class);

    @Override
    public VirtualFile findFile(Project project, String filename) {
        Collection<VirtualFile> files = FilenameIndex.getVirtualFilesByName(
                project,
                filename,
                GlobalSearchScope.projectScope(project)
        );
        return files.stream().findFirst().orElse(null);
    }

    @Override
    public Map<String, String> getDbParameters(VirtualFile dbFile) {
        Map<String, String> dbParameters = new HashMap<>();

        try (InputStream inputStream = dbFile.getInputStream()) {
            Properties props = new Properties();
            props.load(inputStream);
            putIfNotNull(dbParameters,"url",props.getProperty("spring.datasource.url"));
            putIfNotNull(dbParameters,"userName",props.getProperty("spring.datasource.userName"));
            putIfNotNull(dbParameters,"password",props.getProperty("spring.datasource.password"));
        } catch (IOException ex) {
            return null;
        }
        return dbParameters;
    }

    private void putIfNotNull(Map<String, String> map, String key, String value) {
        if (value != null && !value.isEmpty()) {
            map.put(key, value);
        }
    }
    @Override
    public ConnectionResult testConnection(String url, String user, String pass) {
        try {
            Class.forName("org.postgresql.Driver");
            try (Connection connection = DriverManager.getConnection(url, user, pass)) {
                return new ConnectionResult(true, "Connection successful");
            }
        } catch (Exception ex) {
            LOG.warn("DB connection failed", ex);
            return new ConnectionResult(false, ex.getMessage());
        }
    }
}
