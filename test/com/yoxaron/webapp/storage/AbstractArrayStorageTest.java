package com.yoxaron.webapp.storage;

import com.yoxaron.webapp.exception.ExistStorageException;
import com.yoxaron.webapp.exception.NotExistStorageException;
import com.yoxaron.webapp.exception.StorageException;
import com.yoxaron.webapp.model.Resume;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

abstract class AbstractArrayStorageTest {

    private final Storage storage;

    private static final String UUID_1 = "uuid1";
    private static final String UUID_2 = "uuid2";
    private static final String UUID_3 = "uuid3";

    public AbstractArrayStorageTest(Storage storage) {
        this.storage = storage;
    }

    @BeforeEach
    void setUp() {
        storage.clear();
        storage.save(new Resume(UUID_1));
        storage.save(new Resume(UUID_2));
        storage.save(new Resume(UUID_3));
    }

    @Test
    void getAll() {
        Resume r1 = new Resume(UUID_1);
        Resume r2 = new Resume(UUID_2);
        Resume r3 = new Resume(UUID_3);

        Assertions.assertArrayEquals(new Resume[]{r1, r2, r3}, storage.getAll());
    }

    @Test
    void get() {
        Resume r1 = new Resume(UUID_1);
        Assertions.assertEquals(r1, storage.get(UUID_1));
    }

    @Test
    void getNotExist() {
        Assertions.assertThrows(NotExistStorageException.class, () -> storage.get("dummy"));
    }

    @Test
    void save() {
        Resume r = new Resume();
        storage.save(r);
        Assertions.assertEquals(r, storage.get(r.getUuid()));
        Assertions.assertEquals(4, storage.size());
    }

    @Test
    void saveAlreadyExist() {
        Resume r = new Resume(UUID_1);
        Assertions.assertThrows(ExistStorageException.class, () -> storage.save(r));
    }

    @Test
    void saveOverflow() {
        storage.clear();

        try {
            for (int i = 0; i < AbstractArrayStorage.STORAGE_CAPACITY; i++) {
                storage.save(new Resume());
            }
        } catch (Exception e) {
            Assertions.fail("Something went wrong");
        }

        Assertions.assertThrows(StorageException.class, () -> storage.save(new Resume()));
    }

    @Test
    void update() {
        Resume r = new Resume(UUID_1);
        storage.update(r);
        Assertions.assertSame(r, storage.get(UUID_1));
    }

    @Test
    void updateNotExist() {
        Resume r = new Resume("dummy");
        Assertions.assertThrows(NotExistStorageException.class, () -> storage.update(r));
    }

    @Test
    void delete() {
        storage.delete(UUID_1);
        Assertions.assertEquals(2, storage.size());
        Assertions.assertThrows(NotExistStorageException.class, () -> storage.get(UUID_1));
    }

    @Test
    void deleteNotExist() {
        Assertions.assertThrows(NotExistStorageException.class, () -> storage.delete("dummy"));
    }

    @Test
    void clear() {
        storage.clear();
        Assertions.assertEquals(0, storage.size());
    }

    @Test
    void size() {
        Assertions.assertEquals(3, storage.size());
    }
}