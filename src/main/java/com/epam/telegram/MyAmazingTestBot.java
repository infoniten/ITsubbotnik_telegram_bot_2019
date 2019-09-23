package com.epam.telegram;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class MyAmazingTestBot extends TelegramLongPollingBot {

    public void onUpdateReceived(Update update) {

    }

    public String getBotUsername() {
        return null;
    }

    public String getBotToken() {
        return null;
    }
}
