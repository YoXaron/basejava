package com.yoxaron.webapp.storage;

import com.yoxaron.webapp.exception.StorageException;
import com.yoxaron.webapp.model.Resume;

import java.util.Arrays;
import java.util.List;

/**
 * Array based storage for Resumes
 */
public abstract class AbstractArrayStorage extends AbstractStorage<Integer> {

    protected static final int STORAGE_CAPACITY = 10000;
    protected final Resume[] storage = new Resume[STORAGE_CAPACITY];
    protected int size;

    protected abstract Integer getSearchKey(String uuid);

    protected abstract void saveToArrayStorage(Resume r, int index);

    protected abstract void deleteFromArrayStorage(int index);

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    public List<Resume> doGetAll() {
        return Arrays.asList(Arrays.copyOf(storage, size));
    }

    @Override
    protected Resume doGet(Integer index) {
        return storage[index];
    }

    @Override
    protected void doSave(Resume r, Integer index) {
        if (size == STORAGE_CAPACITY) {
            throw new StorageException("Storage is full", r.getUuid());
        } else {
            saveToArrayStorage(r, index);
            size++;
        }
    }

    @Override
    protected void doUpdate(Resume r, Integer index) {
        storage[index] = r;
    }

    @Override
    protected void doDelete(Integer index) {
        deleteFromArrayStorage(index);
        storage[--size] = null;
    }

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    public int size() {
        return size;
    }

    protected boolean isExist(Integer index) {
        return index >= 0;
    }
}