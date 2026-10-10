package com.quizmaster.model;

public class LeaderboardEntry {
    private final long userId;
    private final String studentName;
    private final long attempts;
    private final double averagePercentage;
    private final double bestPercentage;

    public LeaderboardEntry(long userId, String studentName, long attempts,
                            double averagePercentage, double bestPercentage) {
        this.userId = userId;
        this.studentName = studentName;
        this.attempts = attempts;
        this.averagePercentage = averagePercentage;
        this.bestPercentage = bestPercentage;
    }

    public long getUserId() { return userId; }
    public String getStudentName() { return studentName; }
    public long getAttempts() { return attempts; }
    public double getAveragePercentage() { return averagePercentage; }
    public double getBestPercentage() { return bestPercentage; }
}
