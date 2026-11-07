package com.plovdev.assis.constructor;

import com.plovdev.assis.bots.CommandHandler;
import com.plovdev.assis.constructor.messages.Messager;
import com.plovdev.assis.utils.Utils;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

public class AssisCreator extends TelegramLongPollingBot {
    private final Messager messager;
    public AssisCreator() {
        messager = new Messager(this);
        messager.initCommands();
    }
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            if (message.hasText() && message.getText() != null) {
                messager.notifyBotComands(update);
            }
        }
        if (update.hasCallbackQuery()) {
            CommandHandler.notifyButtons(update);
        }
    }

    @Override
    public String getBotUsername() {
        return Utils.getBotName();
    }

    @Override
    public String getBotToken() {
        return Utils.getBotToken();
    }
}