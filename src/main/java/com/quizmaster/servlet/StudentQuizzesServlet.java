package com.quizmaster.servlet;

import com.quizmaster.dao.QuizDao;
import com.quizmaster.util.BaseServlet;
import com.quizmaster.util.WebUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/student/quizzes")
public class StudentQuizzesServlet extends BaseServlet {
    private final QuizDao quizzes = new QuizDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setAttribute("quizzes", quizzes.findPublished());
            request.setAttribute("results", new com.quizmaster.dao.ResultDao()
                    .findForStudent(currentUser(request).getId()));
            render(request, response, "quizzes.jsp");
        } catch (SQLException ex) {
            fail(request, response, ex);
        }
    }
}
