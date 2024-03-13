package com.yoxaron.webapp.storage;

import com.yoxaron.webapp.ResumeTestData;
import com.yoxaron.webapp.exception.ExistStorageException;
import com.yoxaron.webapp.exception.NotExistStorageException;
import com.yoxaron.webapp.model.Resume;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

public abstract class AbstractStorageTest {

    private static final String UUID_1 = "uuid1";
    private static final String UUID_2 = "uuid2";
    private static final String UUID_3 = "uuid3";
    private static final String UUID_4 = "uuid4";
    private static final String UUID_NOT_EXIST = "dummy";

    private static final Resume RESUME_1 = ResumeTestData.createFilledResume(UUID_1, "FullName1");
    private static final Resume RESUME_2 = ResumeTestData.createFilledResume(UUID_2, "FullName2");
    private static final Resume RESUME_3 = ResumeTestData.createFilledResume(UUID_3, "FullName3");
    private static final Resume RESUME_4 = ResumeTestData.createFilledResume(UUID_4, "FullName4");

    protected static final File STORAGE_DIR = new File("/Users/yoxaron/IdeaProjects/basejava/storage");

    protected final Storage storage;

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
    public void getAllSorted() {
        List<Resume> expected = List.of(RESUME_1, RESUME_2, RESUME_3);
        List<Resume> actual = storage.getAllSorted();
        Assertions.assertEquals(3, actual.size());
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
    public void update() {
        Resume r = ResumeTestData.createFilledResume(UUID_1, "New Name");
        storage.update(r);
        Assertions.assertSame(r, storage.get(UUID_1));
    }

    @Test
    public void updateNotExist() {
        Resume r = ResumeTestData.createFilledResume(UUID_NOT_EXIST, "dummy");
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