package com.epam.db.service;

import com.epam.telegram.entity.UserSession;

public interface UserSessionService {
    UserSession saveUserSession(UserSession userSession);

    UserSession getUserSessionByChatId(Long chatId);
}
