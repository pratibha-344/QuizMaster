package com.quizmaster.model;

public class Question {
    private final long id;
    private final long quizId;
    private final String text;
    private final String optionA;
    private final String optionB;
    private final String optionC;
    private final String optionD;
    private final String correctOption;

    public Question(long id, long quizId, String text, String optionA, String optionB,
                    String optionC, String optionD, String correctOption) {
        this.id = id;
        this.quizId = quizId;
        this.text = text;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.correctOption = correctOption;
    }

    public long getId() { return id; }
    public long getQuizId() { return quizId; }
    public String getText() { return text; }
    public String getOptionA() { return optionA; }
    public String getOptionB() { return optionB; }
    public String getOptionC() { return optionC; }
    public String getOptionD() { return optionD; }
    public String getCorrectOption() { return correctOption; }
}
