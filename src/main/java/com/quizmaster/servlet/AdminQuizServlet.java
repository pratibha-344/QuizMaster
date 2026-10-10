package com.quizmaster.servlet;

import com.quizmaster.dao.QuizDao;
import com.quizmaster.model.Quiz;
import com.quizmaster.util.BaseServlet;
import com.quizmaster.util.WebUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

@WebServlet("/admin/quizzes/*")
public class AdminQuizServlet extends BaseServlet {
    private final QuizDao quizzes = new QuizDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getPathInfo();
        try {
            if ("/new".equals(path) || "/edit".equals(path)) {
                request.setAttribute("quiz", "/edit".equals(path)
                        ? quizzes.findById(WebUtil.longParameter(request, "id")).orElse(null) : null);
                render(request, response, "admin-quiz-form.jsp");
            } else {
                request.setAttribute("quizzes", quizzes.findAll());
                render(request, response, "admin-quizzes.jsp");
            }
        } catch (SQLException ex) {
            fail(request, response, ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getPathInfo();
        try {
            if ("/delete".equals(path)) {
                quizzes.delete(WebUtil.longParameter(request, "quizId"));
                WebUtil.flash(request, "success",
                        "Quiz deleted, including its questions and saved results.");
            } else if ("/save".equals(path)) {
                saveQuiz(request);
                WebUtil.flash(request, "success", "Quiz saved.");
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            redirect(request, response, "/admin/quizzes");
        } catch (IllegalArgumentException ex) {
            request.setAttribute("formError", ex.getMessage());
            request.setAttribute("quiz", formQuiz(request));
            render(request, response, "admin-quiz-form.jsp");
        } catch (SQLException ex) {
            fail(request, response, ex);
        }
    }

    private void saveQuiz(HttpServletRequest request) throws SQLException {
        String title = WebUtil.text(request, "title");
        String description = WebUtil.text(request, "description");
        int timeLimit = WebUtil.intParameter(request, "timeLimitMinutes", 0);
        long id = Math.max(0, WebUtil.longParameter(request, "id"));
        if (title.length() < 3 || title.length() > 160) {
            throw new IllegalArgumentException("Quiz title must be between 3 and 160 characters.");
        }
        if (description.length() > 4000) {
            throw new IllegalArgumentException("Quiz description must be 4,000 characters or fewer.");
        }
        if (timeLimit < 1 || timeLimit > 240) {
            throw new IllegalArgumentException("Time limit must be from 1 to 240 minutes.");
        }
        quizzes.save(new Quiz(id, title, description, timeLimit,
                "true".equals(request.getParameter("published")), 0));
    }

    private Quiz formQuiz(HttpServletRequest request) {
        long id = Math.max(0, WebUtil.longParameter(request, "id"));
        return new Quiz(id, WebUtil.text(request, "title"), WebUtil.text(request, "description"),
                WebUtil.intParameter(request, "timeLimitMinutes", 15),
                "true".equals(request.getParameter("published")), 0);
    }
}
