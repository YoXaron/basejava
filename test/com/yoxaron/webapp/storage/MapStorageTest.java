package com.yoxaron.webapp.storage;

import org.junit.jupiter.api.Disabled;

public class MapStorageTest extends AbstractStorageTest {

    public MapStorageTest() {
        super(new MapStorage());
    }

    @Override
    @Disabled
    public void saveOverflow() {}
}