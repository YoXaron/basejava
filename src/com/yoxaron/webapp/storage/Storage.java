package com.yoxaron.webapp.storage;

import com.yoxaron.webapp.model.Resume;

import java.util.List;

public interface Storage {

    List<Resume> getAllSorted();

    Resume get(String uuid);

    void save(Resume r);

    void update(Resume r);

    void delete(String uuid);

    void clear();

    int size();
}
