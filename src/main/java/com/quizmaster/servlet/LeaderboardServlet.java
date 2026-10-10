package com.quizmaster.servlet;

import com.quizmaster.dao.ResultDao;
import com.quizmaster.util.BaseServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/student/leaderboard")
public class LeaderboardServlet extends BaseServlet {
    private final ResultDao results = new ResultDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setAttribute("entries", results.leaderboard());
            render(request, response, "leaderboard.jsp");
        } catch (SQLException ex) {
            fail(request, response, ex);
        }
    }
}
