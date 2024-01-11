package com.yoxaron.webapp.storage;

import com.yoxaron.webapp.model.Resume;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    private static final int STORAGE_CAPACITY = 10000;
    private Resume[] storage = new Resume[STORAGE_CAPACITY];
    private int size;

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    public Resume[] getAll() {
        return Arrays.copyOf(storage, size);
    }

    public Resume get(String uuid) {
        int index = getIndex(uuid);
        return (index != -1) ? storage[index] : null;
    }

    public void save(Resume r) {
        if (size == STORAGE_CAPACITY) {
            System.out.println("SAVE ERROR: Storage is full\n");
            return;
        }

        if (isResumePresent(r)) {
            System.out.printf("SAVE ERROR: This UUID=%s already exists\n", r.getUuid());
        } else {
            storage[size++] = r;
        }
    }

    public void update(Resume r) {
        int indexToUpdate = getIndex(r.getUuid());

        if (indexToUpdate != -1) {
            storage[indexToUpdate] = r;
        } else {
            System.out.printf("UPDATE ERROR: No such resume in the storage. UUID=%s\n", r.getUuid());
        }
    }

    public void delete(String uuid) {
        int indexToDelete = getIndex(uuid);

        if (indexToDelete == -1) {
            System.out.printf("DELETE ERROR: No such resume in the storage. UUID=%s\n", uuid);
            return;
        }

        if (indexToDelete < size - 1) {
            System.arraycopy(storage, indexToDelete + 1, storage, indexToDelete, size - indexToDelete - 1);
        }

        storage[--size] = null;
    }

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    public int size() {
        return size;
    }

    public boolean isResumePresent(Resume r) {
        return get(r.getUuid()) != null;
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