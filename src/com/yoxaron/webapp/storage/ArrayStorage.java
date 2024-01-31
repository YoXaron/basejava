package com.yoxaron.webapp.storage;

import com.yoxaron.webapp.model.Resume;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage extends AbstractArrayStorage {

    @Override
    protected void saveToStorage(Resume r) {
        storage[size] = r;
    }

    @Override
    protected void deleteFromStorage(int index) {
        storage[index] = storage[size - 1];
    }

    @Override
    public Object getIndex(String uuid) {
        for (int i = 0; i < size; i++) {
            if (uuid.equals(storage[i].getUuid())) {
                return i;
            }
        }
        return -1;
    }
}