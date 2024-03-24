package com.yoxaron.webapp.storage;

import com.yoxaron.webapp.storage.serialization.ObjectStreamSerializer;

public class ObjectStreamFileStorageTest extends AbstractStorageTest {

    public ObjectStreamFileStorageTest() {
        super(new FileStorage(STORAGE_DIR, new ObjectStreamSerializer()));
    }
}