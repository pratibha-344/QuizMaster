package com.quizmaster.util;

import com.quizmaster.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public abstract class BaseServlet extends HttpServlet {
    protected void render(HttpServletRequest request, HttpServletResponse response, String view)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        request.setAttribute("currentUser", session.getAttribute("authUser"));
        request.setAttribute("csrfToken", session.getAttribute("csrfToken"));
        request.setAttribute("flashType", session.getAttribute("flashType"));
        request.setAttribute("flashMessage", session.getAttribute("flashMessage"));
        session.removeAttribute("flashType");
        session.removeAttribute("flashMessage");
        request.getRequestDispatcher("/WEB-INF/views/" + view).forward(request, response);
    }

    protected void fail(HttpServletRequest request, HttpServletResponse response,
                        Exception exception) throws ServletException, IOException {
        getServletContext().log("QuizMaster request failed.", exception);
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        request.setAttribute("errorMessage",
                "QuizMaster could not complete that request. Check that MySQL is running and the database has been set up. See README.md for help.");
        render(request, response, "error.jsp");
    }

    protected void redirect(HttpServletRequest request, HttpServletResponse response, String path)
            throws IOException {
        response.sendRedirect(request.getContextPath() + path);
    }

    protected User currentUser(HttpServletRequest request) {
        Object value = request.getSession().getAttribute("authUser");
        return value instanceof User user ? user : null;
    }
}
