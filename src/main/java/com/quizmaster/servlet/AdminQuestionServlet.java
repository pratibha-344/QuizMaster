package com.quizmaster.servlet;

import com.quizmaster.dao.QuestionDao;
import com.quizmaster.dao.QuizDao;
import com.quizmaster.model.Question;
import com.quizmaster.util.BaseServlet;
import com.quizmaster.util.WebUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/admin/questions/*")
public class AdminQuestionServlet extends BaseServlet {
    private final QuestionDao questions = new QuestionDao();
    private final QuizDao quizzes = new QuizDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        long quizId = WebUtil.longParameter(request, "quizId");
        try {
            var quiz = quizzes.findById(quizId);
            if (quiz.isEmpty()) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            request.setAttribute("quiz", quiz.get());
            request.setAttribute("questions", questions.findForAdmin(quizId));
            render(request, response, "admin-questions.jsp");
        } catch (SQLException ex) {
            fail(request, response, ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        long quizId = WebUtil.longParameter(request, "quizId");
        String path = request.getPathInfo();
        try {
            if ("/delete".equals(path)) {
                questions.delete(WebUtil.longParameter(request, "questionId"));
                WebUtil.flash(request, "success", "Question deleted.");
            } else if ("/save".equals(path)) {
                saveQuestion(request, quizId);
                WebUtil.flash(request, "success", "Question saved.");
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            redirect(request, response, "/admin/questions?quizId=" + quizId);
        } catch (IllegalArgumentException ex) {
            WebUtil.flash(request, "error", ex.getMessage());
            redirect(request, response, "/admin/questions?quizId=" + quizId);
        } catch (SQLException ex) {
            fail(request, response, ex);
        }
    }

    private void saveQuestion(HttpServletRequest request, long quizId) throws SQLException {
        long id = Math.max(0, WebUtil.longParameter(request, "questionId"));
        String text = WebUtil.text(request, "questionText");
        String a = WebUtil.text(request, "optionA");
        String b = WebUtil.text(request, "optionB");
        String c = WebUtil.text(request, "optionC");
        String d = WebUtil.text(request, "optionD");
        String correct = WebUtil.text(request, "correctOption").toUpperCase();
        if (quizId < 1 || quizzes.findById(quizId).isEmpty()) {
            throw new IllegalArgumentException("Select a valid quiz first.");
        }
        if (text.isBlank() || text.length() > 4000 || a.isBlank() || b.isBlank()
                || c.isBlank() || d.isBlank() || a.length() > 500 || b.length() > 500
                || c.length() > 500 || d.length() > 500) {
            throw new IllegalArgumentException("Enter a question and all four options (up to 500 characters each).");
        }
        if (!correct.matches("[ABCD]")) {
            throw new IllegalArgumentException("Choose the correct answer.");
        }
        questions.save(new Question(id, quizId, text, a, b, c, d, correct));
    }
}
