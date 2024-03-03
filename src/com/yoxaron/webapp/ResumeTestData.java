package com.yoxaron.webapp;

import com.yoxaron.webapp.model.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class ResumeTestData {
    public static void main(String[] args) {
        Map<ContactType, String> contacts = new EnumMap<>(ContactType.class);
        contacts.put(ContactType.PHONE_NUMBER, "+7(921) 855-0482");
        contacts.put(ContactType.SKYPE, "skype:grigory.kislin");
        contacts.put(ContactType.EMAIL, "gkislin@yandex.ru");
        contacts.put(ContactType.GITHUB, "https://github.com/gkislin");
        contacts.put(ContactType.LINKEDIN, "https://linkedin.com/in/gkislin");
        contacts.put(ContactType.STACKOVERFLOW, "https://stackoverflow.com/users/548473/grigory-kislin");
        contacts.put(ContactType.HOME_PAGE, "http://gkislin.ru/");

        Map<SectionType, Section> sections = new EnumMap<>(SectionType.class);
        sections.put(SectionType.PERSONAL, new TextSection("Аналитический склад ума, сильная логика, креативность, " +
                "инициативность. Пурист кода и архитектуры."));
        sections.put(SectionType.OBJECTIVE, new TextSection("Ведущий стажировок и корпоративного обучения " +
                "по Java Web и Enterprise технологиям"));

        List<String> achievements = new ArrayList<>();
        achievements.add("Организация команды и успешная реализация Java проектов для сторонних заказчиков: " +
                "приложения автопарк на стеке Spring Cloud/микросервисы, система мониторинга показателей " +
                "спортсменов на Spring Boot, участие в проекте МЭШ на Play-2, многомодульный Spring Boot + Vaadin " +
                "проект для комплексных DIY смет");
        achievements.add("С 2013 года: разработка проектов \"Разработка Web приложения\",\"Java Enterprise\", " +
                "\"Многомодульный maven. Многопоточность. XML (JAXB/StAX). Веб сервисы (JAX-RS/SOAP). " +
                "Удаленное взаимодействие (JMS/AKKA)\". Организация онлайн стажировок и ведение проектов. " +
                "Более 3500 выпускников.");
        achievements.add("Реализация двухфакторной аутентификации для онлайн платформы управления проектами Wrike. " +
                "Интеграция с Twilio, DuoSecurity, Google Authenticator, Jira, Zendesk.");
        achievements.add("Налаживание процесса разработки и непрерывной интеграции ERP системы River BPM. " +
                "Интеграция с 1С, Bonita BPM, CMIS, LDAP. Разработка приложения управления окружением на стеке: " +
                "Scala/Play/Anorm/JQuery. Разработка SSO аутентификации и авторизации различных ERP модулей, " +
                "интеграция CIFS/SMB java сервера.");
        achievements.add("Реализация c нуля Rich Internet Application приложения на стеке технологий JPA, Spring, " +
                "Spring-MVC, GWT, ExtGWT (GXT), Commet, HTML5, Highstock для алгоритмического трейдинга.");
        achievements.add("Создание JavaEE фреймворка для отказоустойчивого взаимодействия слабо-связанных сервисов " +
                "(SOA-base архитектура, JAX-WS, JMS, AS Glassfish). Сбор статистики сервисов и информации " +
                "о состоянии через систему мониторинга Nagios. Реализация онлайн клиента для администрирования и " +
                "мониторинга системы по JMX (Jython/ Django).");
        achievements.add("Реализация протоколов по приему платежей всех основных платежных системы России " +
                "(Cyberplat, Eport, Chronopay, Сбербанк), Белоруcсии(Erip, Osmp) и Никарагуа.");

        sections.put(SectionType.ACHIEVEMENTS, new ListSection(achievements));

        List<String> qualifications = new ArrayList<>();
        qualifications.add("JEE AS: GlassFish (v2.1, v3), OC4J, JBoss, Tomcat, Jetty, WebLogic, WSO2");
        qualifications.add("Version control: Subversion, Git, Mercury, ClearCase, Perforce");
        qualifications.add("DB: PostgreSQL(наследование, pgplsql, PL/Python), Redis (Jedis), H2, Oracle, MySQL, " +
                "SQLite, MS SQL, HSQLDB");
        qualifications.add("Languages: Java, Scala, Python/Jython/PL-Python, JavaScript, Groovy");
        qualifications.add("XML/XSD/XSLT, SQL, C/C++, Unix shell scripts");
        qualifications.add("Java Frameworks: Java 8 (Time API, Streams), Guava, Java Executor, MyBatis, " +
                "Spring (MVC, Security, Data, Clouds, Boot), JPA (Hibernate, EclipseLink), Guice, " +
                "GWT(SmartGWT, ExtGWT/GXT), Vaadin, Jasperreports, Apache Commons, Eclipse SWT, JUnit, " +
                "Selenium (htmlelements).");
        qualifications.add("Python: Django.");
        qualifications.add("JavaScript: jQuery, ExtJS, Bootstrap.js, underscore.js");
        qualifications.add("Scala: SBT, Play2, Specs2, Anorm, Spray, Akka");
        qualifications.add("Технологии: Servlet, JSP/JSTL, JAX-WS, REST, EJB, RMI, JMS, JavaMail, JAXB, StAX, " +
                "SAX, DOM, XSLT, MDB, JMX, JDBC, JPA, JNDI, JAAS, SOAP, AJAX, Commet, HTML5, ESB, CMIS, BPMN2, " +
                "LDAP, OAuth1, OAuth2, JWT.");
        qualifications.add("Инструменты: Maven + plugin development, Gradle, настройка Ngnix");
        qualifications.add("администрирование Hudson/Jenkins, Ant + custom task, SoapUI, JPublisher, Flyway, " +
                "Nagios, iReport, OpenCmis, Bonita, pgBouncer");
        qualifications.add("Отличное знание и опыт применения концепций ООП, SOA, шаблонов проектрирования, " +
                "архитектурных шаблонов, UML, функционального программирования");
        qualifications.add("Родной русский, английский \"upper intermediate\"");

        sections.put(SectionType.QUALIFICATIONS, new ListSection(qualifications));

        List<Organization> organizations = new ArrayList<>();

        organizations.add(new Organization("Java Online Projects", "http://javaops.ru/",
                List.of(new Period(
                        LocalDate.of(2013, 10, 1),
                        LocalDate.now(),
                        "Автор проекта.",
                        "Создание, организация и проведение Java онлайн проектов и стажировок."))));

        organizations.add(new Organization("Wrike", "https://www.wrike.com/",
                List.of(new Period(
                        LocalDate.of(2014, 10, 1),
                        LocalDate.of(2016, 1, 1),
                        "Старший разработчик (backend)",
                        "Проектирование и разработка онлайн платформы управления проектами Wrike " +
                                "(Java 8 API, Maven, Spring, MyBatis, Guava, Vaadin, PostgreSQL, Redis). " +
                                "Двухфакторная аутентификация, авторизация по OAuth1, OAuth2, JWT SSO."))));

        organizations.add(new Organization("RIT Center", "",
                List.of(new Period(
                        LocalDate.of(2012, 4, 1),
                        LocalDate.of(2014, 10, 1),
                        "Java архитектор",
                        "Организация процесса разработки системы ERP для разных окружений: релизная " +
                                "политика, версионирование, ведение CI (Jenkins), миграция базы (кастомизация " +
                                "Flyway), конфигурирование системы (pgBoucer, Nginx), AAA via SSO. Архитектура БД и " +
                                "серверной части системы. Разработка интеграционных сервисов: CMIS, BPMN2, 1C " +
                                "(WebServices), сервисов общего назначения (почта, экспорт в pdf, doc, html). " +
                                "Интеграция Alfresco JLAN для online редактирование из браузера документов MS " +
                                "Office. Maven + plugin development, Ant, Apache Commons, Spring security, " +
                                "Spring MVC, Tomcat,WSO2, xcmis, OpenCmis, Bonita, Python scripting, Unix shell " +
                                "remote scripting via ssh tunnels, PL/Python"))));

        organizations.add(new Organization("Luxoft (Deutsche Bank)", "",
                List.of(new Period(
                        LocalDate.of(2010, 12, 1),
                        LocalDate.of(2012, 04, 1),
                        "Ведущий программист",
                        "Участие в проекте Deutsche Bank CRM (WebLogic, Hibernate, Spring, Spring MVC, " +
                                "SmartGWT, GWT, Jasper, Oracle). Реализация клиентской и серверной части CRM. " +
                                "Реализация RIA-приложения для администрирования, мониторинга и анализа результатов " +
                                "в области алгоритмического трейдинга. JPA, Spring, Spring-MVC, GWT, ExtGWT (GXT), " +
                                "Highstock, Commet, HTML5."))));

        sections.put(SectionType.EXPERIENCE, new OrganizationSection(organizations));

        List<Organization> educations = new ArrayList<>();

        educations.add(new Organization("Coursera",
                "https://www.coursera.org/learn/scala-functional-programming",
                List.of(
                        new Period(
                                LocalDate.of(2013, 3, 1),
                                LocalDate.of(2013, 5, 1),
                                "Functional Programming Principles in Scala by Martin Odersky",
                                null
                        )
                )
        ));

        educations.add(new Organization("Luxoft",
                "",
                List.of(
                        new Period(
                                LocalDate.of(2011, 3, 1),
                                LocalDate.of(2011, 4, 1),
                                "Объектно-ориентированный анализ ИС. Концептуальное моделирование на UML.",
                                null
                        )
                )
        ));

        educations.add(new Organization("Санкт-Петербургский национальный исследовательский университет " +
                "информационных технологий, механики и оптики",
                "itmo.ru",
                List.of(
                        new Period(
                                LocalDate.of(1993, 9, 1),
                                LocalDate.of(1996, 7, 1),
                                "Аспирантура (программист С, С++)",
                                null
                        ),

                        new Period(
                                LocalDate.of(1987, 9, 1),
                                LocalDate.of(1993, 7, 1),
                                "Инженер программист",
                                null
                        )
                )
        ));

        sections.put(SectionType.EDUCATION, new OrganizationSection(educations));

        Resume resume = new Resume("1", "Григорий Кислин", contacts, sections);

        System.out.println(resume.getFullName());

        ContactType[] contactTypes = ContactType.values();
        for (ContactType contactType : contactTypes) {
            System.out.print(contactType.getTitle() + ": ");
            String contact = contacts.get(contactType);
            System.out.println(contact);
        }

        SectionType[] sectionTypes = SectionType.values();
        for (SectionType sectionType : sectionTypes) {
            System.out.println(sectionType.getTitle());
            Section section = sections.get(sectionType);
            System.out.println(section);
        }
    }
}