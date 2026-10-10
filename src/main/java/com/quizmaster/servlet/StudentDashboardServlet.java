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

@WebServlet("/student/dashboard")
public class StudentDashboardServlet extends BaseServlet {
    private final QuizDao quizzes = new QuizDao();
    private final ResultDao results = new ResultDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            long userId = currentUser(request).getId();
            request.setAttribute("quizzes", quizzes.findPublished());
            request.setAttribute("results", results.findForStudent(userId));
            request.setAttribute("average", Math.round(results.averageForStudent(userId)));
            render(request, response, "student-dashboard.jsp");
        } catch (SQLException ex) {
            fail(request, response, ex);
        }
    }
}
