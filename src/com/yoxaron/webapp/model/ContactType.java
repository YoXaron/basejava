package com.yoxaron.webapp.model;

public enum ContactType {
    PHONE_NUMBER("Tel."),
    EMAIL("Email"),
    SKYPE("Skype"),
    GITHUB("Github"),
    LINKEDIN("LinkedIn"),
    STACKOVERFLOW("Stackoverflow"),
    HOME_PAGE("Homepage");

    private final String title;

    ContactType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
