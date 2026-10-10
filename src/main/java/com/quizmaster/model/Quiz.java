package com.quizmaster.model;

public class Quiz {
    private final long id;
    private final String title;
    private final String description;
    private final int timeLimitMinutes;
    private final boolean published;
    private final int questionCount;

    public Quiz(long id, String title, String description, int timeLimitMinutes,
                boolean published, int questionCount) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.timeLimitMinutes = timeLimitMinutes;
        this.published = published;
        this.questionCount = questionCount;
    }

    public long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public int getTimeLimitMinutes() { return timeLimitMinutes; }
    public boolean isPublished() { return published; }
    public int getQuestionCount() { return questionCount; }
}
