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
    private final String url;
    private final String user;
    private final String password;

//    private final Storage storage;

    private Config() {
        try (InputStream is = new FileInputStream(PROPERTIES_FILE)) {
            props.load(is);
            storageDir = new File(props.getProperty("storage.dir"));
            url = props.getProperty("db.url");
            user = props.getProperty("db.user");
            password = props.getProperty("db.password");
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load config file " + PROPERTIES_FILE.getAbsolutePath());
        }
    }

    public static Config getInstance() {
        return INSTANCE;
    }

    public String getUrl() {
        return url;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public File getStorageDir() {
        return storageDir;
    }
}