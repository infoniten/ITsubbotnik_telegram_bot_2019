package com.epam.db.service.impl;

import com.epam.db.service.QuestionService;
import com.epam.telegram.entity.Question;
import com.epam.telegram.entity.QuestionsPack;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class XmlQuestionServiceMockImpl implements QuestionService {
    private List<Question> questionList;

    public XmlQuestionServiceMockImpl() {
        this.questionList = initialize();
    }

    @Override
    public Question getQuestionWithoutRepetition(List<Long> ids) {
        Random rnd = new Random(System.currentTimeMillis());
        int index = rnd.nextInt(questionList.size());
        return questionList.get(index);
    }


    private List<Question> initialize() {
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(Objects.requireNonNull(classLoader.getResource("questions.xml")).getFile());

            JAXBContext jaxbContext = JAXBContext.newInstance(QuestionsPack.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            QuestionsPack questionsPack = (QuestionsPack) jaxbUnmarshaller.unmarshal(file);
            return questionsPack.getQuestionList();
        }
        catch (JAXBException | NullPointerException e){
            System.out.println("Error init xml file " + e);
        }
        return new ArrayList<>();
    }
}
