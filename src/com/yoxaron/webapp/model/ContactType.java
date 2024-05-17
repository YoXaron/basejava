package com.yoxaron.webapp.model;

public enum ContactType {
    PHONE_NUMBER("Tel."),
    EMAIL("Email") {
        @Override
        public String toHtml0(String value) {
            return "<a href='mailto: " + value + "'>" + value + "</a>";
        }
    },
    SKYPE("Skype") {
        @Override
        public String toHtml0(String value) {
            return "<a href='skype: " + value + "'>" + value + "</a>";
        }
    },
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

    public String toHtml(String value) {
        return (value == null) ? "" : toHtml0(value);
    }

    protected String toHtml0(String value) {
        return title + ": " + value;
    }
}
