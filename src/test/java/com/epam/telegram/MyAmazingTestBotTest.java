package com.epam.telegram;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MyAmazingTestBotTest {

    @Autowired
    private MyAmazingTestBot myAmazingTestBot;

    @Test
    public void botNameAndTokenCorrectFillTestDrive() {
        Assert.assertEquals("testUserName", myAmazingTestBot.getBotUsername());
        Assert.assertEquals("111222333:asddfsdggsdgsdg_aaatest", myAmazingTestBot.getBotToken());
    }
}