package com.quizmaster.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Base64;

@WebFilter("/*")
public class CsrfFilter implements Filter {
    private static final SecureRandom RANDOM = new SecureRandom();

    @Override
    public void doFilter(ServletRequest incoming, ServletResponse outgoing, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) incoming;
        HttpServletResponse response = (HttpServletResponse) outgoing;
        HttpSession session = request.getSession(true);
        String token = (String) session.getAttribute("csrfToken");
        if (token == null) {
            byte[] bytes = new byte[32];
            RANDOM.nextBytes(bytes);
            token = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
            session.setAttribute("csrfToken", token);
        }

        String method = request.getMethod();
        if (!"GET".equalsIgnoreCase(method) && !"HEAD".equalsIgnoreCase(method)
                && !"OPTIONS".equalsIgnoreCase(method)) {
            String submitted = request.getParameter("_csrf");
            if (submitted == null || !constantTimeEquals(token, submitted)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "The form expired. Reload and try again.");
                return;
            }
        }
        chain.doFilter(request, response);
    }

    private boolean constantTimeEquals(String expected, String submitted) {
        return java.security.MessageDigest.isEqual(
                expected.getBytes(java.nio.charset.StandardCharsets.UTF_8),
                submitted.getBytes(java.nio.charset.StandardCharsets.UTF_8));
    }
}
