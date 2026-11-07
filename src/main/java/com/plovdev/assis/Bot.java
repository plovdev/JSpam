package com.plovdev.assis;

import it.tdlight.Init;
import it.tdlight.Log;
import it.tdlight.Slf4JLogMessageHandler;
import it.tdlight.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Bot {
    private static final Logger log = LoggerFactory.getLogger(Bot.class);

    public Bot() {
        try {
            long adminId = Integer.getInteger("it.tdlight.example.adminid", 667900586);
            Init.init();

            Log.setLogMessageHandler(1, new Slf4JLogMessageHandler());
            try (SimpleTelegramClientFactory clientFactory = new SimpleTelegramClientFactory()) {
                APIToken apiToken = new APIToken(34508761, "f526d8a80c64e0bf58c1f9c12df30115");
                TDLibSettings settings = TDLibSettings.create(apiToken);

                Path sessionPath = Paths.get("tdlight-session");
                settings.setDatabaseDirectoryPath(sessionPath.resolve("data"));
                settings.setDownloadedFilesDirectoryPath(sessionPath.resolve("downloads"));

                SimpleTelegramClientBuilder clientBuilder = clientFactory.builder(settings);

                SimpleAuthenticationSupplier<?> authenticationData = AuthenticationSupplier.user("+79303082547");
                var app = new ExampleApp(clientBuilder, authenticationData, adminId);
            }
        } catch (Exception e) {
            log.error("Error to init td lib: ", e);
        }
    }
}