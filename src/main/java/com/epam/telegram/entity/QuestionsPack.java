package com.epam.telegram.entity;

import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@NoArgsConstructor
@XmlRootElement
@Builder
public class QuestionsPack {
    List<Question> questionList;

    public QuestionsPack(List<Question> questionList) {
        this.questionList = questionList;
    }
    @XmlElementWrapper(name = "answersList")
    @XmlElement(name = "element")
    public List<Question> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(List<Question> questionList) {
        this.questionList = questionList;
    }

    @Override
    public String toString() {
        return "QuestionsList{" +
                "questionList=" + questionList +
                '}';
    }
}
