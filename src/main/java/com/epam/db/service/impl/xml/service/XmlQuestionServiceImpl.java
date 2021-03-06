package com.epam.db.service.impl.xml.service;

import com.epam.db.service.QuestionService;
import com.epam.db.service.impl.xml.entity.XmlQuestion;
import com.epam.db.service.impl.xml.entity.XmlQuestionsPack;
import com.epam.telegram.entity.Question;
import org.apache.commons.math3.random.RandomDataGenerator;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class XmlQuestionServiceImpl implements QuestionService {
    private List<XmlQuestion> questionList;

    public XmlQuestionServiceImpl() {
        this.questionList = initialize();
    }

    @Override
    public XmlQuestion getQuestionWithoutRepetition(HashSet<Question> ids) {
        if (ids.size() >= questionList.size() - 1) {
            return null;
        }

        if (ids.size() < questionList.size() / 2) {
            return getQuestionWithRandomIndexGeneration(ids);
        } else {
            return getQuestionWithSmartIndexGeneration(ids);
        }
    }
    private List<XmlQuestion> initialize() {
        try {
            File file = new File("questions.xml");

            JAXBContext jaxbContext = JAXBContext.newInstance(XmlQuestionsPack.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            XmlQuestionsPack questionsPack = (XmlQuestionsPack) jaxbUnmarshaller.unmarshal(file);
            return questionsPack.getQuestionList();
        }
        catch (JAXBException | NullPointerException e){
            System.out.println("Error init xml file " + e);
        }
        return new ArrayList<>();
    }

    private XmlQuestion getQuestionWithRandomIndexGeneration(HashSet<Question> ids) {
        RandomDataGenerator rnd = new RandomDataGenerator();
        for (int index = getRndInt(rnd); ; index = getRndInt(rnd)) {
            if (!ids.contains(questionList.get(index))) {
                return questionList.get(index);
            }
        }
    }

    private int getRndInt(RandomDataGenerator rnd) {
        return rnd.nextInt(0, questionList.size() - 1);
    }

    private XmlQuestion getQuestionWithSmartIndexGeneration(HashSet<Question> ids) {
        List<XmlQuestion> notSendQuestions = questionList
                .stream()
                .filter(xmlQuestion -> !ids.contains(xmlQuestion))
                .collect(Collectors.toList());

        RandomDataGenerator rnd = new RandomDataGenerator();

        return notSendQuestions.get(rnd.nextInt(0, notSendQuestions.size() - 1));
    }
}
