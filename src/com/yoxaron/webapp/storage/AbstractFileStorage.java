package com.yoxaron.webapp.storage;

import com.yoxaron.webapp.exception.StorageException;
import com.yoxaron.webapp.model.Resume;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class AbstractFileStorage extends AbstractStorage<File> {

    private final File directory;

    protected AbstractFileStorage(File directory) {
        Objects.requireNonNull(directory, "directory must not be null");

        if (!directory.isDirectory()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not a directory");
        }

        if (!directory.canRead() || !directory.canWrite()) {
            throw new IllegalArgumentException((directory.getAbsolutePath() + " is not readable/writable"));
        }

        this.directory = directory;
    }

    @Override
    protected File getSearchKey(String uuid) {
        return new File(directory, uuid);
    }

    @Override
    protected List<Resume> doGetAll() {
        List<Resume> resumes = new ArrayList<>();

        File[] files = getFiles(directory);
        for (File file : files) {
            resumes.add(doGet(file));
        }

        return resumes;
    }

    @Override
    protected Resume doGet(File file) {
        try {
            return doRead(file);
        } catch (IOException e) {
            throw new StorageException("Failed to create file", file.getName(), e);
        }
    }

    @Override
    protected void doSave(Resume r, File file) {
        try {
            if (!file.createNewFile()) {
                throw new StorageException("Failed to create file", file.getName());
            } else {
                doWrite(r, file);
            }
        } catch (IOException e) {
            throw new StorageException("Failed to write to file", file.getName(), e);
        }
    }

    @Override
    protected void doUpdate(Resume r, File file) {
        try {
            doWrite(r, file);
        } catch (IOException e) {
            throw new StorageException("Failed to update file", file.getName(), e);
        }
    }

    @Override
    protected void doDelete(File file) {
        if (!file.delete()) {
            throw new StorageException("Failed to delete file", file.getName());
        }
    }

    @Override
    protected boolean isExist(File file) {
        return file.exists();
    }

    @Override
    public void clear() {
        File[] files = getFiles(directory);

        for (File file : files) {
            doDelete(file);
        }
    }

    @Override
    public int size() {
        return getFiles(directory).length;
    }

    private File[] getFiles(File directory) {
        File[] files = directory.listFiles();
        if (files == null) {
            throw new StorageException("Directory is empty");
        }
        return files;
    }

    protected abstract void doWrite(Resume r, File file) throws IOException;

    protected abstract Resume doRead(File file) throws IOException;
}
