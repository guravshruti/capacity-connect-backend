package com.capacityconnect.backend.model;

public class QuizResult {
    private int totalQuestions;
    private int correctCount;
    private int score;

    public QuizResult(int totalQuestions, int correctCount) {
        this.totalQuestions = totalQuestions;
        this.correctCount = correctCount;
        this.score = totalQuestions == 0 ? 0 : (correctCount * 100) / totalQuestions;
    }

    public int getTotalQuestions() { return totalQuestions; }
    public int getCorrectCount() { return correctCount; }
    public int getScore() { return score; }
}