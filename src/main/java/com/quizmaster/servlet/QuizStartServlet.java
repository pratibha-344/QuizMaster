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

@WebServlet("/student/quiz/start")
public class QuizStartServlet extends BaseServlet {
    private final QuizService service = new QuizService(new QuizDao(), new QuestionDao(), new ResultDao());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        long quizId = WebUtil.longParameter(request, "quizId");
        try {
            Quiz quiz = service.publishedQuiz(quizId)
                    .orElseThrow(() -> new IllegalArgumentException("That quiz is not available."));
            if (quiz.getQuestionCount() == 0) {
                WebUtil.flash(request, "error", "This quiz does not have any questions yet.");
                redirect(request, response, "/student/quizzes");
                return;
            }
            @SuppressWarnings("unchecked")
            Map<Long, Long> starts = (Map<Long, Long>) request.getSession().getAttribute("attemptStarts");
            if (starts == null) {
                starts = new HashMap<>();
                request.getSession().setAttribute("attemptStarts", starts);
            }
            starts.putIfAbsent(quizId, System.currentTimeMillis());
            redirect(request, response, "/student/quiz?id=" + quizId);
        } catch (IllegalArgumentException ex) {
            WebUtil.flash(request, "error", ex.getMessage());
            redirect(request, response, "/student/quizzes");
        } catch (SQLException ex) {
            fail(request, response, ex);
        }
    }
}
