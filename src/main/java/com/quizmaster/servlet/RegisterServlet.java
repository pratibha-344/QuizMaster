package com.quizmaster.servlet;

import com.quizmaster.dao.UserDao;
import com.quizmaster.model.User;
import com.quizmaster.service.AuthService;
import com.quizmaster.util.BaseServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/register")
public class RegisterServlet extends BaseServlet {
    private final AuthService auth = new AuthService(new UserDao());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        render(request, response, "register.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        try {
            User user = auth.register(request.getParameter("fullName"), request.getParameter("email"),
                    request.getParameter("password"), request.getParameter("confirmPassword"));
            request.changeSessionId();
            request.getSession().setAttribute("authUser", user);
            redirect(request, response, "/student/dashboard");
        } catch (IllegalArgumentException ex) {
            request.setAttribute("formError", ex.getMessage());
            render(request, response, "register.jsp");
        } catch (SQLException ex) {
            if (ex.getErrorCode() == 1062) {
                request.setAttribute("formError", "An account with that email already exists.");
                render(request, response, "register.jsp");
            } else {
                fail(request, response, ex);
            }
        }
    }
}
