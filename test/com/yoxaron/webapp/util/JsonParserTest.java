package com.yoxaron.webapp.util;

import com.yoxaron.webapp.model.Resume;
import com.yoxaron.webapp.model.Section;
import com.yoxaron.webapp.model.TextSection;
import com.yoxaron.webapp.storage.AbstractStorageTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class JsonParserTest {

    @Test
    void testResume() {
        String json = JsonParser.write(AbstractStorageTest.RESUME_1);
//        System.out.println(json);
        Resume resume = JsonParser.read(json, Resume.class);
        Assertions.assertEquals(AbstractStorageTest.RESUME_1, resume);
    }

    @Test
    void write() {
        Section section1 = new TextSection("Objective1");
        String json = JsonParser.write(section1, Section.class);
//        System.out.println(json);
        Section section2 = JsonParser.read(json, Section.class);
        Assertions.assertEquals(section1, section2);
    }
}