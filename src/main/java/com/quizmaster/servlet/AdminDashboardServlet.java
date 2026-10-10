package com.quizmaster.servlet;

import com.quizmaster.dao.QuizDao;
import com.quizmaster.dao.ResultDao;
import com.quizmaster.dao.UserDao;
import com.quizmaster.util.BaseServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends BaseServlet {
    private final UserDao users = new UserDao();
    private final QuizDao quizzes = new QuizDao();
    private final ResultDao results = new ResultDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setAttribute("studentCount", users.countStudents());
            request.setAttribute("quizCount", quizzes.countAll());
            request.setAttribute("resultCount", results.countAll());
            request.setAttribute("recentResults", results.findAll().stream().limit(6).toList());
            render(request, response, "admin-dashboard.jsp");
        } catch (SQLException ex) {
            fail(request, response, ex);
        }
    }
}
