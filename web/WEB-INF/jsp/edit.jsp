<%@ page import="com.yoxaron.webapp.model.ContactType" %>
<%@ page import="com.yoxaron.webapp.model.SectionType" %>
<%@ page import="com.yoxaron.webapp.model.ListSection" %>
<%@ page import="com.yoxaron.webapp.model.OrganizationSection" %>
<%@ page import="com.yoxaron.webapp.util.DateUtil" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/styles.css">
    <link rel="stylesheet" href="css/edit-resume-styles.css">
    <jsp:useBean id="resume" type="com.yoxaron.webapp.model.Resume" scope="request"/>
    <title>Resume ${resume.fullName}</title>
</head>
<body>
<jsp:include page="fragment/header.jsp"/>
<form method="post" action="resume" enctype="application/x-www-form-urlencoded">
    <input type="hidden" name="uuid" value="${resume.uuid}">
    <div class="scrollable-panel">
        <div class="form-wrapper">
            <div class="section">ФИО</div>
            <input class="field" type="text" name="fullName" size=55 value="${resume.fullName}" placeholder="ФИО"
                   required
                   pattern="^[^\s\d]+( [^\s\d]+)*$"
                   oninvalid="this.setCustomValidity('Пожалуйста, введите корректное имя')"
                   oninput="this.setCustomValidity('')">
            <div class="section">Контакты</div>
            <c:forEach var="type" items="<%=ContactType.values()%>">
                <input class="field" type="text" name="${type.name()}" size=30
                       placeholder="${type.title}"
                       value="${resume.getContact(type)}">
            </c:forEach>
            <div class="spacer"></div>
            <c:forEach var="sectionType" items="<%=SectionType.values()%>">
                <c:set var="section" value="${resume.getSection(sectionType)}"/>
                <jsp:useBean id="section" type="com.yoxaron.webapp.model.Section"/>
                <div class="field-label">${sectionType.title}</div>
                <c:choose>
                    <c:when test="${sectionType == 'OBJECTIVE' || sectionType == 'PERSONAL'}">
                        <textarea class="field" name="${sectionType}"><%=section%></textarea>
                    </c:when>
                    <c:when test="${sectionType == 'QUALIFICATIONS' || sectionType == 'ACHIEVEMENTS'}">
                        <textarea class="field"
                                  name="${sectionType}"><%=String.join("\n", ((ListSection) section).getList())%></textarea>
                    </c:when>
                    <c:when test="${sectionType == 'EXPERIENCE' || sectionType == 'EDUCATION'}">
                        <c:forEach var="org" items="<%=((OrganizationSection) section).getOrganizations()%>"
                                   varStatus="counter">
                            <c:choose>
                                <c:when test="${counter.index == 0}">
                                </c:when>
                                <c:otherwise>
                                    <div class="spacer"></div>
                                </c:otherwise>
                            </c:choose>
                            <input class="field" type="text" placeholder="Название" name="${sectionType}" size=100
                                   value="${org.name}">
                            <input class="field" type="text" placeholder="Ссылка" name="${sectionType}link" size=100
                                   value="${org.link}">
                            <c:forEach var="period" items="${org.periods}">
                                <jsp:useBean id="period" type="com.yoxaron.webapp.model.Period"/>
                                <div class="date-section">
                                    <input class="field date" size=10
                                           name="${sectionType}${counter.index}startDate"
                                           value="<%=DateUtil.getStringFromDate(period.getBegin())%>"
                                           placeholder="Начало, ММ/ГГГГ">
                                    <input class="field date" size=10
                                           name="${sectionType}${counter.index}endDate"
                                           value="<%=DateUtil.getStringFromDate(period.getEnd())%>"
                                           placeholder="Окончание, ММ/ГГГГ">
                                </div>
                                <input class="field" type="text" size=75
                                       name="${sectionType}${counter.index}title"
                                       value="${period.title}"
                                       placeholder="Заголовок">
                                <textarea class="field" placeholder="Описание"
                                          name="${sectionType}${counter.index}description">${period.description}</textarea>
                            </c:forEach>
                        </c:forEach>
                    </c:when>
                </c:choose>
            </c:forEach>
            <div class="spacer"></div>
            <div class="button-section">
                <button class="green-submit-button" type="submit">Сохранить</button>
                <button class="red-cancel-button" type="button" onclick="window.history.back()">Отменить</button>
            </div>
        </div>
    </div>
</form>
<jsp:include page="fragment/footer.jsp"/>
</body>
</html>
