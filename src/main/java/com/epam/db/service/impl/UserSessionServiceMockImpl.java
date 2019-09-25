package com.epam.db.service.impl;

import com.epam.db.service.UserSessionService;
import com.epam.telegram.entity.UserSession;

import java.util.HashMap;
import java.util.Map;

public class UserSessionServiceMockImpl implements UserSessionService {

    private Map<Long, UserSession> userSessionMap;

    public UserSessionServiceMockImpl() {
        this.userSessionMap = new HashMap<>();
    }

    @Override
    public UserSession saveUserSession(UserSession userSession) {
        if (userSessionMap.get(userSession.getChatId()) != null) {
            userSessionMap.remove(userSession.getChatId());
            userSessionMap.put(userSession.getChatId(), userSession);
        } else {
            userSessionMap.put(userSession.getChatId(), userSession);
        }
        return userSession;
    }

    @Override
    public UserSession getUserSessionByChatId(Long chatId) {
        return userSessionMap.get(chatId);
    }
}
