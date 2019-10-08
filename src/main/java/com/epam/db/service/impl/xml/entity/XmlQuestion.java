package com.epam.db.service.impl.xml.entity;

import com.epam.telegram.entity.Question;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
@NoArgsConstructor
public class XmlQuestion extends Question {
    @Builder(builderMethodName = "childBuilder")
    public XmlQuestion(long id, String question, List<String> answerList, String correctAnswer) {
        super(id, question, answerList, correctAnswer);
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
