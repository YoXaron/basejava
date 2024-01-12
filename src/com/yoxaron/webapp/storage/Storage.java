package com.yoxaron.webapp.storage;

import com.yoxaron.webapp.model.Resume;

public interface Storage {
    Resume[] getAll();

    Resume get(String uuid);

    void save(Resume r);

    void update(Resume r);

    void delete(String uuid);

    void clear();

    int size();
}