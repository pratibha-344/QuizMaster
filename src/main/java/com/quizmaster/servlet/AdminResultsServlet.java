package com.quizmaster.servlet;

import com.quizmaster.dao.ResultDao;
import com.quizmaster.util.BaseServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/admin/results")
public class AdminResultsServlet extends BaseServlet {
    private final ResultDao results = new ResultDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setAttribute("results", results.findAll());
            render(request, response, "admin-results.jsp");
        } catch (SQLException ex) {
            fail(request, response, ex);
        }
    }
}
