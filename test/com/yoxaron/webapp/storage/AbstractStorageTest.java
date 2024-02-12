package com.yoxaron.webapp.storage;

import com.yoxaron.webapp.exception.ExistStorageException;
import com.yoxaron.webapp.exception.NotExistStorageException;
import com.yoxaron.webapp.exception.StorageException;
import com.yoxaron.webapp.model.Resume;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public abstract class AbstractStorageTest {

    private static final String UUID_1 = "uuid1";
    private static final String UUID_2 = "uuid2";
    private static final String UUID_3 = "uuid3";
    private static final String UUID_4 = "uuid4";
    private static final String UUID_NOT_EXIST = "dummy";

    private static final Resume RESUME_1 = new Resume(UUID_1, "FullName1");
    private static final Resume RESUME_2 = new Resume(UUID_2, "FullName2");
    private static final Resume RESUME_3 = new Resume(UUID_3, "FullName3");
    private static final Resume RESUME_4 = new Resume(UUID_4, "FullName4");

    private final Storage storage;

    public AbstractStorageTest(Storage storage) {
        this.storage = storage;
    }

    @BeforeEach
    public void setUp() {
        storage.clear();
        storage.save(RESUME_1);
        storage.save(RESUME_2);
        storage.save(RESUME_3);
    }

    @Test
    public void getAll() {
        List<Resume> expected = List.of(RESUME_1, RESUME_2, RESUME_3);
        List<Resume> actual = storage.getAllSorted();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void get() {
        assertGet(RESUME_1);
        assertGet(RESUME_2);
        assertGet(RESUME_3);
    }

    @Test
    public void getNotExist() {
        Assertions.assertThrows(NotExistStorageException.class, () -> storage.get(UUID_NOT_EXIST));
    }

    @Test
    public void save() {
        storage.save(RESUME_4);
        assertGet(RESUME_4);
        assertSize(4);
    }

    @Test
    public void saveAlreadyExist() {
        Assertions.assertThrows(ExistStorageException.class, () -> storage.save(RESUME_1));
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

    @Test
    public void update() {
        Resume r = new Resume(UUID_1, "dummy");
        storage.update(r);
        Assertions.assertSame(r, storage.get(UUID_1));
    }

    @Test
    public void updateNotExist() {
        Resume r = new Resume(UUID_NOT_EXIST, "dummy");
        Assertions.assertThrows(NotExistStorageException.class, () -> storage.update(r));
    }

    @Test
    public void delete() {
        storage.delete(UUID_1);
        assertSize(2);
        Assertions.assertThrows(NotExistStorageException.class, () -> storage.get(UUID_1));
    }

    @Test
    public void deleteNotExist() {
        Assertions.assertThrows(NotExistStorageException.class, () -> storage.delete(UUID_NOT_EXIST));
    }

    @Test
    public void clear() {
        storage.clear();
        assertSize(0);
        Assertions.assertEquals(List.of(), storage.getAllSorted());
    }

    @Test
    public void size() {
        assertSize(3);
    }

    private void assertGet(Resume resume) {
        Assertions.assertEquals(resume, storage.get(resume.getUuid()));
    }

    private void assertSize(int size) {
        Assertions.assertEquals(size, storage.size());
    }
}