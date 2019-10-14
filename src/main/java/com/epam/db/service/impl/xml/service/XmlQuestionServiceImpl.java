package com.epam.db.service.impl.xml.service;

import com.epam.db.service.QuestionService;
import com.epam.db.service.impl.xml.entity.XmlQuestion;
import com.epam.db.service.impl.xml.entity.XmlQuestionsPack;
import org.apache.commons.math3.random.RandomDataGenerator;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class XmlQuestionServiceImpl implements QuestionService {
    private List<XmlQuestion> questionList;

    public XmlQuestionServiceImpl() {
        this.questionList = initialize();
    }

    @Override
    public XmlQuestion getQuestionWithoutRepetition(List<Integer> ids) {
        if (ids.size() == questionList.size()) {
            return null;
        }
        RandomDataGenerator rnd = new RandomDataGenerator();
        Integer index = rnd.nextInt(0, questionList.size());
        for (; ids.contains(index); index = rnd.nextInt(0, questionList.size() - 1)) ;
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
