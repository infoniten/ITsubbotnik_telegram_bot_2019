package com.epam.telegram.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
public class BotServiceImpl implements BotService {

    private TelegramLongPollingBot telegramLongPollingBot;
    private TelegramBotsApi botsApi;
    private String activeProfile;

    @Autowired
    public BotServiceImpl(TelegramLongPollingBot telegramLongPollingBot, TelegramBotsApi botsApi, String activeProfile) {
        this.telegramLongPollingBot = telegramLongPollingBot;
        this.botsApi = botsApi;
        this.activeProfile = activeProfile;
    }

    @Override
    public void start() {
        if (activeProfile.equals("dev")) {
            log.debug("Nothing to do, it's dev profile");
            return;
        }
        try {
            botsApi.registerBot(telegramLongPollingBot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
