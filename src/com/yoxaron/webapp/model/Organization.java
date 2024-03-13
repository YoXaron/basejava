package com.yoxaron.webapp.model;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class Organization implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String name;
    private final String link;
    private final List<Period> periods;

    public Organization(String name, String link, List<Period> periods) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(periods);
        this.name = name;
        this.link = link;
        this.periods = periods;
    }

    public String getName() {
        return name;
    }

    public String getLink() {
        return link;
    }

    public List<Period> getPeriods() {
        return periods;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Organization that = (Organization) o;

        if (!name.equals(that.name)) return false;
        if (!Objects.equals(link, that.link)) return false;
        return periods.equals(that.periods);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (link != null ? link.hashCode() : 0);
        result = 31 * result + periods.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Organization{" +
                "name='" + name + '\'' +
                ", link='" + link + '\'' +
                ", periods=" + periods +
                '}';
    }
}
