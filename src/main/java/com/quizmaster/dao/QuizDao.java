package com.quizmaster.dao;

import com.quizmaster.model.Quiz;
import com.quizmaster.util.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class QuizDao {
    private static final String SELECT_WITH_COUNT =
            "SELECT q.id, q.title, q.description, q.time_limit_minutes, q.is_published, " +
            "COUNT(question.id) AS question_count FROM quizzes q " +
            "LEFT JOIN questions question ON question.quiz_id = q.id ";

    private static Quiz map(ResultSet rs) throws SQLException {
        return new Quiz(rs.getLong("id"), rs.getString("title"), rs.getString("description"),
                rs.getInt("time_limit_minutes"), rs.getBoolean("is_published"),
                rs.getInt("question_count"));
    }

    public List<Quiz> findPublished() throws SQLException {
        String sql = SELECT_WITH_COUNT + "WHERE q.is_published = TRUE " +
                "GROUP BY q.id ORDER BY q.created_at DESC";
        return queryList(sql);
    }

    public List<Quiz> findAll() throws SQLException {
        return queryList(SELECT_WITH_COUNT + "GROUP BY q.id ORDER BY q.created_at DESC");
    }

    private List<Quiz> queryList(String sql) throws SQLException {
        List<Quiz> quizzes = new ArrayList<>();
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) quizzes.add(map(rs));
        }
        return quizzes;
    }

    public Optional<Quiz> findById(long id) throws SQLException {
        String sql = SELECT_WITH_COUNT +
                "WHERE q.id = ? GROUP BY q.id";
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        }
    }

    public Optional<Quiz> findPublishedById(long id) throws SQLException {
        String sql = SELECT_WITH_COUNT +
                "WHERE q.id = ? AND q.is_published = TRUE GROUP BY q.id";
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        }
    }

    public void save(Quiz quiz) throws SQLException {
        if (quiz.getId() == 0) {
            String sql = "INSERT INTO quizzes (title, description, time_limit_minutes, is_published) " +
                    "VALUES (?, ?, ?, ?)";
            try (Connection connection = Database.getConnection();
                 PreparedStatement statement = connection.prepareStatement(sql)) {
                bind(statement, quiz);
                statement.executeUpdate();
            }
        } else {
            String sql = "UPDATE quizzes SET title = ?, description = ?, time_limit_minutes = ?, " +
                    "is_published = ? WHERE id = ?";
            try (Connection connection = Database.getConnection();
                 PreparedStatement statement = connection.prepareStatement(sql)) {
                bind(statement, quiz);
                statement.setLong(5, quiz.getId());
                if (statement.executeUpdate() != 1) throw new SQLException("Quiz was not found.");
            }
        }
    }

    private void bind(PreparedStatement statement, Quiz quiz) throws SQLException {
        statement.setString(1, quiz.getTitle());
        statement.setString(2, quiz.getDescription());
        statement.setInt(3, quiz.getTimeLimitMinutes());
        statement.setBoolean(4, quiz.isPublished());
    }

    public void delete(long id) throws SQLException {
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM quizzes WHERE id = ?")) {
            statement.setLong(1, id);
            if (statement.executeUpdate() != 1) throw new SQLException("Quiz was not found.");
        }
    }

    public long countAll() throws SQLException {
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM quizzes");
             ResultSet rs = statement.executeQuery()) {
            rs.next();
            return rs.getLong(1);
        }
    }
}
