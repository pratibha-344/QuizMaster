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

@WebServlet("/student/profile")
public class StudentProfileServlet extends BaseServlet {
    private final AuthService auth = new AuthService(new UserDao());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        render(request, response, "profile.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            User updated = auth.updateProfile(currentUser(request).getId(),
                    request.getParameter("fullName"), request.getParameter("email"));
            request.getSession().setAttribute("authUser", updated);
            request.setAttribute("profileMessage", "Your profile has been updated.");
            render(request, response, "profile.jsp");
        } catch (IllegalArgumentException ex) {
            request.setAttribute("formError", ex.getMessage());
            render(request, response, "profile.jsp");
        } catch (SQLException ex) {
            if (ex.getErrorCode() == 1062) {
                request.setAttribute("formError", "That email address is already in use.");
                render(request, response, "profile.jsp");
            } else {
                fail(request, response, ex);
            }
        }
    }
}
