package com.yoxaron.webapp.storage;

import com.yoxaron.webapp.exception.StorageException;
import com.yoxaron.webapp.model.Resume;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public abstract class AbstractArrayStorageTest extends AbstractStorageTest {

    public AbstractArrayStorageTest(Storage storage) {
        super(storage);
    }

    @Test
    public void saveOverflow() {
        storage.clear();

        try {
            for (int i = 0; i < AbstractArrayStorage.STORAGE_CAPACITY; i++) {
                storage.save(new Resume("dummy"));
            }
        } catch (Exception e) {
            Assertions.fail("Overflow ahead of time");
        }

        Assertions.assertThrows(StorageException.class, () -> storage.save(new Resume("dummy")));
    }
}
