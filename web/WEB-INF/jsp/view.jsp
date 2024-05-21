<%@ page import="com.yoxaron.webapp.model.ContactType" %>
<%@ page import="com.yoxaron.webapp.model.TextSection" %>
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

    <h2>${resume.fullName}&nbsp;
        <a href="resume?uuid=${resume.uuid}&action=edit">
            <img src="img/edit.png" style="width: 25px; height: 25px;">
        </a>
    </h2>

    <p>
        <c:forEach var="contactEntry" items="${resume.contacts}">
            <jsp:useBean id="contactEntry"
                         type="java.util.Map.Entry<com.yoxaron.webapp.model.ContactType, java.lang.String>"/>
            <%=contactEntry.getKey().toHtml(contactEntry.getValue()) %>
            <br/>
        </c:forEach>
    </p>

    <hr>

    <table>
        <c:forEach var="sectionEntry" items="${resume.sections}">
            <jsp:useBean id="sectionEntry"
                         type="java.util.Map.Entry<com.yoxaron.webapp.model.SectionType, com.yoxaron.webapp.model.Section>"/>
            <c:set var="sectionType" value="${sectionEntry.key}"/>
            <c:set var="section" value="${sectionEntry.value}"/>
            <jsp:useBean id="section" type="com.yoxaron.webapp.model.Section"/>

            <tr>
                <td><h3><a name="type.name">${sectionType.title}</a></h3></td>
                <c:if test="${sectionType.name() == 'OBJECTIVE'}">
                    <td>
                        <h3><%=((TextSection) section).getText()%></h3>
                    </td>
                </c:if>
            </tr>

            <c:if test="${sectionType.name() != 'OBJECTIVE'}">
                <c:choose>
                    <c:when test="${sectionType.name() == 'PERSONAL'}">
                        <tr>
                            <td><%=((TextSection) section).getText()%></td>
                        </tr>
                    </c:when>

                    <c:when test="${sectionType.name() == 'QUALIFICATIONS' || sectionType.name() == 'ACHIEVEMENTS'}">
                        <tr>
                            <td>
                                <ul>
                                    <c:forEach var="item" items="<%=((ListSection) section).getList()%>">
                                        <li>${item}</li>
                                    </c:forEach>
                                </ul>
                            </td>
                        </tr>
                    </c:when>

                    <c:when test="${sectionType.name() == 'EXPERIENCE' || sectionType.name() == 'EDUCATION'}">
                        <c:forEach var="org" items="<%=((OrganizationSection) section).getOrganizations()%>">
                            <tr>
                                <td>
                                    <c:choose>
                                        <c:when test="${empty org.link}">
                                            <h4>${org.name}</h4>
                                        </c:when>
                                        <c:otherwise>
                                            <h4><a href="${org.link}">${org.name}</a></h4>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                            <c:forEach var="period" items="${org.periods}">
                                <jsp:useBean id="period" type="com.yoxaron.webapp.model.Period"/>
                                <tr>
                                    <td><%=DateUtil.getStringFromPeriod(period)%>
                                    </td>
                                    <td>
                                        <b>${period.title}</b>
                                        <br>
                                            ${period.description}
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:forEach>
                    </c:when>
                </c:choose>
            </c:if>
        </c:forEach>
    </table>
    <br>
    <button onclick=window.history.back()>Back</button>
</section>
<jsp:include page="fragment/footer.jsp"/>
</body>
</html>
