package com.quizmaster.model;

import java.sql.Timestamp;

public class QuizResult {
    private final long id;
    private final long userId;
    private final long quizId;
    private final String studentName;
    private final String quizTitle;
    private final int score;
    private final int totalQuestions;
    private final int durationSeconds;
    private final Timestamp submittedAt;

    public QuizResult(long id, long userId, long quizId, String studentName, String quizTitle,
                      int score, int totalQuestions, int durationSeconds, Timestamp submittedAt) {
        this.id = id;
        this.userId = userId;
        this.quizId = quizId;
        this.studentName = studentName;
        this.quizTitle = quizTitle;
        this.score = score;
        this.totalQuestions = totalQuestions;
        this.durationSeconds = durationSeconds;
        this.submittedAt = submittedAt;
    }

    public long getId() { return id; }
    public long getUserId() { return userId; }
    public long getQuizId() { return quizId; }
    public String getStudentName() { return studentName; }
    public String getQuizTitle() { return quizTitle; }
    public int getScore() { return score; }
    public int getTotalQuestions() { return totalQuestions; }
    public int getDurationSeconds() { return durationSeconds; }
    public Timestamp getSubmittedAt() { return submittedAt; }
    public double getPercentage() {
        return totalQuestions == 0 ? 0 : Math.round(score * 1000.0 / totalQuestions) / 10.0;
    }
}
