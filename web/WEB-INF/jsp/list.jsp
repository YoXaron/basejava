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

<jsp:include page="fragment/header.jsp"/>

<div class="content">
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
</div>
<jsp:include page="fragment/footer.jsp"/>

</body>
</html>
