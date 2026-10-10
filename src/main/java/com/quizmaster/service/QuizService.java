package com.quizmaster.service;

import com.quizmaster.dao.QuestionDao;
import com.quizmaster.dao.QuizDao;
import com.quizmaster.dao.ResultDao;
import com.quizmaster.model.Question;
import com.quizmaster.model.Quiz;
import com.quizmaster.model.QuizResult;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class QuizService {
    private final QuizDao quizzes;
    private final QuestionDao questions;
    private final ResultDao results;

    public QuizService(QuizDao quizzes, QuestionDao questions, ResultDao results) {
        this.quizzes = quizzes;
        this.questions = questions;
        this.results = results;
    }

    public QuizResult submit(long studentId, long quizId, Map<Long, String> answers,
                             long startedAtMillis, long submittedAtMillis) throws SQLException {
        Quiz quiz = quizzes.findPublishedById(quizId)
                .orElseThrow(() -> new IllegalArgumentException("That quiz is no longer available."));
        List<Question> answerKey = questions.findForAdmin(quizId);
        if (answerKey.isEmpty()) throw new IllegalArgumentException("This quiz has no questions.");

        long allowedSeconds = quiz.getTimeLimitMinutes() * 60L;
        long elapsedSeconds = Math.max(0, (submittedAtMillis - startedAtMillis) / 1000L);
        int score = 0;
        if (elapsedSeconds <= allowedSeconds) {
            for (Question question : answerKey) {
                String selected = answers.get(question.getId());
                if (selected != null && selected.equalsIgnoreCase(question.getCorrectOption())) score++;
            }
        }
        int duration = (int) Math.min(allowedSeconds, elapsedSeconds);
        long resultId = results.create(studentId, quizId, score, answerKey.size(), duration);
        return results.findOwnedById(resultId, studentId)
                .orElseThrow(() -> new SQLException("The saved quiz result could not be loaded."));
    }

    public Optional<Quiz> publishedQuiz(long quizId) throws SQLException {
        return quizzes.findPublishedById(quizId);
    }

    public List<Question> studentQuestions(long quizId) throws SQLException {
        return questions.findForStudent(quizId);
    }

    public List<Quiz> publishedQuizzes() throws SQLException {
        return quizzes.findPublished();
    }
}
