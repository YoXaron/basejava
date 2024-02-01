package com.yoxaron.webapp.storage;

import com.yoxaron.webapp.exception.StorageException;
import com.yoxaron.webapp.model.Resume;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public abstract class AbstractArrayStorage extends AbstractStorage {

    protected static final int STORAGE_CAPACITY = 10000;
    protected final Resume[] storage = new Resume[STORAGE_CAPACITY];
    protected int size;

    protected abstract void saveToStorage(Resume r);

    protected abstract void deleteFromStorage(int index);

    protected abstract Object getSearchKey(String uuid);

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    public Resume[] getAll() {
        return Arrays.copyOf(storage, size);
    }

    @Override
    protected Resume getResume(Object index) {
        return storage[(int) index];
    }

    @Override
    protected void saveResume(Resume r, Object index) {
        if (size == STORAGE_CAPACITY) {
            throw new StorageException("Storage is full", r.getUuid());
        } else {
            saveToStorage(r);
            size++;
        }
    }

    @Override
    protected void updateResume(Resume r, Object index) {
        storage[(int) index] = r;
    }

    @Override
    protected void deleteResume(Object index) {
        deleteFromStorage((int) index);
        storage[--size] = null;
    }

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    public int size() {
        return size;
    }

    protected boolean isExist(Object index) {
        return (Integer) index >= 0;
    }
}