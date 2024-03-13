package com.yoxaron.webapp.storage;

import com.yoxaron.webapp.model.Resume;

import java.io.*;

public class ObjectStreamStorage extends AbstractFileStorage {

    protected ObjectStreamStorage(File directory) {
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
        Resume r;

        try (ObjectInputStream ois = new ObjectInputStream(inputStream)) {
            r = (Resume) ois.readObject();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return r;
    }
}