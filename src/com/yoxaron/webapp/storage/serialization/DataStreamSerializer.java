package com.yoxaron.webapp.storage.serialization;

import com.yoxaron.webapp.exception.StorageException;
import com.yoxaron.webapp.model.*;
import com.yoxaron.webapp.util.DataConsumer;
import com.yoxaron.webapp.util.DeserializationAction;
import com.yoxaron.webapp.util.ListDeserializationAction;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class DataStreamSerializer implements SerializationStrategy {

    @Override
    public void doWrite(Resume r, OutputStream outputStream) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(outputStream)) {
            dos.writeUTF(r.getUuid());
            dos.writeUTF(r.getFullName());

            Map<ContactType, String> contacts = r.getContacts();
            writeContacts(dos, contacts);

            Map<SectionType, Section> sections = r.getSections();
            writeSections(dos, sections);
        }
    }

    @Override
    public Resume doRead(InputStream inputStream) throws IOException {
        try (DataInputStream dis = new DataInputStream(inputStream)) {
            String uuid = dis.readUTF();
            String fullName = dis.readUTF();

            Resume resume = new Resume(uuid, fullName);
            resume.setContacts(readContacts(dis, new HashMap<>()));
            resume.setSections(readSections(dis, new HashMap<>()));

            return resume;
        }
    }

    private void writeContacts(DataOutputStream dos, Map<ContactType, String> contacts) throws IOException {
        writeWithException(contacts.entrySet(), dos, entry -> {
            dos.writeUTF(entry.getKey().name());
            dos.writeUTF(entry.getValue());
        });
    }

    private Map<ContactType, String> readContacts(DataInputStream dis, Map<ContactType, String> contacts) throws IOException {
        readWithException(dis, () -> contacts.put(ContactType.valueOf(dis.readUTF()), dis.readUTF()));
        return contacts;
    }

    private void writeSections(DataOutputStream dos, Map<SectionType, Section> sections) throws IOException {
        writeWithException(sections.entrySet(), dos, entry -> {
            SectionType sectionType = entry.getKey();
            Section section = entry.getValue();
            dos.writeUTF(sectionType.name());
            switch (sectionType) {
                case PERSONAL, OBJECTIVE -> writeTextSection(dos, (TextSection) section);
                case ACHIEVEMENTS, QUALIFICATIONS -> writeListSection(dos, (ListSection) section);
                case EXPERIENCE, EDUCATION -> writeOrganizationSection(dos, (OrganizationSection) section);
            }
        });
    }

    private Map<SectionType, Section> readSections(DataInputStream dis, Map<SectionType, Section> sections) throws IOException {
        readWithException(dis, () -> {
            SectionType sectionType = SectionType.valueOf(dis.readUTF());
            switch (sectionType) {
                case PERSONAL, OBJECTIVE -> sections.put(sectionType, readTextSection(dis));
                case ACHIEVEMENTS, QUALIFICATIONS -> sections.put(sectionType, readListSection(dis));
                case EXPERIENCE, EDUCATION -> sections.put(sectionType, readOrganizationSection(dis));
                default ->
                        throw new StorageException("DataStreamSerializer: unknown section class: " + sectionType.name());
            }
        });
        return sections;
    }

    private void writeTextSection(DataOutputStream dos, TextSection section) throws IOException {
        dos.writeUTF(section.getText());
    }

    private TextSection readTextSection(DataInputStream dis) throws IOException {
        return new TextSection(dis.readUTF());
    }

    private void writeListSection(DataOutputStream dos, ListSection section) throws IOException {
        writeWithException(section.getList(), dos, dos::writeUTF);
    }

    private ListSection readListSection(DataInputStream dis) throws IOException {
        return new ListSection(readList(dis, dis::readUTF));
    }

    private void writeOrganizationSection(DataOutputStream dos, OrganizationSection section) throws IOException {
        List<Organization> organizations = section.getOrganizations();
        writeWithException(organizations, dos, org -> {
            dos.writeUTF(org.getName());
            dos.writeUTF(org.getLink());
            List<Period> periods = org.getPeriods();
            writeWithException(periods, dos, period -> {
                dos.writeLong(period.getBegin().toEpochDay());
                dos.writeLong(period.getEnd().toEpochDay());
                dos.writeUTF(period.getTitle());
                boolean hasDescription = period.getDescription() != null;
                dos.writeBoolean(hasDescription);
                if (hasDescription) {
                    dos.writeUTF(period.getDescription());
                }
            });
        });
    }

    private OrganizationSection readOrganizationSection(DataInputStream dis) throws IOException {
        List<Organization> organizations = readList(dis, () -> {
            String orgName = dis.readUTF();
            String orgLink = dis.readUTF();
            List<Period> periods = readList(dis, () -> {
                LocalDate begin = LocalDate.ofEpochDay(dis.readLong());
                LocalDate end = LocalDate.ofEpochDay(dis.readLong());
                String title = dis.readUTF();
                boolean hasDescription = dis.readBoolean();
                String description = null;
                if (hasDescription) {
                    description = dis.readUTF();
                }
                return new Period(begin, end, title, description);
            });
            return new Organization(orgName, orgLink, periods);
        });
        return new OrganizationSection(organizations);
    }

    private <T> void writeWithException(Collection<T> collection, DataOutputStream dos, DataConsumer<T> action) throws IOException {
        dos.writeInt(collection.size());
        for (T item : collection) {
            action.accept(item);
        }
    }

    private void readWithException(DataInputStream dis, DeserializationAction action) throws IOException {
        int size = dis.readInt();
        for (int i = 0; i < size; i++) {
            action.perform();
        }
    }

    private <T> List<T> readList(DataInputStream dis, ListDeserializationAction<T> action) throws IOException {
        List<T> list = new ArrayList<>();
        readWithException(dis, () -> list.add(action.perform()));
        return list;
    }
}
