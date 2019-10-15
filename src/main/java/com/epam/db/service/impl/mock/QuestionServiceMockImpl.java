package com.epam.db.service.impl.mock;

import com.epam.db.service.QuestionService;
import com.epam.telegram.entity.Question;

import java.util.*;

public class QuestionServiceMockImpl implements QuestionService {
    private List<Question> questionList;

    public QuestionServiceMockImpl() {
        this.questionList = initialize();
    }

    @Override
    public Question getQuestionWithoutRepetition(HashSet<Question> ids) {
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
                .correctAnswer("21")
                .answerList(Arrays.asList("21", "20", "19", "17"))
                .build());

        questions.add(Question
                .builder()
                .id(7)
                .question("Какова доля азота в атмосфере?")
                .correctAnswer("78%")
                .answerList(Arrays.asList("12%", "78%", "31%", "96%"))
                .build());

        questions.add(Question
                .builder()
                .id(8)
                .question("Назовите часть речи слова «Привет»")
                .correctAnswer("Междометие")
                .answerList(Arrays.asList("Междометие", "Существительное", "Предлог", "Огурец"))
                .build());

        questions.add(Question
                .builder()
                .id(9)
                .question("Чему приблизительно ровно число «Пи»")
                .correctAnswer("3,14")
                .answerList(Arrays.asList("3,14", "3,145", "3,12", "3,41"))
                .build());

        questions.add(Question
                .builder()
                .id(10)
                .question("Сколько материков на планете Земля")
                .correctAnswer("6")
                .answerList(Arrays.asList("6", "5", "7", "1"))
                .build());

        questions.add(Question
                .builder()
                .id(11)
                .question("На каком уроке проходят правило буравчика")
                .correctAnswer("Физика")
                .answerList(Arrays.asList("Алгебра", "Труды", "Физкультура", "Физика"))
                .build());

        questions.add(Question
                .builder()
                .id(12)
                .question("В каком году отменили крепостное право")
                .correctAnswer("1861")
                .answerList(Arrays.asList("1943", "1811", "1861", "1921"))
                .build());

        questions.add(Question
                .builder()
                .id(13)
                .question("Во сколько лет умер Ленин")
                .correctAnswer("53")
                .answerList(Arrays.asList("33", "61", "57", "53"))
                .build());

        questions.add(Question
                .builder()
                .id(14)
                .question("Карточка стандартной формы для записи электронной информации при помощи кодового расположения отверстий")
                .correctAnswer("Перфокарта")
                .answerList(Arrays.asList("Блокнот", "Пластинка", "Перфокарта", "Ключ Шмелевского"))
                .build());

        questions.add(Question
                .builder()
                .id(15)
                .question("индийский политический и общественный деятель, один из руководителей и идеологов движения за независимость Индии от Великобритании")
                .correctAnswer("Махатма Ганди")
                .answerList(Arrays.asList("Имхотеб Махтанг", "Гиоргян Местов", "Махатма Ганди", "Валерий Хмарых"))
                .build());
        questions.add(Question
                .builder()
                .id(16)
                .question("Еденица измерения силы тока")
                .correctAnswer("Ампер")
                .answerList(Arrays.asList("Грут", "Ватт", "Тор", "Ампер"))
                .build());

        questions.add(Question
                .builder()
                .id(17)
                .question("Сатурн какая по счету планета от солнца")
                .correctAnswer("6")
                .answerList(Arrays.asList("6", "2", "3", "5"))
                .build());

        questions.add(Question
                .builder()
                .id(18)
                .question("Сколько будет 0.2 км в дециметрах")
                .correctAnswer("2000")
                .answerList(Arrays.asList("200", "20000", "20", "2000"))
                .build());

        questions.add(Question
                .builder()
                .id(19)
                .question("Какое число обозначается римскими цифрами LXXVII")
                .correctAnswer("77")
                .answerList(Arrays.asList("223", "91", "162", "77"))
                .build());

        questions.add(Question
                .builder()
                .id(20)
                .question("Зеленый пигмент окрашивающий листья растений")
                .correctAnswer("Хлорофилл")
                .answerList(Arrays.asList("Ингалипт", "Хлорофилл", "Хлоропласт", "Пенополистирол"))
                .build());

        return questions;
    }
}
