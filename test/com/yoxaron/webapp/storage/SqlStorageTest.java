package com.yoxaron.webapp.storage;

import com.yoxaron.webapp.util.Config;

public class SqlStorageTest extends AbstractStorageTest {

    private static final Config config = Config.getInstance();
    public static final String url = config.getUrl();
    public static final String user = config.getUser();
    public static final String password = config.getPassword();

    public SqlStorageTest() {
        super(new SqlStorage(url, user, password));
    }
}