package com.yoxaron.webapp.storage;

import com.yoxaron.webapp.model.Resume;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public abstract class AbstractArrayStorage implements Storage {

    protected static final int STORAGE_CAPACITY = 10000;
    protected final Resume[] storage = new Resume[STORAGE_CAPACITY];
    protected int size;

    protected abstract void saveToStorage(Resume r);

    protected abstract void deleteFromStorage(int index);

    protected abstract int getIndex(String uuid);

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    public Resume[] getAll() {
        return Arrays.copyOf(storage, size);
    }

    public final Resume get(String uuid) {
        int index = getIndex(uuid);

        if (isExist(index)) {
            return storage[index];
        } else {
            System.out.printf("GET ERROR: Resume with UUID=%s not found in the storage.\n", uuid);
            return null;
        }
    }

    public final void save(Resume r) {
        if (size == STORAGE_CAPACITY) {
            System.out.println("SAVE ERROR: Storage is full.\n");
        } else if (isExist(getIndex(r.getUuid()))) {
            System.out.printf("SAVE ERROR: This UUID=%s already exists.\n", r.getUuid());
        } else {
            saveToStorage(r);
            size++;
        }
    }

    public final void update(Resume r) {
        int index = getIndex(r.getUuid());

        if (isExist(index)) {
            storage[index] = r;
        } else {
            System.out.printf("UPDATE ERROR: No such resume in the storage. UUID=%s.\n", r.getUuid());
        }
    }

    public final void delete(String uuid) {
        int index = getIndex(uuid);

        if (isExist(index)) {
            deleteFromStorage(index);
            storage[--size] = null;
        } else {
            System.out.printf("DELETE ERROR: No such resume in the storage. UUID=%s.\n", uuid);
        }
    }

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    public int size() {
        return size;
    }

    protected boolean isExist(int index) {
        return index >= 0;
    }
}