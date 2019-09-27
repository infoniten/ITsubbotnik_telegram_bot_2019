package com.epam.telegram.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
@NoArgsConstructor
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    @XmlElementWrapper(name = "answersListOf")
    @XmlElement(name = "element")
    public List<String> getAnswerList() {
        return answerList;
    }

    public void setAnswerList(List<String> answerList) {
        this.answerList = answerList;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", question='" + question + '\'' +
                ", answerList=" + answerList +
                ", correctAnswer='" + correctAnswer + '\'' +
                '}';
    }
}
