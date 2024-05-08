package com.yoxaron.webapp.web;

import com.yoxaron.webapp.model.Resume;
import com.yoxaron.webapp.storage.Storage;
import com.yoxaron.webapp.util.Config;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

public class ResumeServlet extends HttpServlet {

    private Storage storage;

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        storage = Config.getInstance().getStorage();
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
        for (Resume r : storage.getAllSorted()) {
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
