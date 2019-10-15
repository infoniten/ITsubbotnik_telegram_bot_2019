package com.epam.telegram.entity;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode
public class Question {
    protected int id;
    protected String question;
    protected List<String> answerList;
    protected String correctAnswer;

    @Builder
    public Question(int id, String question, List<String> answerList, String correctAnswer) {
        this.id = id;
        this.question = question;
        this.answerList = answerList;
        this.correctAnswer = correctAnswer;
    }
}
