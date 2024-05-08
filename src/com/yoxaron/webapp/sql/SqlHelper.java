package com.yoxaron.webapp.sql;

import com.yoxaron.webapp.exception.SqlExceptionConverter;
import com.yoxaron.webapp.exception.StorageException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SqlHelper {

    private final ConnectionFactory connectionFactory;

    public SqlHelper(String url, String user, String password) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        connectionFactory = () -> DriverManager.getConnection(url, user, password);
    }

    public <T> T executeQuery(String query, QueryExecutor<T> queryExecutor) {
        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            return queryExecutor.execute(statement);
        } catch (SQLException e) {
            throw SqlExceptionConverter.convertException(e);
        }
    }

    public <T> T executeTransactionalQuery(SqlTransaction<T> executor) {
        try (Connection connection = connectionFactory.getConnection()) {
            try {
                connection.setAutoCommit(false);
                T res = executor.execute(connection);
                connection.commit();
                return res;
            } catch (SQLException e) {
                connection.rollback();
                throw SqlExceptionConverter.convertException(e);
            }
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }
}
