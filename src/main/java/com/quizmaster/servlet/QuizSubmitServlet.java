package com.quizmaster.servlet;

import com.quizmaster.dao.QuestionDao;
import com.quizmaster.dao.QuizDao;
import com.quizmaster.dao.ResultDao;
import com.quizmaster.model.QuizResult;
import com.quizmaster.service.QuizService;
import com.quizmaster.util.BaseServlet;
import com.quizmaster.util.WebUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/student/quiz/submit")
public class QuizSubmitServlet extends BaseServlet {
    private final QuizService service = new QuizService(new QuizDao(), new QuestionDao(), new ResultDao());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        long quizId = WebUtil.longParameter(request, "quizId");
        @SuppressWarnings("unchecked")
        Map<Long, Long> starts = (Map<Long, Long>) request.getSession().getAttribute("attemptStarts");
        Long startedAt = starts == null ? null : starts.get(quizId);
        if (startedAt == null) {
            WebUtil.flash(request, "error", "This quiz attempt is no longer active.");
            redirect(request, response, "/student/quizzes");
            return;
        }
        try {
            Map<Long, String> answers = parseAnswers(request);
            QuizResult result = service.submit(currentUser(request).getId(), quizId, answers,
                    startedAt, System.currentTimeMillis());
            starts.remove(quizId);
            redirect(request, response, "/student/results/view?id=" + result.getId());
        } catch (IllegalArgumentException ex) {
            starts.remove(quizId);
            WebUtil.flash(request, "error", ex.getMessage());
            redirect(request, response, "/student/quizzes");
        } catch (SQLException ex) {
            fail(request, response, ex);
        }
    }

    private Map<Long, String> parseAnswers(HttpServletRequest request) {
        Map<Long, String> answers = new HashMap<>();
        request.getParameterMap().forEach((name, values) -> {
            if (name.startsWith("q_") && values.length > 0) {
                try {
                    String value = values[0].trim().toUpperCase();
                    if (value.matches("[ABCD]")) answers.put(Long.parseLong(name.substring(2)), value);
                } catch (NumberFormatException ignored) {
                    // Ignore malformed question IDs; the service scores only real quiz questions.
                }
            }
        });
        return answers;
    }

    static void finishExpired(HttpServletRequest request, HttpServletResponse response,
                              QuizService service, long quizId, long startedAt)
            throws ServletException, IOException {
        try {
            QuizResult result = service.submit(((com.quizmaster.model.User)
                            request.getSession().getAttribute("authUser")).getId(),
                    quizId, Map.of(), startedAt, System.currentTimeMillis());
            Object value = request.getSession().getAttribute("attemptStarts");
            if (value instanceof Map<?, ?> starts) starts.remove(quizId);
            response.sendRedirect(request.getContextPath() + "/student/results/view?id=" + result.getId());
        } catch (SQLException | IllegalArgumentException ex) {
            request.getServletContext().log("Could not finalize an expired quiz.", ex);
            WebUtil.flash(request, "error", "Your quiz time expired. Please start a new attempt.");
            response.sendRedirect(request.getContextPath() + "/student/quizzes");
        }
    }
}
