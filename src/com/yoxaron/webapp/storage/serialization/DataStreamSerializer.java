package com.yoxaron.webapp.storage.serialization;

import com.yoxaron.webapp.exception.StorageException;
import com.yoxaron.webapp.model.*;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        dos.writeInt(contacts.size());
        for (Map.Entry<ContactType, String> entry : contacts.entrySet()) {
            dos.writeUTF(entry.getKey().name());
            dos.writeUTF(entry.getValue());
        }
    }

    private Map<ContactType, String> readContacts(DataInputStream dis, Map<ContactType, String> contacts) throws IOException {
        int contactsSize = dis.readInt();
        for (int i = 0; i < contactsSize; i++) {
            contacts.put(ContactType.valueOf(dis.readUTF()), dis.readUTF());
        }
        return contacts;
    }

    private void writeSections(DataOutputStream dos, Map<SectionType, Section> sections) throws IOException {
        dos.writeInt(sections.size());
        for (Map.Entry<SectionType, Section> entry : sections.entrySet()) {
            SectionType sectionType = entry.getKey();
            Section section = entry.getValue();
            dos.writeUTF(sectionType.name());
            switch (sectionType) {
                case PERSONAL, OBJECTIVE -> writeTextSection(dos, (TextSection) section);
                case ACHIEVEMENTS, QUALIFICATIONS -> writeListSection(dos, (ListSection) section);
                case EXPERIENCE, EDUCATION -> writeOrganizationSection(dos, (OrganizationSection) section);
            }
        }
    }

    private Map<SectionType, Section> readSections(DataInputStream dis, Map<SectionType, Section> sections) throws IOException {
        int sectionsSize = dis.readInt();
        for (int i = 0; i < sectionsSize; i++) {
            SectionType sectionType = SectionType.valueOf(dis.readUTF());
            switch (sectionType) {
                case PERSONAL, OBJECTIVE -> sections.put(sectionType, readTextSection(dis));
                case ACHIEVEMENTS, QUALIFICATIONS -> sections.put(sectionType, readListSection(dis));
                case EXPERIENCE, EDUCATION -> sections.put(sectionType, readOrganizationSection(dis));
                default ->
                        throw new StorageException("DataStreamSerializer: unknown section class: " + sectionType.name());
            }
        }
        return sections;
    }

    private void writeTextSection(DataOutputStream dos, TextSection section) throws IOException {
        dos.writeUTF(section.getText());
    }

    private TextSection readTextSection(DataInputStream dis) throws IOException {
        return new TextSection(dis.readUTF());
    }

    private void writeListSection(DataOutputStream dos, ListSection section) throws IOException {
        List<String> list = section.getList();
        dos.writeInt(list.size());
        for (String s : list) {
            dos.writeUTF(s);
        }
    }

    private ListSection readListSection(DataInputStream dis) throws IOException {
        int listSize = dis.readInt();
        List<String> list = new ArrayList<>();
        for (int j = 0; j < listSize; j++) {
            String item = dis.readUTF();
            list.add(item);
        }
        return new ListSection(list);
    }

    private void writeOrganizationSection(DataOutputStream dos, OrganizationSection section) throws IOException {
        List<Organization> organizations = section.getOrganizations();
        dos.writeInt(organizations.size());
        for (Organization org : organizations) {
            dos.writeUTF(org.getName());
            dos.writeUTF(org.getLink());
            List<Period> periods = org.getPeriods();
            dos.writeInt(periods.size());
            for (Period period : periods) {
                dos.writeLong(period.getBegin().toEpochDay());
                dos.writeLong(period.getEnd().toEpochDay());
                dos.writeUTF(period.getTitle());
                boolean hasDescription = (period.getDescription() != null);
                dos.writeBoolean(hasDescription);
                if (hasDescription) {
                    dos.writeUTF(period.getDescription());
                }
            }
        }
    }

    private OrganizationSection readOrganizationSection(DataInputStream dis) throws IOException {
        int orgSize = dis.readInt();
        List<Organization> organizations = new ArrayList<>();
        for (int j = 0; j < orgSize; j++) {
            String orgName = dis.readUTF();
            String orgLink = dis.readUTF();
            int periodsSize = dis.readInt();
            List<Period> periods = new ArrayList<>();
            for (int k = 0; k < periodsSize; k++) {
                LocalDate begin = LocalDate.ofEpochDay(dis.readLong());
                LocalDate end = LocalDate.ofEpochDay(dis.readLong());
                String title = dis.readUTF();
                boolean hasDescription = dis.readBoolean();
                String description = null;
                if (hasDescription) {
                    description = dis.readUTF();
                }
                periods.add(new Period(begin, end, title, description));
            }
            organizations.add(new Organization(orgName, orgLink, periods));
        }
        return new OrganizationSection(organizations);
    }
}