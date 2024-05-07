package com.yoxaron.webapp.web;

import com.yoxaron.webapp.model.Resume;
import com.yoxaron.webapp.util.ResumeTestData;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ResumeServlet extends HttpServlet {

    private final List<Resume> resumes = new ArrayList<>();

    public ResumeServlet() {
        for (int i = 0; i < 10; i++) {
            resumes.add(ResumeTestData.createFilledResume(UUID.randomUUID().toString(), "Name" + i));
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        String name = request.getParameter("name");
        PrintWriter out = response.getWriter();
        out.write(name == null ? "<h1>Hello, friend!</h1>" : "<h1>Hello, " + name + "!</h1>");

        out.write("<h2>Resume table</h2>");

        out.write("<style>");
        out.write("table { border-collapse: collapse; width: auto; }");
        out.write("th, td { border: 1px solid black; padding: 5px; text-align: center; }");
        out.write("</style>");

        out.write("<table>");
        out.write("<tr>");
        out.write("<th>UUID</th>");
        out.write("<th>Full name</th>");
        out.write("</tr>");
        for (Resume r : resumes) {
            out.write("<tr>");
            out.write("<td>" + r.getUuid() + "</td>");
            out.write("<td>" + r.getFullName() + "</td>");
            out.write("</tr>");
        }
        out.write("</table>");
        out.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
