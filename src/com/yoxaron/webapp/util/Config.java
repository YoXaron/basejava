package com.yoxaron.webapp.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {

    private static final File PROPERTIES_FILE = new File("config/resumes.properties");
    public static final Config INSTANCE = new Config();

    private final Properties props = new Properties();

    private final File storageDir;
    private final String dbUrl;
    private final String dbUser;
    private final String dbPassword;

    private Config() {
        try (InputStream is = new FileInputStream(PROPERTIES_FILE)) {
            props.load(is);
            storageDir = new File(props.getProperty("storage.dir"));
            dbUrl = props.getProperty("db.url");
            dbUser = props.getProperty("db.user");
            dbPassword = props.getProperty("db.password");
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load config file " + PROPERTIES_FILE.getAbsolutePath());
        }
    }

    public static Config getInstance() {
        return INSTANCE;
    }

    public String getDbUrl() {
        return dbUrl;
    }

    public String getDbUser() {
        return dbUser;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public File getStorageDir() {
        return storageDir;
    }
}