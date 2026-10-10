package com.quizmaster.filter;

import com.quizmaster.model.User;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter(urlPatterns = {"/student/*", "/admin/*"})
public class RoleFilter implements Filter {
    @Override
    public void doFilter(ServletRequest incoming, ServletResponse outgoing, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) incoming;
        HttpServletResponse response = (HttpServletResponse) outgoing;
        Object value = request.getSession().getAttribute("authUser");
        if (!(value instanceof User user)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        boolean adminPath = request.getRequestURI().contains("/admin/");
        if (adminPath != user.isAdmin()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "You do not have access to this page.");
            return;
        }
        request.setAttribute("currentUser", user);
        chain.doFilter(request, response);
    }
}
