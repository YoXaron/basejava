package com.yoxaron.webapp.storage;

import com.yoxaron.webapp.exception.NotExistStorageException;
import com.yoxaron.webapp.model.ContactType;
import com.yoxaron.webapp.model.Resume;
import com.yoxaron.webapp.sql.SqlHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SqlStorage implements Storage {

    private final SqlHelper sqlHelper;

    public SqlStorage(SqlHelper sqlHelper) {
        this.sqlHelper = sqlHelper;
    }

    @Override
    public List<Resume> getAllSorted() {
        return sqlHelper.executeQuery("SELECT * FROM resume r " +
                        "LEFT JOIN contact c ON r.uuid = c.resume_uuid " +
                        "ORDER BY full_name, uuid",
                ps -> {
                    Map<String, Resume> resumes = new LinkedHashMap<>();
                    ResultSet resultSet = ps.executeQuery();
                    while (resultSet.next()) {
                        String uuid = resultSet.getString("uuid");
                        Resume resume = resumes.get(uuid);

                        if (resume == null) {
                            resume = new Resume(uuid, resultSet.getString("full_name"));
                            resumes.put(uuid, resume);
                        }

                        addContact(resultSet, resume);
                    }
                    return new ArrayList<>(resumes.values());
                }
        );
    }

    @Override
    public Resume get(String uuid) {
        return sqlHelper.executeQuery("SELECT * FROM resume r " +
                        " LEFT JOIN contact c" +
                        " ON r.uuid = c.resume_uuid" +
                        " WHERE r.uuid = ?",
                ps -> {
                    ps.setString(1, uuid);
                    ResultSet resultSet = ps.executeQuery();
                    if (!resultSet.next()) {
                        throw new NotExistStorageException(uuid);
                    }
                    Resume r = new Resume(uuid, resultSet.getString("full_name"));

                    do {
                        addContact(resultSet, r);
                    } while (resultSet.next());

                    return r;
                }
        );
    }

    @Override
    public void save(Resume r) {
        sqlHelper.executeTransactionalQuery(conn -> {
            try (PreparedStatement ps = conn.prepareStatement("INSERT INTO resume (uuid, full_name) VALUES (?, ?)")) {
                ps.setString(1, r.getUuid());
                ps.setString(2, r.getFullName());
                ps.execute();
            }

            insertContacts(conn, r);
            return null;
        });
    }

    @Override
    public void update(Resume r) {
        sqlHelper.executeTransactionalQuery(conn -> {
            try (PreparedStatement ps = conn.prepareStatement("UPDATE resume SET full_name = ? WHERE uuid = ?")) {
                ps.setString(1, r.getFullName());
                ps.setString(2, r.getUuid());

                if (ps.executeUpdate() == 0) {
                    throw new NotExistStorageException(r.getUuid());
                }

                ps.execute();
            }
            deleteContact(conn, r);
            insertContacts(conn, r);
            return null;
        });
    }

    @Override
    public void delete(String uuid) {
        sqlHelper.executeQuery("DELETE FROM resume WHERE uuid = ?",
                ps -> {
                    ps.setString(1, uuid);
                    int rowsDeleted = ps.executeUpdate();
                    if (rowsDeleted == 0) {
                        throw new NotExistStorageException(uuid);
                    }
                    return null;
                }
        );
    }

    @Override
    public void clear() {
        sqlHelper.executeQuery("DELETE FROM resume",
                ps -> {
                    ps.execute();
                    return null;
                }
        );
    }

    @Override
    public int size() {
        return sqlHelper.executeQuery("SELECT COUNT(*) AS total FROM resume",
                ps -> {
                    ResultSet resultSet = ps.executeQuery();
                    return resultSet.next() ? resultSet.getInt("total") : 0;
                }
        );
    }

    private void addContact(ResultSet resultSet, Resume resume) throws SQLException {
        ContactType type = ContactType.valueOf(resultSet.getString("type"));
        String value = resultSet.getString("value");
        if (value != null) {
            resume.addContact(type, value);
        }
    }

    private void insertContacts(Connection conn, Resume r) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO contact (resume_uuid, type, value) VALUES (?, ?, ?)")) {
            for (Map.Entry<ContactType, String> entry : r.getContacts().entrySet()) {
                ps.setString(1, r.getUuid());
                ps.setString(2, entry.getKey().name());
                ps.setString(3, entry.getValue());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void deleteContact(Connection conn, Resume r) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM contact WHERE resume_uuid = ?")) {
            ps.setString(1, r.getUuid());
            ps.execute();
        }
    }
}
