<%@ page import="com.yoxaron.webapp.model.ContactType" %>
<%@ page import="com.yoxaron.webapp.model.SectionType" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/styles.css">
    <link rel="stylesheet" href="css/list-resume-styles.css">
    <title>Список всех резюме</title>
</head>
<body>
<jsp:include page="fragment/header.jsp"/>
<div class="scrollable-panel">
    <div class="table-wrapper">
        <a href="resume?null&action=add">
            <p class="btn-new">Добавить резюме</p>
        </a>
        <div class="resumes-list">
            <table>
                <tr class="t-header">
                    <th class="name-column">Имя</th>
                    <th class="info-column">Позиция</th>
                    <th class="img-column">Редактировать</th>
                    <th class="img-column">Удалить</th>
                </tr>
                <jsp:useBean id="resumes" scope="request" type="java.util.List"/>
                <c:forEach items="${resumes}" var="resume">
                    <jsp:useBean id="resume" type="com.yoxaron.webapp.model.Resume"/>
                    <tr class="t-body">
                        <td class="name-column">
                            <a class="contact-link" href="resume?uuid=${resume.uuid}&action=view">${resume.fullName}</a>
                        </td>
                        <td class="info-column">
                            <p><%=resume.getSection(SectionType.OBJECTIVE)%></p>
                        </td>
                        <td class="img-column">
                            <a class="no-underline-anchor" href="resume?uuid=${resume.uuid}&action=edit">
                                <img src="img/edit.svg" style="width: 20px; height: 20px;">
                            </a>
                        </td>
                        <td class="img-column">
                            <a class="no-underline-anchor" href="resume?uuid=${resume.uuid}&action=delete">
                                <img src="img/delete.png" style="width: 20px; height: 20px;">
                            </a>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </div>
    </div>
</div>
<jsp:include page="fragment/footer.jsp"/>
</body>
</html>
