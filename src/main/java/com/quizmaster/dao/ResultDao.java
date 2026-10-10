package com.quizmaster.dao;

import com.quizmaster.model.LeaderboardEntry;
import com.quizmaster.model.QuizResult;
import com.quizmaster.util.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ResultDao {

private static final String RESULT_COLUMNS =
        "SELECT r.id, r.user_id, r.quiz_id, u.full_name AS student_name, q.title AS quiz_title, " +
        "r.score, r.total_questions, r.duration_seconds, r.submitted_at ";
    private static final String RESULT_FROM =
            "FROM results r JOIN users u ON u.id = r.user_id JOIN quizzes q ON q.id = r.quiz_id ";

    public long create(long userId, long quizId, int score, int total, int durationSeconds)
            throws SQLException {
        String sql = "INSERT INTO results (user_id, quiz_id, score, total_questions, duration_seconds) " +
                "VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, userId);
            statement.setLong(2, quizId);
            statement.setInt(3, score);
            statement.setInt(4, total);
            statement.setInt(5, durationSeconds);
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (!keys.next()) throw new SQLException("Saving the result did not return an ID.");
                return keys.getLong(1);
            }
        }
    }

    public List<QuizResult> findForStudent(long userId) throws SQLException {
        if (!hasRows("SELECT COUNT(*) FROM results WHERE user_id = ?", userId)) {
            return List.of();
        }
        return query(RESULT_COLUMNS + RESULT_FROM +
                "WHERE r.user_id = ? ORDER BY r.submitted_at DESC", userId);
    }

    public Optional<QuizResult> findOwnedById(long resultId, long userId) throws SQLException {
        if (!hasRows("SELECT COUNT(*) FROM results WHERE id = ? AND user_id = ?", resultId, userId)) {
            return Optional.empty();
        }
        List<QuizResult> rows = query(RESULT_COLUMNS + RESULT_FROM +
                "WHERE r.id = ? AND r.user_id = ?", resultId, userId);
        return rows.stream().findFirst();
    }

    public List<QuizResult> findAll() throws SQLException {
        if (!hasRows("SELECT COUNT(*) FROM results")) {
            return List.of();
        }
        return query(RESULT_COLUMNS + RESULT_FROM + "ORDER BY r.submitted_at DESC LIMIT 300");
    }

    private List<QuizResult> query(String sql, long... parameters) throws SQLException {
        List<QuizResult> results = new ArrayList<>();
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            for (int i = 0; i < parameters.length; i++) statement.setLong(i + 1, parameters[i]);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    results.add(new QuizResult(rs.getLong("id"), rs.getLong("user_id"),
                            rs.getLong("quiz_id"), rs.getString("student_name"),
                            rs.getString("quiz_title"), rs.getInt("score"),
                            rs.getInt("total_questions"), rs.getInt("duration_seconds"),
                            rs.getTimestamp("submitted_at")));
                }
            }
        }
        return results;
    }

    private boolean hasRows(String sql, long... parameters) throws SQLException {
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            for (int i = 0; i < parameters.length; i++) statement.setLong(i + 1, parameters[i]);
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next() && rs.getLong(1) > 0;
            }
        }
    }

    public List<LeaderboardEntry> leaderboard() throws SQLException {
        String sql = "SELECT u.id AS user_id, u.full_name, COUNT(*) AS attempts, " +
                "ROUND(AVG(r.score * 100.0 / NULLIF(r.total_questions, 0)), 1) AS average_percentage, " +
                "ROUND(MAX(r.score * 100.0 / NULLIF(r.total_questions, 0)), 1) AS best_percentage " +
                "FROM results r JOIN users u ON u.id = r.user_id WHERE u.role = 'STUDENT' " +
                "GROUP BY u.id, u.full_name ORDER BY average_percentage DESC, best_percentage DESC, " +
                "attempts DESC, u.full_name ASC LIMIT 50";
        List<LeaderboardEntry> entries = new ArrayList<>();
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                entries.add(new LeaderboardEntry(rs.getLong("user_id"), rs.getString("full_name"),
                        rs.getLong("attempts"), rs.getDouble("average_percentage"),
                        rs.getDouble("best_percentage")));
            }
        }
        return entries;
    }

    public long countAll() throws SQLException {
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM results");
             ResultSet rs = statement.executeQuery()) {
            rs.next();
            return rs.getLong(1);
        }
    }

    public double averageForStudent(long userId) throws SQLException {
        String sql = "SELECT COALESCE(AVG(score * 100.0 / NULLIF(total_questions, 0)), 0) " +
                "FROM results WHERE user_id = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, userId);
            try (ResultSet rs = statement.executeQuery()) {
                rs.next();
                return rs.getDouble(1);
            }
        }
    }
}
