package com.plovdev.assis;

import com.plovdev.assis.constructor.AssisCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        new Thread(Bot::new).start();
//        try {
//            log.info("Starting bot...");
//            AssisCreator creator = new AssisCreator();
//            TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
//            api.registerBot(creator);
//            log.info("Bot started!");
//        } catch (Exception e) {
//            log.error("Error to start bot: ", e);
//        }
    }
}