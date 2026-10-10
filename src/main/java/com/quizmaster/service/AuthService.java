package com.quizmaster.service;

import com.quizmaster.dao.UserDao;
import com.quizmaster.model.User;
import com.quizmaster.util.PasswordUtil;

import java.sql.SQLException;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Pattern;

public class AuthService {
    private static final Pattern EMAIL = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
    private final UserDao users;

    public AuthService(UserDao users) {
        this.users = users;
    }

    public Optional<User> login(String email, String password) throws SQLException {
        String normalizedEmail = email == null ? "" : email.trim().toLowerCase(Locale.ROOT);
        Optional<String> hash = users.findPasswordHashByEmail(normalizedEmail);
        if (hash.isEmpty() || !PasswordUtil.verify(password, hash.get())) return Optional.empty();
        return users.findByEmail(normalizedEmail);
    }

    public User register(String fullName, String email, String password, String confirmation)
            throws SQLException {
        String name = fullName == null ? "" : fullName.trim();
        String normalizedEmail = email == null ? "" : email.trim().toLowerCase(Locale.ROOT);
        if (name.length() < 2 || name.length() > 120) {
            throw new IllegalArgumentException("Enter a name between 2 and 120 characters.");
        }
        if (normalizedEmail.length() > 190 || !EMAIL.matcher(normalizedEmail).matches()) {
            throw new IllegalArgumentException("Enter a valid email address.");
        }
        if (password == null || password.length() < 8 || password.length() > 128) {
            throw new IllegalArgumentException("Your password must be at least 8 characters long.");
        }
        if (!password.equals(confirmation)) {
            throw new IllegalArgumentException("The passwords do not match.");
        }
        if (users.findByEmail(normalizedEmail).isPresent()) {
            throw new IllegalArgumentException("An account with that email already exists.");
        }
        long id = users.createStudent(name, normalizedEmail, PasswordUtil.hash(password));
        return users.findById(id).orElseThrow(() -> new SQLException("New account could not be loaded."));
    }

    public User updateProfile(long userId, String fullName, String email) throws SQLException {
        String name = fullName == null ? "" : fullName.trim();
        String normalizedEmail = email == null ? "" : email.trim().toLowerCase(Locale.ROOT);
        if (name.length() < 2 || name.length() > 120) {
            throw new IllegalArgumentException("Enter a name between 2 and 120 characters.");
        }
        if (normalizedEmail.length() > 190 || !EMAIL.matcher(normalizedEmail).matches()) {
            throw new IllegalArgumentException("Enter a valid email address.");
        }
        users.updateProfile(userId, name, normalizedEmail);
        return users.findById(userId).orElseThrow(() -> new SQLException("Updated profile could not be loaded."));
    }
}
