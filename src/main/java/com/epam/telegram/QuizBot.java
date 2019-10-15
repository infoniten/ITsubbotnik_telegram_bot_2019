package com.epam.telegram;

import com.epam.db.service.QuestionService;
import com.epam.db.service.UserSessionService;
import com.epam.db.service.impl.mock.UserSessionServiceMockImpl;
import com.epam.db.service.impl.xml.service.XmlQuestionServiceImpl;
import com.epam.telegram.entity.Question;
import com.epam.telegram.entity.UserSession;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class QuizBot extends TelegramLongPollingBot {

    private static final Integer POINTS_TO_WIN = 20;

    private String botName;
    private String botToken;

    private QuestionService questionService = new XmlQuestionServiceImpl();
    private UserSessionService userSessionService = new UserSessionServiceMockImpl();

    private String accept = "Поехали!\uD83D\uDCAA";

    public QuizBot(String botName, String botToken) {
        this.botName = botName;
        this.botToken = botToken;
    }

    public QuizBot(DefaultBotOptions options, String botName, String botToken) {
        super(options);
        this.botName = botName;
        this.botToken = botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {

            String message_text = update.getMessage().getText();
            long chat_id = update.getMessage().getChatId();

            log.info("Message received: " + chat_id);

            if (message_text.equals("/start") && isNewUser(chat_id)) {
                sendHelloForUser(chat_id, update.getMessage().getFrom().getFirstName());
                log.info("New chat with user: " + chat_id);

            } else if (message_text.equals(accept) && isNewUser(chat_id)) {

                log.info("User accepted: " + chat_id);
                UserSession userSession = saveChatIDToDB(chat_id);
                userSession.setLastQuestion(getQuestionForUser(userSession));
                sendQuestionForUser(userSession);
                userSessionService.saveUserSession(userSession);

            } else if (scoredLessThanNeed(chat_id) && LastQuestionNotNull(chat_id)) {

                UserSession userSession = userSessionService.getUserSessionByChatId(chat_id);

                if (isCorrectAnswer(userSession, message_text)) {

                    log.info("user give correct answer: " + chat_id);
                    userSession.incNumberOfCorrectAnswer();

                    if (!scoredLessThanNeed(chat_id)) {

                        log.info("user finish quiz: " + chat_id);
                        sendCongratulations(userSession);

                    } else {

                        userSessionService.saveUserSession(userSession);
                        sendResultAndNextQuestionWithSaveResults(userSession, true);
                    }

                } else {

                    log.info("user give incorrect answer: " + chat_id);
                    sendResultAndNextQuestionWithSaveResults(userSession, false);

                }
            }
        }
    }

    private Boolean isCorrectAnswer(UserSession userSession, String message_text) {
        return userSession.getLastQuestion().getCorrectAnswer().toLowerCase().equals(message_text.toLowerCase());
    }

    private void sendResultAndNextQuestionWithSaveResults(UserSession userSession, Boolean isCorrectAnswer) {
        sendCorrectOtNotAnswer(userSession.getChatId(), isCorrectAnswer);
        sendResultForUser(userSession);
        userSession.setLastQuestion(getQuestionForUser(userSession));
        sendQuestionForUser(userSession);
        userSessionService.saveUserSession(userSession);
    }

    private Boolean scoredLessThanNeed(Long chat_id) {
        return userSessionService.getUserSessionByChatId(chat_id).getCorrectAnswerSum() < POINTS_TO_WIN;
    }

    private Boolean LastQuestionNotNull(Long chat_id) {
        return userSessionService.getUserSessionByChatId(chat_id).getLastQuestion() != null;
    }

    private Boolean isNewUser(Long chatId) {
        return userSessionService.getUserSessionByChatId(chatId) == null;
    }

    private UserSession saveChatIDToDB(long chatID) {
        return userSessionService.saveUserSession(new UserSession(chatID));
    }

    private Question getQuestionForUser(UserSession userSession) {
        return questionService.getQuestionWithoutRepetition(userSession.getAnsweredQuiz());
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    private void sendHelloForUser(long chatId, String userName) {
        SendMessage message = new SendMessage().setChatId(chatId);

        message.setText(getHelloMessage(userName));
        message.setReplyMarkup(getAcceptButton());

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private String getHelloMessage(String userName) {
        return "Привет, " + userName + "! Мы рады приветствовать тебя в нашем квизе :) Тебе предстоит ответить " +
                "на несколько простых вопросов и в конце тебя ждёт награда! Ты готов?";
    }

    private String getLooserMessage() {

        return "Для тебя закончились вопросы и ты не смог пройти тест :C Пройди в IT-исповедальню " +
                "и отпусти свои грехи";
    }

    private String getCorrectAnswerSumString(Integer correctAnswerSum) {
        return "Твой результат " +
                correctAnswerSum +
                "/" + POINTS_TO_WIN + " баллов";
    }

    private void sendCongratulations(UserSession userSession) {
        SendMessage message = new SendMessage().setChatId(userSession.getChatId());

        message.setText("Поздравляю! Ты прошёл испытание! Подойди к стенду и получи свой приз!");
        ReplyKeyboardRemove keyboardMarkup = new ReplyKeyboardRemove();
        message.setReplyMarkup(keyboardMarkup);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendCorrectOtNotAnswer(Long chatId, Boolean correct) {
        SendMessage message = new SendMessage().setChatId(chatId);

        message.setText(correct ? "Правильно!" : "Не правильно!");

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendResultForUser(UserSession userSession) {
        SendMessage message = new SendMessage().setChatId(userSession.getChatId());

        message.setText(getCorrectAnswerSumString(userSession.getCorrectAnswerSum()));

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendQuestionForUser(UserSession userSession) {
        SendMessage message = new SendMessage().setChatId(userSession.getChatId());
        message.setText(
                userSession.getLastQuestion() == null ? getLooserMessage() : userSession.getLastQuestion().getQuestion()
        );

        ReplyKeyboard markup = getAnswerButtons(userSession.getLastQuestion());

        message.setReplyMarkup(markup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private ReplyKeyboard getAnswerButtons(Question question) {

        if (question == null || question.getAnswerList() == null) {
            return new ReplyKeyboardRemove();
        }
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();
        for (String answer :
                question.getAnswerList()) {
            keyboardRow.add(answer);
        }
        keyboardRows.add(keyboardRow);
        keyboardMarkup.setKeyboard(keyboardRows);

        return keyboardMarkup;
    }

    private ReplyKeyboardMarkup getAcceptButton() {

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add(accept);
        keyboardRows.add(keyboardRow);
        keyboardMarkup.setKeyboard(keyboardRows);

        return keyboardMarkup;
    }
}
