package com.plovdev.assis;

import it.tdlight.jni.TdApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Sender {
    private static final Logger log = LoggerFactory.getLogger(Sender.class);

    public static void send(String text, long chatId) {
        try {
            var client = Storage.getClient();
            if (client == null) {
                log.error("TDLib client not initialized");
                return;
            }

            var req = new TdApi.SendMessage();
            req.chatId = chatId;
            var txt = new TdApi.InputMessageText();
            TdApi.FormattedText formattedText = new TdApi.FormattedText();
            formattedText.text = text;
            txt.text = formattedText;
            req.inputMessageContent = txt;
            Storage.getClient().sendMessage(req, true);
        } catch (Exception e) {
            log.error("Error sending message: {}", e.getMessage());
        }
    }

    /**
     * Получает информацию о прочтении сообщений в чате
     */
    public static boolean checkReadReceipts(long chatId, long messageId) {
        try {
            // Получаем информацию о чате
            TdApi.GetChat getChat = new TdApi.GetChat(chatId);
            TdApi.Chat chat = Storage.getClient().send(getChat).get();

            // Получаем последнее прочитанное сообщение
            long lastReadInboxMessageId = chat.lastReadInboxMessageId;

            // Проверяем, прочитано ли конкретное сообщение
            if (messageId <= lastReadInboxMessageId) {
                return true;
            }

        } catch (Exception e) {
            log.error("Ошибка получения статуса прочтения", e);
        }
        return false;
    }
}