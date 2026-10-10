package com.quizmaster.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public final class WebUtil {
    private WebUtil() {}

    public static long longParameter(HttpServletRequest request, String name) {
        try {
            return Long.parseLong(request.getParameter(name));
        } catch (NumberFormatException | NullPointerException ex) {
            return -1;
        }
    }

    public static int intParameter(HttpServletRequest request, String name, int fallback) {
        try {
            return Integer.parseInt(request.getParameter(name));
        } catch (NumberFormatException | NullPointerException ex) {
            return fallback;
        }
    }

    public static String text(HttpServletRequest request, String name) {
        String value = request.getParameter(name);
        return value == null ? "" : value.trim();
    }

    public static void flash(HttpServletRequest request, String type, String message) {
        HttpSession session = request.getSession();
        session.setAttribute("flashType", type);
        session.setAttribute("flashMessage", message);
    }
}
