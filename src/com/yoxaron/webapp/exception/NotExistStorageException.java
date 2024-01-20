package com.yoxaron.webapp.exception;

public class NotExistStorageException extends StorageException {

    public NotExistStorageException(String uuid) {
        super("Resume " + uuid + " does not exist",  uuid);
    }
}
