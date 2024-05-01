package com.yoxaron.webapp.exception;

import org.postgresql.util.PSQLException;

import java.sql.SQLException;

public class SqlExceptionConverter {

    public static StorageException convertException(SQLException e) {
        if (e instanceof PSQLException) {
            if (e.getSQLState().equals("23505")) {
                return new ExistStorageException(null);
            }
        }
        return new StorageException(e);
    }
}
