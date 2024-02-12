package com.yoxaron.webapp.storage;

import org.junit.jupiter.api.Disabled;

public class MapUuidStorageTest extends AbstractStorageTest {

    public MapUuidStorageTest() {
        super(new MapUuidStorage());
    }

    @Override
    @Disabled
    public void saveOverflow() {}
}