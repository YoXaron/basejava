package com.yoxaron.webapp.storage;

import com.yoxaron.webapp.exception.ExistStorageException;
import com.yoxaron.webapp.exception.NotExistStorageException;
import com.yoxaron.webapp.model.Resume;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public abstract class AbstractStorage<T> implements Storage {

    private static final Logger LOG = Logger.getLogger(AbstractStorage.class.getName());

    protected abstract T getSearchKey(String uuid);

    protected abstract List<Resume> doGetAll();

    protected abstract Resume doGet(T searchKey);

    protected abstract void doSave(Resume r, T searchKey);

    protected abstract void doUpdate(Resume r, T searchKey);

    protected abstract void doDelete(T searchKey);

    protected abstract boolean isExist(T searchKey);

    public final List<Resume> getAllSorted() {
        LOG.info("GetAllSorted");
        List<Resume> list = doGetAll();
        Collections.sort(list);
        return list;
    }

    public final Resume get(String uuid) {
        LOG.info("Get " + uuid);
        T searchKey = getExistingSearchKey(uuid);
        return doGet(searchKey);
    }

    public final void save(Resume r) {
        LOG.info("Save " + r);
        T searchKey = getNotExistingSearchKey(r.getUuid());
        doSave(r, searchKey);
    }

    public final void update(Resume r) {
        LOG.info("Update " + r);
        T searchKey = getExistingSearchKey(r.getUuid());
        doUpdate(r, searchKey);
    }

    public final void delete(String uuid) {
        LOG.info("Delete " + uuid);
        T searchKey = getExistingSearchKey(uuid);
        doDelete(searchKey);
    }

    private T getExistingSearchKey(String uuid) {
        T searchKey = getSearchKey(uuid);

        if (isExist(searchKey)) {
            return searchKey;
        } else {
            LOG.warning("Resume " + uuid + " does not exist");
            throw new NotExistStorageException(uuid);
        }
    }

    private T getNotExistingSearchKey(String uuid) {
        T searchKey = getSearchKey(uuid);

        if (!isExist(searchKey)) {
            return searchKey;
        } else {
            LOG.warning("Resume " + uuid + " already exists");
            throw new ExistStorageException(uuid);
        }
    }
}