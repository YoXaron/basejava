package com.yoxaron.webapp.model;

public enum SectionType {
    PERSONAL("Личные качества"),
    OBJECTIVE("Позиция"),
    ACHIEVEMENT("Достижения"),
    QUALIFICATIONS("Квалификация"),
    EXPERIENCE("Опыт"),
    EDUCATION("Образование");

    private String title;

    SectionType(String title) {
        this.title = title;
    }
}
