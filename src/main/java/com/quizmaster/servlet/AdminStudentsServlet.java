package com.quizmaster.servlet;

import com.quizmaster.dao.UserDao;
import com.quizmaster.util.BaseServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/admin/students")
public class AdminStudentsServlet extends BaseServlet {
    private final UserDao users = new UserDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setAttribute("students", users.listStudents());
            render(request, response, "admin-students.jsp");
        } catch (SQLException ex) {
            fail(request, response, ex);
        }
    }
}
