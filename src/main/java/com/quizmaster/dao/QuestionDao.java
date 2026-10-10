package com.quizmaster.dao;

import com.quizmaster.model.Question;
import com.quizmaster.util.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionDao {
    public List<Question> findForStudent(long quizId) throws SQLException {
        return findByQuiz(quizId, false);
    }

    public List<Question> findForAdmin(long quizId) throws SQLException {
        return findByQuiz(quizId, true);
    }

    private List<Question> findByQuiz(long quizId, boolean includeAnswer) throws SQLException {
        String answerField = includeAnswer ? "correct_option" : "'' AS correct_option";
        String sql = "SELECT id, quiz_id, question_text, option_a, option_b, option_c, option_d, " +
                answerField + " FROM questions WHERE quiz_id = ? ORDER BY id";
        List<Question> questions = new ArrayList<>();
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, quizId);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    questions.add(new Question(rs.getLong("id"), rs.getLong("quiz_id"),
                            rs.getString("question_text"), rs.getString("option_a"),
                            rs.getString("option_b"), rs.getString("option_c"),
                            rs.getString("option_d"), rs.getString("correct_option")));
                }
            }
        }
        return questions;
    }

    public void save(Question question) throws SQLException {
        if (question.getId() == 0) {
            String sql = "INSERT INTO questions (quiz_id, question_text, option_a, option_b, " +
                    "option_c, option_d, correct_option) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (Connection connection = Database.getConnection();
                 PreparedStatement statement = connection.prepareStatement(sql)) {
                bind(statement, question);
                statement.executeUpdate();
            }
        } else {
            String sql = "UPDATE questions SET quiz_id = ?, question_text = ?, option_a = ?, " +
                    "option_b = ?, option_c = ?, option_d = ?, correct_option = ? WHERE id = ?";
            try (Connection connection = Database.getConnection();
                 PreparedStatement statement = connection.prepareStatement(sql)) {
                bind(statement, question);
                statement.setLong(8, question.getId());
                if (statement.executeUpdate() != 1) throw new SQLException("Question was not found.");
            }
        }
    }

    private void bind(PreparedStatement statement, Question question) throws SQLException {
        statement.setLong(1, question.getQuizId());
        statement.setString(2, question.getText());
        statement.setString(3, question.getOptionA());
        statement.setString(4, question.getOptionB());
        statement.setString(5, question.getOptionC());
        statement.setString(6, question.getOptionD());
        statement.setString(7, question.getCorrectOption());
    }

    public void delete(long id) throws SQLException {
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM questions WHERE id = ?")) {
            statement.setLong(1, id);
            if (statement.executeUpdate() != 1) throw new SQLException("Question was not found.");
        }
    }
}
