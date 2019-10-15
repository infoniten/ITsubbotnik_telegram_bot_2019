package com.epam.telegram.entity;

import lombok.Getter;

import java.util.HashSet;

public class UserSession {
    @Getter
    private long chatId;
    @Getter
    private Integer correctAnswerSum;
    @Getter
    private HashSet<Question> answeredQuiz;
    @Getter
    private Question lastQuestion;

    public UserSession(long chatId) {
        this.chatId = chatId;
        this.correctAnswerSum = 0;
        this.answeredQuiz = new HashSet<>();
    }

    public void incNumberOfCorrectAnswer() {
        this.correctAnswerSum += 1;
    }

    private void addIdAnswer(Question question) {
        answeredQuiz.add(question);
    }

    public void setLastQuestion(Question question) {
        if (question != null) {
            addIdAnswer(question);
        }
        this.lastQuestion = question;
    }
}

