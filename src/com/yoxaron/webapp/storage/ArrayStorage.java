package com.yoxaron.webapp.storage;

import com.yoxaron.webapp.model.Resume;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage implements Storage {
    private static final int STORAGE_CAPACITY = 10000;
    private final Resume[] storage = new Resume[STORAGE_CAPACITY];
    private int size;

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    public Resume[] getAll() {
        return Arrays.copyOf(storage, size);
    }

    public Resume get(String uuid) {
        int index = getIndex(uuid);

        if (isExist(index)) {
            return storage[index];
        } else {
            System.out.printf("GET ERROR: Resume with UUID=%s not found in the storage.\n", uuid);
            return null;
        }
    }

    public void save(Resume r) {
        if (size == STORAGE_CAPACITY) {
            System.out.println("SAVE ERROR: Storage is full.\n");
        } else if (isExist(getIndex(r.getUuid()))) {
            System.out.printf("SAVE ERROR: This UUID=%s already exists.\n", r.getUuid());
        } else {
            storage[size++] = r;
        }
    }

    public void update(Resume r) {
        int index = getIndex(r.getUuid());

        if (isExist(index)) {
            storage[index] = r;
        } else {
            System.out.printf("UPDATE ERROR: No such resume in the storage. UUID=%s.\n", r.getUuid());
        }
    }

    public void delete(String uuid) {
        int index = getIndex(uuid);

        if (!isExist(index)) {
            System.out.printf("DELETE ERROR: No such resume in the storage. UUID=%s.\n", uuid);
        } else {
            storage[index] = storage[size - 1];
            storage[--size] = null;
        }
    }

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    public int size() {
        return size;
    }

    private boolean isExist(int index) {
        return index != -1;
    }

    public int getIndex(String uuid) {
        for (int i = 0; i < size; i++) {
            if (uuid.equals(storage[i].getUuid())) {
                return i;
            }
        }
        return -1;
    }
}