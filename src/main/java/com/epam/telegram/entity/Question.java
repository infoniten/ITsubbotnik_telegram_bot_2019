package com.epam.telegram.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Data
@NoArgsConstructor
public class Question {
    protected long id;
    protected String question;
    protected List<String> answerList;
    protected String correctAnswer;

    @Builder
    public Question(long id, String question, List<String> answerList, String correctAnswer) {
        this.id = id;
        this.question = question;
        this.answerList = answerList;
        this.correctAnswer = correctAnswer;
    }
}
