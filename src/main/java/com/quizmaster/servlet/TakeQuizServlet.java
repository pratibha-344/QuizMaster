package com.quizmaster.servlet;

import com.quizmaster.dao.QuestionDao;
import com.quizmaster.dao.QuizDao;
import com.quizmaster.dao.ResultDao;
import com.quizmaster.model.Quiz;
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

@WebServlet("/student/quiz")
public class TakeQuizServlet extends BaseServlet {
    private final QuizService service = new QuizService(new QuizDao(), new QuestionDao(), new ResultDao());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        long quizId = WebUtil.longParameter(request, "id");
        try {
            @SuppressWarnings("unchecked")
            Map<Long, Long> starts = (Map<Long, Long>) request.getSession().getAttribute("attemptStarts");
            Long startedAt = starts == null ? null : starts.get(quizId);
            if (startedAt == null) {
                WebUtil.flash(request, "error", "Choose a quiz and press Start to begin.");
                redirect(request, response, "/student/quizzes");
                return;
            }
            Quiz quiz = service.publishedQuiz(quizId).orElse(null);
            if (quiz == null) {
                starts.remove(quizId);
                redirect(request, response, "/student/quizzes");
                return;
            }
            long remaining = Math.max(0, quiz.getTimeLimitMinutes() * 60L
                    - (System.currentTimeMillis() - startedAt) / 1000L);
            if (remaining == 0) {
                QuizSubmitServlet.finishExpired(request, response, service, quizId, startedAt);
                return;
            }
            request.setAttribute("quiz", quiz);
            request.setAttribute("questions", service.studentQuestions(quizId));
            request.setAttribute("remainingSeconds", remaining);
            render(request, response, "take-quiz.jsp");
        } catch (SQLException ex) {
            fail(request, response, ex);
        }
    }
}
