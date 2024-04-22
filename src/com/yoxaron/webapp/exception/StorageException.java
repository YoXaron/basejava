package com.yoxaron.webapp.exception;

public class StorageException extends RuntimeException {
    private final String uuid;

    public StorageException(Exception cause) {
        this(cause.getMessage(), cause);
    }

    public StorageException(String message) {
        this(message, null, null);
    }

    public StorageException(String message, String uuid) {
        this(message, uuid, null);
    }

    public StorageException(String message, Exception cause) {
        this(message, null, cause);
    }

    public StorageException(String message, String uuid, Exception cause) {
        super(message, cause);
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }
}
