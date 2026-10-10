package com.quizmaster.servlet;

import com.quizmaster.dao.ResultDao;
import com.quizmaster.model.QuizResult;
import com.quizmaster.util.BaseServlet;
import com.quizmaster.util.WebUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

@WebServlet("/student/results/*")
public class StudentResultsServlet extends BaseServlet {
    private final ResultDao results = new ResultDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        long userId = currentUser(request).getId();
        String path = request.getPathInfo();
        try {
            if ("/view".equals(path)) {
                Optional<QuizResult> result = results.findOwnedById(
                        WebUtil.longParameter(request, "id"), userId);
                if (result.isEmpty()) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    return;
                }
                request.setAttribute("result", result.get());
                render(request, response, "result-detail.jsp");
            } else {
                request.setAttribute("results", results.findForStudent(userId));
                render(request, response, "results.jsp");
            }
        } catch (SQLException ex) {
            fail(request, response, ex);
        }
    }
}
