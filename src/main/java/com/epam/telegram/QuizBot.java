package com.epam.telegram;

import com.epam.db.service.QuestionService;
import com.epam.db.service.UserSessionService;
import com.epam.db.service.impl.QuestionServiceMockImpl;
import com.epam.db.service.impl.UserSessionServiceMockImpl;
import com.epam.db.service.impl.XmlQuestionServiceMockImpl;
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

import javax.xml.bind.JAXBException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class QuizBot extends TelegramLongPollingBot {

    private String botName;
    private String botToken;

    private QuestionService questionService = new XmlQuestionServiceMockImpl();
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

            if (message_text.equals("/start")) {
                sendHelloForUser(chat_id);
                log.info("New chat with user: " + chat_id);

            } else if (message_text.equals(accept)) {
                log.info("User accepted: " + chat_id);
                UserSession userSession = saveChatIDToDB(chat_id);
                userSession.setLastQuestion(getQuestionForUser(userSession));
                sendQuestionForUser(userSession);
                userSessionService.saveUserSession(userSession);
            } else if (userSessionService.getUserSessionByChatId(chat_id).getCorrectAnswerSum() < 20) {
                UserSession userSession = userSessionService.getUserSessionByChatId(chat_id);
                if (userSession.getLastQuestion().getCorrectAnswer().toLowerCase().equals(message_text.toLowerCase())) {
                    log.info("user give correct answer: " + chat_id);
                    userSession.incNumberOfCorrectAnswer();
                    if (userSession.getCorrectAnswerSum() >= 20) {
                        log.info("user finish quiz: " + chat_id);
                        sendCongratulations(userSession);
                    } else {
                        userSessionService.saveUserSession(userSession);
                        sendCorrectOtNotAnswer(userSession.getChatId(), true);
                        sendResultForUser(userSession);
                        userSession.setLastQuestion(getQuestionForUser(userSession));
                        sendQuestionForUser(userSession);
                        userSessionService.saveUserSession(userSession);
                    }
                } else {
                    log.info("user give incorrect answer: " + chat_id);
                    sendCorrectOtNotAnswer(userSession.getChatId(), false);
                    sendResultForUser(userSession);
                    userSession.setLastQuestion(getQuestionForUser(userSession));
                    sendQuestionForUser(userSession);
                    userSessionService.saveUserSession(userSession);
                }
            }
        }

    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
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

    private void sendHelloForUser(long chatId) {
        SendMessage message = new SendMessage().setChatId(chatId);

        message.setText(getHelloMessage());
        message.setReplyMarkup(getAcceptButton());

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

    private String getCorrectAnswerSumString(Integer correctAnswerSum) {

        return "Твой результат " +
                correctAnswerSum +
                "/20 баллов";
    }

    private void sendQuestionForUser(UserSession userSession) {
        SendMessage message = new SendMessage().setChatId(userSession.getChatId());
        message.setText(userSession.getLastQuestion().getQuestion());

        ReplyKeyboard markup = getAnswerButtons(userSession.getLastQuestion());

        message.setReplyMarkup(markup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private ReplyKeyboard getAnswerButtons(Question question) {

        if (question.getAnswerList() == null) {
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


    private String getHelloMessage() {
        return "Привет! Мы рады приветствовать тебя в нашем квизе :) Тебе предстоит ответить " +
                "на несколько простых вопросов и в конце тебя ждёт награда! Ты готов?";
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

    private UserSession saveChatIDToDB(long chatID) {
        return userSessionService.saveUserSession(new UserSession(chatID));
    }

    private Question getQuestionForUser(UserSession userSession) {
        return questionService.getQuestionWithoutRepetition(userSession.getAnsweredQuiz());
    }
}
