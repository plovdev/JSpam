package com.plovdev.assis.constructor.messages;

import com.plovdev.assis.bots.StateMachine;
import com.plovdev.assis.db.Users;
import com.plovdev.assis.db.models.UserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import static com.plovdev.assis.bots.CommandHandler.notifyComands;
import static com.plovdev.assis.bots.CommandHandler.registerComand;

public class Messager {
    private static final Logger log = LoggerFactory.getLogger(Messager.class);
    private final StateMachine machine = new StateMachine();
    private final TelegramLongPollingBot bot;

    public Messager(TelegramLongPollingBot bot) {
        this.bot = bot;
    }

    public void initCommands() {
        registerComand("/start", ((update, message, from, chatId, text, repository) -> {
            SendMessage st = new SendMessage(chatId, """
                    Здравствйте!
                    Рады видеть вас в этом конструкторе автоответчиков.
                    """);
            bot.execute(st);
        }));


        Stater stater = new Stater(bot, machine);
        stater.addStates();
    }

    public void notifyBotComands(Update update) {
        Message message = update.getMessage();
        User from = message.getFrom();

        boolean isExecuted = notifyComands(update);

        if (!isExecuted) {
            try {
                UserEntity repository = Users.get(from.getId().toString());
                if (repository != null) {
                    String state = repository.getState();
                    if (!state.equals("none")) {
                        machine.notifyStates(state, update);
                    }
                }
            } catch (Exception e) {
                log.error("Произошла ошибка обработки состояний пользователя: {}", e.getMessage());
            }
        }
    }
}