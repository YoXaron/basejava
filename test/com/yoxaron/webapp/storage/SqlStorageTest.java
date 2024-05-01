package com.yoxaron.webapp.storage;

import com.yoxaron.webapp.util.Config;

public class SqlStorageTest extends AbstractStorageTest {

    public SqlStorageTest() {
        super(Config.getInstance().getStorage());
    }
}
