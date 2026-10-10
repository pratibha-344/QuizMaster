package com.quizmaster.servlet;

import com.quizmaster.model.User;
import com.quizmaster.service.AuthService;
import com.quizmaster.dao.UserDao;
import com.quizmaster.util.BaseServlet;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

@WebServlet("/login")
public class LoginServlet extends BaseServlet {
    private final AuthService auth = new AuthService(new UserDao());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, jakarta.servlet.ServletException {
        Object value = request.getSession().getAttribute("authUser");
        if (value instanceof User user) {
            redirect(request, response, user.isAdmin() ? "/admin/dashboard" : "/student/dashboard");
            return;
        }
        render(request, response, "login.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, jakarta.servlet.ServletException {
        try {
            Optional<User> user = auth.login(request.getParameter("email"), request.getParameter("password"));
            if (user.isEmpty()) {
                request.setAttribute("formError", "Email or password is incorrect.");
                render(request, response, "login.jsp");
                return;
            }
            request.changeSessionId();
            request.getSession().setAttribute("authUser", user.get());
            redirect(request, response, user.get().isAdmin() ? "/admin/dashboard" : "/student/dashboard");
        } catch (SQLException ex) {
            fail(request, response, ex);
        }
    }
}
