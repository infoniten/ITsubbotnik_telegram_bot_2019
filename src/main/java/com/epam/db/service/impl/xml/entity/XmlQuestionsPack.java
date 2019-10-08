package com.epam.db.service.impl.xml.entity;

import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@NoArgsConstructor
@XmlRootElement
@Builder
public class XmlQuestionsPack {
    List<XmlQuestion> questionList;

    public XmlQuestionsPack(List<XmlQuestion> questionList) {
        this.questionList = questionList;
    }
    @XmlElementWrapper(name = "answersList")
    @XmlElement(name = "element")
    public List<XmlQuestion> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(List<XmlQuestion> questionList) {
        this.questionList = questionList;
    }

    @Override
    public String toString() {
        return "QuestionsList{" +
                "questionList=" + questionList +
                '}';
    }
}
