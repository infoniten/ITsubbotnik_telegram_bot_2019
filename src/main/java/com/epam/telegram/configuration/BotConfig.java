package com.epam.telegram.configuration;

import com.epam.telegram.MyAmazingTestBot;
import com.epam.telegram.service.BotService;
import com.epam.telegram.service.BotServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;

@Configuration
public class BotConfig {

    @Value("${telegram.bot.username}")
    private String botName;
    @Value("${telegram.bot.token}")
    private String botToken;
    @Value("${spring.profiles.active:Unknown}")
    private String activeProfile;


    @Bean
    public TelegramLongPollingBot TelegramLongPollingBotManager() {
        return new MyAmazingTestBot(botName, botToken);
    }

    @Bean
    public TelegramBotsApi TelegramBotsApiManager() {
        return new TelegramBotsApi();
    }

    @Bean
    public BotService devBotServiceManager() {
        return new BotServiceImpl(TelegramLongPollingBotManager(), TelegramBotsApiManager(), activeProfile);
    }

}
