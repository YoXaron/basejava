package com.yoxaron.webapp.storage.serialization;

import com.yoxaron.webapp.model.ContactType;
import com.yoxaron.webapp.model.Resume;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class DataStreamSerializer implements SerializationStrategy {

    @Override
    public void doWrite(Resume r, OutputStream outputStream) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(outputStream)) {
            dos.writeUTF(r.getUuid());
            dos.writeUTF(r.getFullName());

            Map<ContactType, String> contacts = r.getContacts();
            dos.writeInt(contacts.size());
            for (Map.Entry<ContactType, String> entry : contacts.entrySet()) {
                dos.writeUTF(entry.getKey().name());
                dos.writeUTF(entry.getValue());
            }


        }
    }

    @Override
    public Resume doRead(InputStream inputStream) throws IOException {
        try (DataInputStream dis = new DataInputStream(inputStream)) {
            String uuid = dis.readUTF();
            String fullName = dis.readUTF();

            int contactsSize = dis.readInt();
            Map<ContactType, String> contacts = new HashMap<>(contactsSize);
            for (int i = 0; i < contactsSize; i++) {
                contacts.put(ContactType.valueOf(dis.readUTF()), dis.readUTF());
            }

            return new Resume(uuid, fullName, contacts, null);
        }
    }
}
