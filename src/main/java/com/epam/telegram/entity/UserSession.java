package com.epam.telegram.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class UserSession {
    @Getter
    private long chatId;
    @Getter
    private Integer correctAnswerSum;
    @Getter
    private List<Integer> answeredQuiz;
    @Getter
    @Setter
    private Question lastQuestion;

    public UserSession(long chatId) {
        this.chatId = chatId;
        this.correctAnswerSum = 0;
        this.answeredQuiz = new ArrayList<>();
    }

    public void incNumberOfCorrectAnswer() {
        this.correctAnswerSum += 1;
    }

    public void addIdAnswer(Integer id) {
        answeredQuiz.add(id);
    }

    public void setLastQuestion(Question question) {
        addIdAnswer(question.id);
        this.lastQuestion = question;
    }
}

