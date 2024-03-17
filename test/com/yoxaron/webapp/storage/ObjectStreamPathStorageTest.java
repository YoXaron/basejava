package com.yoxaron.webapp.storage;

import com.yoxaron.webapp.storage.serialization.ObjectStreamSerialization;

public class ObjectStreamPathStorageTest extends AbstractStorageTest {
    public ObjectStreamPathStorageTest() {
        super(new PathStorage(STORAGE_DIR.toString(), new ObjectStreamSerialization()));
    }
}