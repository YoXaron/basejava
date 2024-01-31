package com.yoxaron.webapp.storage;

import org.junit.jupiter.api.Disabled;

public class ListStorageTest extends AbstractStorageTest {

    public ListStorageTest() {
        super(new ListStorage());
    }

    @Override
    @Disabled
    public void saveOverflow() {}
}