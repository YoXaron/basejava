package com.yoxaron.webapp.storage;

import com.yoxaron.webapp.exception.ExistStorageException;
import com.yoxaron.webapp.exception.NotExistStorageException;
import com.yoxaron.webapp.model.Resume;

public abstract class AbstractStorage implements Storage {

    protected abstract Object getIndex(String uuid);

    protected abstract Resume getResume(Object index);

    protected abstract void updateResume(Resume r, Object index);

    protected abstract void saveResume(Resume r, Object index);

    protected abstract void deleteResume(Object index);

    protected abstract boolean isExist(Object index);

    public final Resume get(String uuid) {
        Object index = getIndex(uuid);

        if (isExist(index)) {
            return getResume(index);
        } else {
            throw new NotExistStorageException(uuid);
        }
    }

    public final void save(Resume r) {
        Object index = getIndex(r.getUuid());

        if (isExist(index)) {
            throw new ExistStorageException(r.getUuid());
        } else {
            saveResume(r, index);
        }
    }

    public final void update(Resume r) {
        Object index = getIndex(r.getUuid());

        if (isExist(index)) {
            updateResume(r, index);
        } else {
            throw new NotExistStorageException(r.getUuid());
        }
    }

    public final void delete(String uuid) {
        Object index = getIndex(uuid);

        if (isExist(index)) {
            deleteResume(index);
        } else {
            throw new NotExistStorageException(uuid);
        }
    }
}
