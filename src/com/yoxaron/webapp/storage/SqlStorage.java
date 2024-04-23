package com.yoxaron.webapp.storage;

import com.yoxaron.webapp.exception.ExistStorageException;
import com.yoxaron.webapp.exception.NotExistStorageException;
import com.yoxaron.webapp.exception.StorageException;
import com.yoxaron.webapp.model.Resume;
import com.yoxaron.webapp.sql.ConnectionFactory;
import org.postgresql.util.PSQLException;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SqlStorage implements Storage {

    private final ConnectionFactory connectionFactory;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        connectionFactory = () -> DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    @Override
    public List<Resume> getAllSorted() {
        List<Resume> resumes = new ArrayList<>();
        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM resume")) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                resumes.add(new Resume(resultSet.getString("uuid"), resultSet.getString("full_name")));
            }
        } catch (SQLException e) {
            throw new StorageException(e);
        }

        Collections.sort(resumes, Comparator.comparing(Resume::getFullName));

        return resumes;
    }

    @Override
    public Resume get(String uuid) {
        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM resume r WHERE r.uuid = ?")) {
            statement.setString(1, uuid);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new NotExistStorageException(uuid);
            }
            return new Resume(uuid, resultSet.getString("full_name"));
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    @Override
    public void save(Resume r) {
        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement("INSERT INTO resume (uuid, full_name) VALUES (?, ?)")) {
            statement.setString(1, r.getUuid());
            statement.setString(2, r.getFullName());
            statement.execute();
        } catch (PSQLException e) {
            if ("23505".equals(e.getSQLState())) {
                throw new ExistStorageException(r.getUuid());
            } else {
                throw new StorageException("Error while saving resume", e);
            }
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    @Override
    public void update(Resume r) {
        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement("UPDATE resume SET full_name = ? WHERE uuid = ?")) {
            statement.setString(1, r.getFullName());
            statement.setString(2, r.getUuid());
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated == 0) {
                throw new NotExistStorageException(r.getUuid());
            }
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    @Override
    public void delete(String uuid) {
        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM resume WHERE uuid = ?")) {
            statement.setString(1, uuid);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted == 0) {
                throw new NotExistStorageException(uuid);
            }
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    @Override
    public void clear() {
        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM resume")) {
            statement.execute();
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    @Override
    public int size() {
        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) AS total FROM resume")) {
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("total");
            }
            return 0;
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }
}