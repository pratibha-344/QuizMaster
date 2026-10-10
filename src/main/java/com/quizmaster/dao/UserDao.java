package com.quizmaster.dao;

import com.quizmaster.model.User;
import com.quizmaster.util.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDao {
    private static User map(ResultSet rs) throws SQLException {
        return new User(rs.getLong("id"), rs.getString("full_name"),
                rs.getString("email"), rs.getString("role"));
    }

    public Optional<User> findByEmail(String email) throws SQLException {
        String sql = "SELECT id, full_name, email, role FROM users WHERE email = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        }
    }

    public Optional<String> findPasswordHashByEmail(String email) throws SQLException {
        String sql = "SELECT password_hash FROM users WHERE email = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next() ? Optional.of(rs.getString("password_hash")) : Optional.empty();
            }
        }
    }

    public Optional<User> findById(long id) throws SQLException {
        String sql = "SELECT id, full_name, email, role FROM users WHERE id = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        }
    }

    public long createStudent(String fullName, String email, String passwordHash) throws SQLException {
        String sql = "INSERT INTO users (full_name, email, password_hash, role) VALUES (?, ?, ?, 'STUDENT')";
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, fullName);
            statement.setString(2, email);
            statement.setString(3, passwordHash);
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (!keys.next()) throw new SQLException("Creating the account did not return an ID.");
                return keys.getLong(1);
            }
        }
    }

    public void updateProfile(long userId, String fullName, String email) throws SQLException {
        String sql = "UPDATE users SET full_name = ?, email = ? WHERE id = ? AND role = 'STUDENT'";
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, fullName);
            statement.setString(2, email);
            statement.setLong(3, userId);
            if (statement.executeUpdate() != 1) throw new SQLException("Student profile was not found.");
        }
    }

    public List<User> listStudents() throws SQLException {
        String sql = "SELECT id, full_name, email, role FROM users WHERE role = 'STUDENT' ORDER BY full_name";
        List<User> students = new ArrayList<>();
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) students.add(map(rs));
        }
        return students;
    }

    public long countStudents() throws SQLException {
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT COUNT(*) FROM users WHERE role = 'STUDENT'");
             ResultSet rs = statement.executeQuery()) {
            rs.next();
            return rs.getLong(1);
        }
    }
}
