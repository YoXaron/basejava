package com.yoxaron.webapp.model;

import java.time.LocalDate;
import java.util.Objects;

public class Period {

    private final LocalDate begin;
    private final LocalDate end;
    private final String title;
    private final String description;

    public Period(LocalDate begin, LocalDate end, String title, String description) {
        Objects.requireNonNull(begin);
        Objects.requireNonNull(end);
        Objects.requireNonNull(title);
        this.begin = begin;
        this.end = end;
        this.title = title;
        this.description = description;
    }

    public LocalDate getBegin() {
        return begin;
    }

    public LocalDate getEnd() {
        return end;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Period period = (Period) o;

        if (!begin.equals(period.begin)) return false;
        if (!end.equals(period.end)) return false;
        if (!title.equals(period.title)) return false;
        return Objects.equals(description, period.description);
    }

    @Override
    public int hashCode() {
        int result = begin.hashCode();
        result = 31 * result + end.hashCode();
        result = 31 * result + title.hashCode();
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Period{" +
                "begin=" + begin +
                ", end=" + end +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
