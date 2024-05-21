package com.yoxaron.webapp.storage;

import com.yoxaron.webapp.exception.NotExistStorageException;
import com.yoxaron.webapp.model.ContactType;
import com.yoxaron.webapp.model.Resume;
import com.yoxaron.webapp.model.Section;
import com.yoxaron.webapp.model.SectionType;
import com.yoxaron.webapp.sql.SqlHelper;
import com.yoxaron.webapp.util.JsonParser;

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
        return sqlHelper.executeTransactionalQuery(conn -> {
            Map<String, Resume> map = new LinkedHashMap<>();

            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM resume ORDER BY full_name, uuid")) {
                ResultSet resultSet = ps.executeQuery();
                while (resultSet.next()) {
                    String uuid = resultSet.getString("uuid");
                    map.put(uuid, new Resume(uuid, resultSet.getString("full_name")));
                }
            }

            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM contact")) {
                ResultSet resultSet = ps.executeQuery();
                while (resultSet.next()) {
                    String uuid = resultSet.getString("resume_uuid");
                    Resume r = map.get(uuid);
                    addContact(resultSet, r);
                }
            }

            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM section")) {
                ResultSet resultSet = ps.executeQuery();
                while (resultSet.next()) {
                    String uuid = resultSet.getString("resume_uuid");
                    Resume r = map.get(uuid);
                    addSection(resultSet, r);
                }
            }

            return new ArrayList<>(map.values());
        });
    }

    @Override
    public Resume get(String uuid) {
        return sqlHelper.executeTransactionalQuery(conn -> {
            Resume resume;

            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM resume WHERE uuid=?")) {
                ps.setString(1, uuid);
                ResultSet resultSet = ps.executeQuery();
                if (resultSet.next()) {
                    String fullName = resultSet.getString("full_name");
                    resume = new Resume(uuid, fullName);
                } else {
                    throw new NotExistStorageException(uuid);
                }
            }

            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM contact WHERE resume_uuid=?")) {
                ps.setString(1, uuid);
                ResultSet resultSet = ps.executeQuery();
                while (resultSet.next()) {
                    addContact(resultSet, resume);
                }
            }

            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM section WHERE resume_uuid=?")) {
                ps.setString(1, uuid);
                ResultSet resultSet = ps.executeQuery();
                while (resultSet.next()) {
                    addSection(resultSet, resume);
                }
            }

            return resume;
        });
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
            insertSections(conn, r);
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
            deleteContacts(conn, r);
            deleteSections(conn, r);
            insertContacts(conn, r);
            insertSections(conn, r);
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
            resume.setContact(type, value);
        }
    }

    private void addSection(ResultSet resultSet, Resume resume) throws SQLException {
        String value = resultSet.getString("value");
        if (value != null) {
            SectionType type = SectionType.valueOf(resultSet.getString("type"));
            resume.setSection(type, JsonParser.read(value, Section.class));
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

    private void insertSections(Connection conn, Resume r) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO section (resume_uuid, type, value) VALUES (?, ?, ?)")) {
            for (Map.Entry<SectionType, Section> entry : r.getSections().entrySet()) {
                ps.setString(1, r.getUuid());
                ps.setString(2, entry.getKey().name());
                ps.setString(3, JsonParser.write(entry.getValue(), Section.class));
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void deleteContacts(Connection conn, Resume r) throws SQLException {
        deleteAttributes(conn, r, "DELETE FROM contact WHERE resume_uuid = ?");
    }

    private void deleteSections(Connection conn, Resume r) throws SQLException {
        deleteAttributes(conn, r, "DELETE FROM section WHERE resume_uuid = ?");
    }

    private void deleteAttributes(Connection conn, Resume r, String sql) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, r.getUuid());
            ps.execute();
        }
    }
}
