package com.yoxaron.webapp.storage;

import org.junit.jupiter.api.Disabled;

public class MapResumeStorageTest extends AbstractStorageTest {

    public MapResumeStorageTest() {
        super(new MapResumeStorage());
    }

    @Override
    @Disabled
    public void saveOverflow() {}
}
