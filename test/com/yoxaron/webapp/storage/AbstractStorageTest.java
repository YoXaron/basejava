package com.yoxaron.webapp.storage;

import com.yoxaron.webapp.exception.ExistStorageException;
import com.yoxaron.webapp.exception.NotExistStorageException;
import com.yoxaron.webapp.model.ContactType;
import com.yoxaron.webapp.model.Resume;
import com.yoxaron.webapp.util.Config;
import com.yoxaron.webapp.util.ResumeTestData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;
import java.util.UUID;

public abstract class AbstractStorageTest {

    public static final String UUID_1 = UUID.randomUUID().toString();
    public static final String UUID_2 = UUID.randomUUID().toString();
    public static final String UUID_3 = UUID.randomUUID().toString();
    public static final String UUID_4 = UUID.randomUUID().toString();
    public static final String UUID_NOT_EXIST = "dummy";

    public static final Resume RESUME_1 = ResumeTestData.createFilledResume(UUID_1, "FullName1");
    public static final Resume RESUME_2 = ResumeTestData.createFilledResume(UUID_2, "FullName2");
    public static final Resume RESUME_3 = ResumeTestData.createFilledResume(UUID_3, "FullName3");
    public static final Resume RESUME_4 = ResumeTestData.createFilledResume(UUID_4, "FullName4");

    protected static final File STORAGE_DIR = Config.getInstance().getStorageDir();

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
        r.addContact(ContactType.PHONE_NUMBER, "88005553535");
        r.addContact(ContactType.EMAIL, "email@yoxaron.com");
        r.addContact(ContactType.SKYPE, "skype123");
        storage.update(r);
        Assertions.assertEquals(r, storage.get(UUID_1));
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
