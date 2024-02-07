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

    protected abstract Object getSearchKey(String uuid);

    protected abstract void saveToArrayStorage(Resume r, int index);

    protected abstract void deleteFromArrayStorage(int index);

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    public Resume[] getAll() {
        return Arrays.copyOf(storage, size);
    }

    @Override
    protected Resume doGet(Object index) {
        return storage[(int) index];
    }

    @Override
    protected void doSave(Resume r, Object index) {
        if (size == STORAGE_CAPACITY) {
            throw new StorageException("Storage is full", r.getUuid());
        } else {
            saveToArrayStorage(r, (int) index);
            size++;
        }
    }

    @Override
    protected void doUpdate(Resume r, Object index) {
        storage[(int) index] = r;
    }

    @Override
    protected void doDelete(Object index) {
        deleteFromArrayStorage((int) index);
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
        return (int) index >= 0;
    }
}