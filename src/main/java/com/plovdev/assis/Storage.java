package com.plovdev.assis;

import it.tdlight.client.SimpleTelegramClient;

public class Storage {
    private static SimpleTelegramClient client;
    public static SimpleTelegramClient getClient() {
        return client;
    }

    public synchronized static void setClient(SimpleTelegramClient client) {
        Storage.client = client;
    }
}