package com.yoxaron.webapp.storage;

import com.yoxaron.webapp.storage.serialization.ObjectStreamSerializer;

public class ObjectStreamPathStorageTest extends AbstractStorageTest {

    public ObjectStreamPathStorageTest() {
        super(new PathStorage(STORAGE_DIR.getAbsolutePath(), new ObjectStreamSerializer()));
    }
}