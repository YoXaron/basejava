package com.yoxaron.webapp.storage;

import com.yoxaron.webapp.exception.StorageException;
import com.yoxaron.webapp.model.Resume;
import com.yoxaron.webapp.storage.serialization.SerializationStrategy;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileStorage extends AbstractStorage<File> {

    private final File directory;
    private final SerializationStrategy strategy;

    protected FileStorage(File directory, SerializationStrategy strategy) {
        Objects.requireNonNull(directory, "directory must not be null");
        Objects.requireNonNull(strategy, "serialization strategy must not be null");

        if (!directory.isDirectory()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not a directory");
        }

        if (!directory.canRead() || !directory.canWrite()) {
            throw new IllegalArgumentException((directory.getAbsolutePath() + " is not readable/writable"));
        }

        this.directory = directory;
        this.strategy = strategy;
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
            return strategy.doRead(new BufferedInputStream(new FileInputStream(file)));
        } catch (IOException e) {
            throw new StorageException("Failed to read file", file.getName(), e);
        }
    }

    @Override
    protected void doSave(Resume r, File file) {
        try {
            if (!file.createNewFile()) {
                throw new StorageException("Failed to create file", file.getName());
            }
        } catch (IOException e) {
            throw new StorageException("Failed to create file", file.getAbsolutePath(), e);
        }
        doUpdate(r, file);
    }

    @Override
    protected void doUpdate(Resume r, File file) {
        try {
            strategy.doWrite(r, new BufferedOutputStream(new FileOutputStream(file)));
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
            throw new StorageException("Failed to get files from the directory");
        }
        return files;
    }
}
