package com.epam.telegram;

import com.epam.db.service.QuestionService;
import com.epam.db.service.UserSessionService;
import com.epam.db.service.impl.QuestionServiceMockImpl;
import com.epam.db.service.impl.UserSessionServiceMockImpl;
import com.epam.telegram.entity.Question;
import com.epam.telegram.entity.UserSession;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

public class QuizBot extends TelegramLongPollingBot {

    private String botName;
    private String botToken;

    private QuestionService questionService = new QuestionServiceMockImpl();
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

            if (message_text.equals("/start")) {
                sendHelloForUser(chat_id);

            } else if (message_text.equals(accept)) {
                UserSession userSession = saveChatIDToDB(chat_id);
                userSession.setLastQuestion(getQuestionForUser(userSession));
                sendQuestionForUser(userSession);
                userSessionService.saveUserSession(userSession);
            } else {
                UserSession userSession = userSessionService.getUserSessionByChatId(chat_id);
                if (userSession.getLastQuestion().getCorrectAnswer().equals(message_text)) {
                    userSession.incNumberOfCorrectAnswer();
                    userSessionService.saveUserSession(userSession);
                    sendCorrectOtNotAnswer(userSession.getChatId(), true);
                    userSession.setLastQuestion(getQuestionForUser(userSession));
                    sendQuestionForUser(userSession);
                    userSessionService.saveUserSession(userSession);
                } else {
                    sendCorrectOtNotAnswer(userSession.getChatId(), false);
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

    private void sendQuestionForUser(UserSession userSession) {
        SendMessage message = new SendMessage().setChatId(userSession.getChatId());
        message.setText(userSession.getLastQuestion().getQuestion());

        ReplyKeyboardMarkup markup = getAnswerButtons(userSession.getLastQuestion());
        if (markup != null) {
            message.setReplyMarkup(markup);
        }
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private ReplyKeyboardMarkup getAnswerButtons(Question question) {

        if (question.getAnswerList() == null) {
            return null;
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
