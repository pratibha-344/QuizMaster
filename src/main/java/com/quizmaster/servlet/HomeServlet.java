package com.quizmaster.servlet;

import com.quizmaster.model.User;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.quizmaster.util.BaseServlet;

import java.io.IOException;

@WebServlet("")
public class HomeServlet extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Object value = request.getSession().getAttribute("authUser");
        if (value instanceof User user) {
            redirect(request, response, user.isAdmin() ? "/admin/dashboard" : "/student/dashboard");
        } else {
            redirect(request, response, "/login");
        }
    }
}
