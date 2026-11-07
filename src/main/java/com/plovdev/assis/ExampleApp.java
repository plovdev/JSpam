package com.plovdev.assis;

import it.tdlight.client.SimpleAuthenticationSupplier;
import it.tdlight.client.SimpleTelegramClient;
import it.tdlight.client.SimpleTelegramClientBuilder;
import it.tdlight.jni.TdApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ExampleApp implements AutoCloseable {
    private static final Logger log = LoggerFactory.getLogger(ExampleApp.class);
    private final SimpleTelegramClient client;
    private static final List<String> blackList = List.of("1168349571");
    private static final List<String> whiteList = List.of("7425474938");

    /**
     * Admin user id, used by the stop command example
     */
    private final long adminId;

    public ExampleApp(SimpleTelegramClientBuilder clientBuilder, SimpleAuthenticationSupplier<?> authenticationData, long adminId) {
        this.adminId = adminId;

        // Add an example update handler that prints when the bot is started
        clientBuilder.addUpdateHandler(TdApi.UpdateAuthorizationState.class, this::onUpdateAuthorizationState);

        // Add an example update handler that prints every received message
        clientBuilder.addUpdateHandler(TdApi.UpdateNewMessage.class, this::onUpdateNewMessage);

        // Build the client
        this.client = clientBuilder.build(authenticationData);
        Storage.setClient(client);
    }

    @Override
    public void close() throws Exception {
        client.close();
    }

    public SimpleTelegramClient getClient() {
        return client;
    }

    /**
     * Print the bot status
     */
    private void onUpdateAuthorizationState(TdApi.UpdateAuthorizationState update) {
        TdApi.AuthorizationState authorizationState = update.authorizationState;
        if (authorizationState instanceof TdApi.AuthorizationStateWaitCode) {
            System.out.println("Wait code");
        } else if (authorizationState instanceof TdApi.AuthorizationStateReady) {
            System.out.println("Logged in");
        } else if (authorizationState instanceof TdApi.AuthorizationStateClosing) {
            System.out.println("Closing...");
        } else if (authorizationState instanceof TdApi.AuthorizationStateClosed) {
            System.out.println("Closed");
        } else if (authorizationState instanceof TdApi.AuthorizationStateLoggingOut) {
            System.out.println("Logging out...");
        }
    }

    /**
     * Print new messages received via updateNewMessage
     */
    private void onUpdateNewMessage(TdApi.UpdateNewMessage update) {
        if (update.message.isOutgoing) {
            return;
        }

        // Get the message content
        TdApi.MessageContent messageContent = update.message.content;

        // Get the message text
        String text;
        if (messageContent instanceof TdApi.MessageText messageText) {
            // Get the text of the text message
            text = messageText.text.text;
        } else {
            return;
        }

        long chatId = update.message.chatId;

        String chatIdStr = String.valueOf(chatId);
        if (!chatIdStr.startsWith("-100") && !blackList.contains(chatIdStr)) {
            if (whiteList.contains(chatIdStr)) {
                Sender.send(AIRequester.request(text, chatId), chatId);
            }
        }
    }

    /**
     * Check if the command sender is admin
     */
    public boolean isAdmin(TdApi.MessageSender sender) {
        if (sender instanceof TdApi.MessageSenderUser messageSenderUser) {
            return messageSenderUser.userId == adminId;
        } else {
            return false;
        }
    }
}