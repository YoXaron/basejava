package com.yoxaron.webapp.storage;

import com.yoxaron.webapp.exception.StorageException;
import com.yoxaron.webapp.model.Resume;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

public abstract class AbstractPathStorage extends AbstractStorage<Path> {

    private final Path directory;

    protected AbstractPathStorage(String dir) {
        directory = Paths.get(dir);
        Objects.requireNonNull(directory, "directory must not be null");

        if (!Files.isDirectory(directory) || !Files.isWritable(directory)) {
            throw new IllegalArgumentException(dir + "is not a directory or not writable");
        }
    }

    @Override
    protected Path getSearchKey(String uuid) {
        return null;
    }

    @Override
    protected List<Resume> doGetAll() {
        return null;
    }

    @Override
    protected Resume doGet(Path file) {
        return null;
    }

    @Override
    protected void doSave(Resume r, Path file) {

    }

    @Override
    protected void doUpdate(Resume r, Path file) {

    }

    @Override
    protected void doDelete(Path file) {

    }

    @Override
    protected boolean isExist(Path file) {
        return false;
    }

    @Override
    public void clear() {
        try {
            Files.list(directory).forEach(this::doDelete);
        } catch (IOException e) {
            throw new StorageException("Failed to clear directory");
        }
    }

    @Override
    public int size() {
        return getFiles(directory).length;
    }

    private Path[] getFiles(Path directory) {
        return null;
    }

    protected abstract void doWrite(Resume r, OutputStream outputStream) throws IOException;

    protected abstract Resume doRead(InputStream inputStream) throws IOException;
}