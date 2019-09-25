package com.epam.telegram;

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
                saveChatIDToDB(chat_id);
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

    private void saveChatIDToDB(long chatID) {

    }
}
