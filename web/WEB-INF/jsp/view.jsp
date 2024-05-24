<%@ page import="com.yoxaron.webapp.model.TextSection" %>
<%@ page import="com.yoxaron.webapp.model.ListSection" %>
<%@ page import="com.yoxaron.webapp.model.OrganizationSection" %>
<%@ page import="com.yoxaron.webapp.util.DateUtil" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/styles.css">
    <link rel="stylesheet" href="css/view-resume-styles.css">
    <jsp:useBean id="resume" type="com.yoxaron.webapp.model.Resume" scope="request"/>
    <title>Резюме ${resume.fullName}</title>
</head>
<body>
<jsp:include page="fragment/header.jsp"/>
<div class="scrollable-panel">
    <div class="form-wrapper">
        <div class="full-name">${resume.fullName}&nbsp;
            <a class="no-underline-anchor" href="resume?uuid=${resume.uuid}&action=edit">
                <img src="img/edit.svg">
            </a>
        </div>
        <div class="contacts">
            <c:forEach var="contactEntry" items="${resume.contacts}">
                <jsp:useBean id="contactEntry"
                             type="java.util.Map.Entry<com.yoxaron.webapp.model.ContactType, java.lang.String>"/>
                <div><%=contactEntry.getKey().toHtml(contactEntry.getValue())%>
                </div>
            </c:forEach>
        </div>
        <div class="spacer"></div>
        <c:forEach var="sectionEntry" items="${resume.sections}">
            <jsp:useBean id="sectionEntry"
                         type="java.util.Map.Entry<com.yoxaron.webapp.model.SectionType, com.yoxaron.webapp.model.Section>"/>
            <c:set var="sectionType" value="${sectionEntry.key}"/>
            <c:set var="section" value="${sectionEntry.value}"/>
            <jsp:useBean id="section" type="com.yoxaron.webapp.model.Section"/>
            <div class="section">${sectionType.title}</div>
            <c:choose>
                <c:when test="${sectionType.name() =='OBJECTIVE'}">
                    <div class="position"><%=((TextSection) section).getText()%>
                    </div>
                </c:when>
                <c:when test="${sectionType.name() =='PERSONAL'}">
                    <div class="qualities"><%=((TextSection) section).getText()%>
                    </div>
                </c:when>
                <c:when test="${sectionType.name() =='QUALIFICATIONS' || sectionType.name() =='ACHIEVEMENTS'}">
                    <ul class="list">
                        <c:forEach var="item" items="<%=((ListSection) section).getList()%>">
                            <li>${item}</li>
                        </c:forEach>
                    </ul>
                </c:when>
                <c:when test="${sectionType.name() == 'EXPERIENCE' || sectionType.name() == 'EDUCATION'}">
                    <c:forEach var="org" items="<%=((OrganizationSection) section).getOrganizations()%>">
                        <div class="section-wrapper">
                            <c:choose>
                                <c:when test="${empty org.link}">
                                    <div class="job-name">${org.name}</div>
                                </c:when>
                                <c:otherwise>
                                    <div class="job-name"><a class="contact-link"
                                                             href="${org.link}">${org.name}</a></div>
                                </c:otherwise>
                            </c:choose>
                            <c:forEach var="period" items="${org.periods}">
                                <jsp:useBean id="period" type="com.yoxaron.webapp.model.Period"/>
                                <div class="period-position">
                                    <div class="period"><%=DateUtil.getStringFromPeriod(period)%>
                                    </div>
                                    <div class="position">${period.title}</div>
                                </div>
                                <c:choose>
                                    <c:when test="${empty period.description}">
                                    </c:when>
                                    <c:otherwise>
                                        <div class="description">${period.description}</div>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </div>
                    </c:forEach>
                </c:when>
            </c:choose>
        </c:forEach>
        <button class="button-back" onclick=window.history.back()>Назад</button>
        <div class="footer-spacer"></div>
    </div>
</div>
<jsp:include page="fragment/footer.jsp"/>
</body>
</html>
