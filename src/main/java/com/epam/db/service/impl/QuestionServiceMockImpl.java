package com.epam.db.service.impl;

import com.epam.db.service.QuestionService;
import com.epam.telegram.entity.Question;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class QuestionServiceMockImpl implements QuestionService {
    private List<Question> questionList;

    public QuestionServiceMockImpl() {
        this.questionList = initialize();
    }

    @Override
    public Question getQuestionWithoutRepetition(List<Long> ids) {
        Random rnd = new Random(System.currentTimeMillis());
        int index = rnd.nextInt(questionList.size());
        return questionList.get(index);
    }


    private List<Question> initialize() {
        List<Question> questions = new ArrayList<>();

        questions.add(Question
                .builder()
                .id(1)
                .question("1 + 1 = ?")
                .correctAnswer("2")
                .answerList(Arrays.asList("3", "5", "6", "2"))
                .build());

        questions.add(Question
                .builder()
                .id(2)
                .question("2 + 2 * 2 = ?")
                .correctAnswer("6")
                .answerList(Arrays.asList("8", "6", "4", "Условия задачи поставлены не корректно, задачу не возможно решить"))
                .build());

        questions.add(Question
                .builder()
                .id(3)
                .question("150 + 150 = ?")
                .correctAnswer("300")
                .answerList(Arrays.asList("Нет", "600", "Я уже решал такие задачи", "300"))
                .build());

        questions.add(Question
                .builder()
                .id(4)
                .question("При входе в кабинет вам бросили книгу по 1С под ноги, ваши действия?")
                .correctAnswer("Перешагну")
                .answerList(Arrays.asList("Подниму", "Перешагну"))
                .build());

        questions.add(Question
                .builder()
                .id(5)
                .question("Этот вопрос проверка на ручной ввод, напиши \"тест\" без кавычек")
                .correctAnswer("тест")
                .answerList(null)
                .build());

        questions.add(Question
                .builder()
                .id(6)
                .question("16 + 5 = ?")
                .correctAnswer("")
                .answerList(Arrays.asList("21", "20", "19", "17"))
                .build());


        return questions;
    }
}
