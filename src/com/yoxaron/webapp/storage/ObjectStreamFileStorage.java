package com.yoxaron.webapp.storage;

import com.yoxaron.webapp.exception.StorageException;
import com.yoxaron.webapp.model.Resume;

import java.io.*;

public class ObjectStreamFileStorage extends AbstractFileStorage {

    protected ObjectStreamFileStorage(File directory) {
        super(directory);
    }

    @Override
    protected void doWrite(Resume r, OutputStream outputStream) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(outputStream)) {
            oos.writeObject(r);
        }
    }

    @Override
    protected Resume doRead(InputStream inputStream) throws IOException {
        try (ObjectInputStream ois = new ObjectInputStream(inputStream)) {
            return (Resume) ois.readObject();
        } catch (ClassNotFoundException e) {
            throw new StorageException("Failed to deserialize the object", null, e);
        }
    }
}