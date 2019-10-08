package com.epam.xml.service;

import com.epam.db.service.QuestionService;
import com.epam.xml.entity.XmlQuestionsPack;
import com.epam.xml.entity.XmlQuestion;
import org.jvnet.hk2.annotations.Service;
import org.springframework.context.annotation.Primary;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class XmlQuestionServiceImpl implements QuestionService {
    private List<XmlQuestion> questionList;

    public XmlQuestionServiceImpl() {
        this.questionList = initialize();
    }

    @Override
    public XmlQuestion getQuestionWithoutRepetition(List<Long> ids) {
        Random rnd = new Random(System.currentTimeMillis());
        int index = rnd.nextInt(questionList.size());
        return questionList.get(index);
    }
    private List<XmlQuestion> initialize() {
        try {
            ClassLoader classLoader = getClass().getClassLoader();
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
}
