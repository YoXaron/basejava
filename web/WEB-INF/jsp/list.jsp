<%@ page import="com.yoxaron.webapp.model.ContactType" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <title>List of all resumes</title>
</head>
<body>
<jsp:include page="fragment/header.jsp"/>
<section>
    <table>
        <tr>
            <th>Name</th>
            <th>Email</th>
        </tr>
        <jsp:useBean id="resumes" scope="request" type="java.util.List"/>
        <c:forEach items="${resumes}" var="resume">
            <jsp:useBean id="resume" type="com.yoxaron.webapp.model.Resume"/>
            <tr>
                <td><a href="resume?uuid=${resume.uuid}">${resume.fullName}</a></td>
                <td>${resume.getContact(ContactType.EMAIL)}</td>
            </tr>
        </c:forEach>
    </table>
</section>
<jsp:include page="fragment/footer.jsp"/>
</body>
</html>
