package com.yoxaron.webapp.storage;

import com.yoxaron.webapp.exception.StorageException;
import com.yoxaron.webapp.model.Resume;
import com.yoxaron.webapp.storage.serialization.SerializationStrategy;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PathStorage extends AbstractStorage<Path> {

    private final Path directory;
    private final SerializationStrategy strategy;

    protected PathStorage(String dir, SerializationStrategy strategy) {
        directory = Paths.get(dir);
        Objects.requireNonNull(directory, "directory must not be null");
        Objects.requireNonNull(strategy, "serialization strategy must not be null");
        this.strategy = strategy;

        if (!Files.isDirectory(directory) || !Files.isWritable(directory)) {
            throw new IllegalArgumentException(dir + " is not a directory or not writable");
        }
    }

    @Override
    protected Path getSearchKey(String uuid) {
        return directory.resolve(uuid);
    }

    @Override
    protected List<Resume> doGetAll() {
        return getDirectoryStream()
                .filter(Files::isRegularFile)
                .map(this::doGet)
                .collect(Collectors.toList());
    }

    @Override
    protected Resume doGet(Path path) {
        try {
            return strategy.doRead(Files.newInputStream(path));
        } catch (IOException e) {
            throw new StorageException("Failed to read file " + path, getFileName(path), e);
        }
    }

    @Override
    protected void doSave(Resume r, Path path) {
        try {
            Files.createFile(path);
        } catch (IOException e) {
            throw new StorageException("Failed to create file " + path, getFileName(path), e);
        }
        doUpdate(r, path);
    }

    @Override
    protected void doUpdate(Resume r, Path path) {
        try {
            strategy.doWrite(r, new BufferedOutputStream(Files.newOutputStream(path)));
        } catch (IOException e) {
            throw new StorageException("Failed to write to file " + path, getFileName(path), e);
        }
    }

    @Override
    protected void doDelete(Path path) {
        try {
            Files.delete(path);
        } catch (IOException e) {
            throw new StorageException("Failed to delete file " + path, getFileName(path), e);
        }
    }

    @Override
    protected boolean isExist(Path path) {
        return Files.exists(path);
    }

    @Override
    public void clear() {
        getDirectoryStream().forEach(this::doDelete);
    }

    @Override
    public int size() {
        return (int) getDirectoryStream().count();
    }

    private Stream<Path> getDirectoryStream() {
        try {
            return Files.list(directory);
        } catch (IOException e) {
            throw new StorageException("Failed to get directory stream", null, e);
        }
    }

    private String getFileName(Path path) {
        return path.getFileName().toString();
    }
}