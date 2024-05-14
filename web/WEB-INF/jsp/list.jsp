<%@ page import="com.yoxaron.webapp.model.Resume" %>
<%@ page import="java.util.List" %>
<%@ page import="com.yoxaron.webapp.model.ContactType" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html" ; charset="UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <title>Resumes</title>
</head>
<body>

<div class="header">
    <h1>RESUME LIST</h1>
</div>

<section>
    <table>
        <tr>
            <th>Name</th>
            <th>Email</th>
        </tr>
        <%
            for (Resume resume : (List<Resume>) request.getAttribute("resumes")) {
        %>
        <tr>
            <td><a href="resume?uuid=<%=resume.getUuid()%>"/><%=resume.getFullName()%>
            </td>
            <td><%=resume.getContact(ContactType.EMAIL)%>
            </td>
        </tr>
        <%
            }
        %>
    </table>
</section>
</body>
</html>
