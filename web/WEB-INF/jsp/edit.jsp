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
    <link rel="stylesheet" href="css/style.css">
    <jsp:useBean id="resume" type="com.yoxaron.webapp.model.Resume" scope="request"/>
    <title>Resume ${resume.fullName}</title>
</head>
<body>
<jsp:include page="fragment/header.jsp"/>
<section class="content">

    <form method="post" action="resume" enctype="application/x-www-form-urlencoded">
        <hr>
        <input type="hidden" name="uuid" value="${resume.uuid}">
        <dl>
            <dt>Имя:</dt>
            <dd><input type="text" name="fullName" size=50 value="${resume.fullName}" placeholder="ФИО"
                       required
                       pattern="^[^\s\d]+( [^\s\d]+)*$"
                       oninvalid="this.setCustomValidity('Пожалуйста, введите корректное имя')"
                       oninput="this.setCustomValidity('')"></dd>
        </dl>
        <hr>
        <h3>Контакты:</h3>
        <c:forEach var="type" items="<%=ContactType.values()%>">
            <dl>
                <dt>${type.title}</dt>
                <dd><input type="text" name="${type.name()}" size=30 value="${resume.getContact(type)}"></dd>
            </dl>
        </c:forEach>
        <hr>

        <c:forEach var="sectionType" items="<%=SectionType.values()%>">
            <c:set var="section" value="${resume.getSection(sectionType)}"/>
            <jsp:useBean id="section" type="com.yoxaron.webapp.model.Section"/>
            <h3>${sectionType.title}</h3>
            <c:choose>
                <c:when test="${sectionType == 'OBJECTIVE'}">
                    <input type="text" name="${sectionType}" size=75 value="<%=section%>">
                </c:when>
                <c:when test="${sectionType == 'PERSONAL'}">
                    <textarea name="${sectionType}" cols=75 rows=5><%=section%></textarea>
                </c:when>
                <c:when test="${sectionType == 'QUALIFICATIONS' || sectionType == 'ACHIEVEMENTS'}">
                    <textarea name="${sectionType}" cols=75 rows=5><%=String.join("\n", ((ListSection) section).getList())%></textarea>
                </c:when>
                <c:when test="${sectionType == 'EXPERIENCE' || sectionType == 'EDUCATION'}">
                    <c:forEach var="org" items="<%=((OrganizationSection) section).getOrganizations()%>"
                               varStatus="counter">
                        <dl>
                            <dt>Название учреждения:</dt>
                            <dd><input type="text" name="${sectionType}" size=100 value="${org.name}"></dd>
                            <br>
                            <dt>Сайт учреждения:</dt>
                            <dd><input type="text" name="${sectionType}link" size=100 value="${org.link}"></dd>
                        </dl>
                        <div style="margin-left: 30px">
                            <c:forEach var="period" items="${org.periods}">
                                <jsp:useBean id="period" type="com.yoxaron.webapp.model.Period"/>
                                <dl>
                                    <dt>Начальная дата:</dt>
                                    <dd>
                                        <input type="text" name="${sectionType}${counter.index}startDate" size=10
                                               value="<%=DateUtil.getStringFromDate(period.getBegin())%>"
                                               placeholder="MM/yyyy">
                                    </dd>
                                </dl>
                                <dl>
                                    <dt>Конечная дата:</dt>
                                    <dd>
                                        <input type="text" name="${sectionType}${counter.index}endDate" size=10
                                               value="<%=DateUtil.getStringFromDate(period.getEnd())%>"
                                               placeholder="MM/yyyy">
                                    </dd>
                                </dl>
                                <dl>
                                    <dt>Должность:</dt>
                                    <dd>
                                        <input type="text" name="${sectionType}${counter.index}title" size=75
                                               value="${period.title}">
                                    </dd>
                                </dl>
                                <dl>
                                    <dt>Описание:</dt>
                                    <dd>
                                        <textarea type="text" name="${sectionType}${counter.index}description" cols=75
                                                  rows=2>${period.description}</textarea>
                                    </dd>
                                </dl>
                            </c:forEach>
                        </div>
                        <hr>
                    </c:forEach>
                </c:when>
            </c:choose>
        </c:forEach>

        <button type="submit">Сохранить</button>
        <button type="button" onclick="window.history.back()">Отменить</button>
    </form>

</section>
<jsp:include page="fragment/footer.jsp"/>
</body>
</html>
