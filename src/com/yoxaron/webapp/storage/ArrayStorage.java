package com.yoxaron.webapp.storage;

import com.yoxaron.webapp.model.Resume;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage extends AbstractArrayStorage {

    @Override
    public Object getSearchKey(String uuid) {
        for (int i = 0; i < size; i++) {
            if (uuid.equals(storage[i].getUuid())) {
                return i;
            }
        }
        return -1;
    }

    @Override
    protected void saveToArrayStorage(Resume r, int index) {
        storage[size] = r;
    }

    @Override
    protected void deleteFromArrayStorage(int index) {
        storage[index] = storage[size - 1];
    }
}