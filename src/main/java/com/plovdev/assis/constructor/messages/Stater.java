package com.plovdev.assis.constructor.messages;

import com.plovdev.assis.bots.StateMachine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

public class Stater {
    private static final Logger log = LoggerFactory.getLogger(Stater.class);
    private final TelegramLongPollingBot bot;
    private final StateMachine machine;

    public Stater(TelegramLongPollingBot bot, StateMachine sm) {
        this.bot = bot;
        machine = sm;
    }

    public void addStates() {

    }
}