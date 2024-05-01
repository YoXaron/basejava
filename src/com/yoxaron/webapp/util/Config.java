package com.yoxaron.webapp.util;

import com.yoxaron.webapp.sql.SqlHelper;
import com.yoxaron.webapp.storage.SqlStorage;
import com.yoxaron.webapp.storage.Storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {

    private static final File PROPERTIES_FILE = new File("config/resumes.properties");
    public static final Config INSTANCE = new Config();

    private final File storageDir;
    private final Storage storage;

    private Config() {
        try (InputStream is = new FileInputStream(PROPERTIES_FILE)) {
            Properties props = new Properties();
            props.load(is);
            storageDir = new File(props.getProperty("storage.dir"));
            storage = new SqlStorage(new SqlHelper(
                    props.getProperty("db.url"),
                    props.getProperty("db.user"),
                    props.getProperty("db.password")));
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load config file " + PROPERTIES_FILE.getAbsolutePath());
        }
    }

    public static Config getInstance() {
        return INSTANCE;
    }

    public Storage getStorage() {
        return storage;
    }

    public File getStorageDir() {
        return storageDir;
    }
}
