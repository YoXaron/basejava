package com.yoxaron.webapp.web;

import com.yoxaron.webapp.model.*;
import com.yoxaron.webapp.storage.Storage;
import com.yoxaron.webapp.util.Config;
import com.yoxaron.webapp.util.DateUtil;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ResumeServlet extends HttpServlet {

    private Storage storage;

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        storage = Config.getInstance().getStorage();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uuid = request.getParameter("uuid");
        String action = request.getParameter("action");

        if (action == null) {
            request.setAttribute("resumes", storage.getAllSorted());
            request.getRequestDispatcher("/WEB-INF/jsp/list.jsp").forward(request, response);
            return;
        }

        Resume r;
        switch (action) {
            case "add":
                r = Resume.EMPTY;
                break;
            case "delete":
                storage.delete(uuid);
                response.sendRedirect("resume");
                return;
            case "view":
                r = storage.get(uuid);
                break;
            case "edit":
                r = storage.get(uuid);
                for (SectionType type : SectionType.values()) {
                    Section section = r.getSection(type);
                    switch (type) {
                        case OBJECTIVE, PERSONAL -> {
                            if (section == null) {
                                section = TextSection.EMPTY;
                            }
                        }
                        case ACHIEVEMENTS, QUALIFICATIONS -> {
                            if (section == null) {
                                section = ListSection.EMPTY;
                            }
                        }
                        case EXPERIENCE, EDUCATION -> {
                            OrganizationSection organizationSection = (OrganizationSection) r.getSection(type);
                            List<Organization> emptyFirstOrganizations = new ArrayList<>();
                            emptyFirstOrganizations.add(Organization.EMPTY);
                            if (organizationSection != null) {
                                for (Organization org : organizationSection.getOrganizations()) {
                                    List<Period> emptyFirstPeriods = new ArrayList<>();
                                    emptyFirstPeriods.add(Period.EMPTY);
                                    emptyFirstPeriods.addAll(org.getPeriods());
                                    emptyFirstOrganizations.add(new Organization(org.getName(), org.getLink(), emptyFirstPeriods));
                                }
                            }
                            section = new OrganizationSection(emptyFirstOrganizations);
                        }
                    }
                    r.setSection(type, section);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown action: " + action);
        }
        request.setAttribute("resume", r);
        request.getRequestDispatcher("view".equals(action) ? "/WEB-INF/jsp/view.jsp" : "/WEB-INF/jsp/edit.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String uuid = request.getParameter("uuid");
        String fullName = request.getParameter("fullName");

        final boolean isNotCreated = (uuid == null || uuid.isEmpty());
        Resume r;
        if (isNotCreated) {
            r = new Resume(fullName);
        } else {
            r = storage.get(uuid);
            r.setFullName(fullName);
        }

        for (ContactType type : ContactType.values()) {
            String value = request.getParameter(type.name());
            if (value != null && !value.trim().isEmpty()) {
                r.setContact(type, value);
            } else {
                r.getContacts().remove(type);
            }
        }

        for (SectionType type : SectionType.values()) {
            String value = request.getParameter(type.name());
            String[] values = request.getParameterValues(type.name());
            if ((value == null || value.trim().isEmpty()) && values.length < 2) {
                r.getSections().remove(type);
            } else {
                switch (type) {
                    case OBJECTIVE, PERSONAL -> r.setSection(type, new TextSection(value));
                    case QUALIFICATIONS, ACHIEVEMENTS -> {
                        List<String> lines = Arrays.stream(value.split("\n"))
                                .filter(l -> !(l.trim().isBlank()))
                                .collect(Collectors.toList());
                        r.setSection(type, new ListSection(lines));
                    }
                    case EXPERIENCE, EDUCATION -> {
                        List<Organization> orgs = new ArrayList<>();
                        String[] links = request.getParameterValues(type.name() + "link");

                        for (int i = 0; i < values.length; i++) {
                            List<Period> periods = new ArrayList<>();
                            String name = values[i];
                            if (name != null && !name.trim().isEmpty()) {
                                String prefix = type.name() + i;
                                String[] startDates = request.getParameterValues(prefix + "startDate");
                                String[] endDates = request.getParameterValues(prefix + "endDate");
                                String[] titles = request.getParameterValues(prefix + "title");
                                String[] descriptions = request.getParameterValues(prefix + "description");

                                if (startDates != null && endDates != null && titles != null && descriptions != null &&
                                        startDates.length == endDates.length && endDates.length == titles.length && titles.length == descriptions.length) {

                                    for (int j = 0; j < titles.length; j++) {
                                        String startDate = startDates[j];
                                        String endDate = endDates[j];
                                        String title = titles[j];
                                        String description = descriptions[j];

                                        if ((startDate != null && !startDate.trim().isEmpty()) ||
                                                (endDate != null && !endDate.trim().isEmpty()) ||
                                                (title != null && !title.trim().isEmpty()) ||
                                                (description != null && !description.trim().isEmpty())) {

                                            periods.add(new Period(DateUtil.parse(startDate), DateUtil.parse(endDate), title, description));
                                        }
                                    }
                                }
                                orgs.add(new Organization(name, links[i], periods));
                            }
                        }
                        r.setSection(type, new OrganizationSection(orgs));
                    }
                }
            }
        }
        if (isNotCreated) {
            storage.save(r);
        } else {
            storage.update(r);
        }
        response.sendRedirect("resume");
    }
}
