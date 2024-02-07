package com.yoxaron.webapp.storage;

import com.yoxaron.webapp.model.Resume;

import java.util.Arrays;

public class SortedArrayStorage extends AbstractArrayStorage {

    @Override
    protected Object getSearchKey(String uuid) {
        Resume searchKey = new Resume(uuid);
        return Arrays.binarySearch(storage, 0, size, searchKey);
    }

    @Override
    protected void saveToArrayStorage(Resume r, int index) {
        index = -(index + 1);
        System.arraycopy(storage, index, storage, index + 1, size - index);
        storage[index] = r;
    }

    @Override
    protected void deleteFromArrayStorage(int index) {
        if (index < size - 1) {
            System.arraycopy(storage, index + 1, storage, index, size - index - 1);
        }
    }
}