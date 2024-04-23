package com.yoxaron.webapp.sql;

import com.yoxaron.webapp.exception.ExistStorageException;
import com.yoxaron.webapp.exception.StorageException;
import com.yoxaron.webapp.util.Config;
import org.postgresql.util.PSQLException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SqlHelper {

    private final ConnectionFactory connectionFactory;

    public SqlHelper() {
        Config config = Config.getInstance();
        connectionFactory = () -> DriverManager.getConnection(config.getDbUrl(), config.getDbUser(), config.getDbPassword());
    }

    public <T> T executeQuery(String query, QueryExecutor<T> queryExecutor) {
        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            return queryExecutor.execute(statement);
        } catch (PSQLException e) {
            if ("23505".equals(e.getSQLState())) {
                throw new ExistStorageException("Resume already exists");
            } else {
                throw new StorageException("Error while saving resume", e);
            }
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }
}
