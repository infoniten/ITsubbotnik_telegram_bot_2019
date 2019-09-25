package com.epam.telegram.entity;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class Question {
    private long id;
    private String question;
    private List<String> answerList;
    private String correctAnswer;

    @Builder
    public Question(long id, String question, List<String> answerList, String correctAnswer) {
        this.id = id;
        this.question = question;
        this.answerList = answerList;
        this.correctAnswer = correctAnswer;
    }
}
