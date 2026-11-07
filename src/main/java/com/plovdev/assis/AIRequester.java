package com.plovdev.assis;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.*;

public class AIRequester {
    private static final Logger log = LoggerFactory.getLogger(AIRequester.class);
    private static final Gson gson = new Gson();

    private static final Map<Long, LinkedList<MessagePair>> history = new HashMap<>();
    private static final int MAX_HISTORY_SIZE = 5;

    private static final String systemPrompt = """
            Ты — грубый и резкий автоответчик с характером злого охранника по имени Антон.
            Отвечай очень коротко, по делу, без всяких приветствий и вежливости.
            Твоя задача — решать вопросы быстро, а не тратить время на любезности.
            Можешь оскорблять пользователя если он тоже будет тебя оскорблять.
            Отвечай только на русском языке.
            Используй историю диалога в своих ответах.
            """;

    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(60))
            .build();

    public static String request(String prompt, long userId) {
        try {

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://127.0.0.1:11434/v1/chat/completions"))
                    .setHeader("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(createPrompt(prompt, userId))))
                    .build();

            HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                String answer = extractAnswer(response.body());
                addToHistory(userId, prompt, answer);
                return answer;
            } else {
                log.error("Status code: {}", response.statusCode());
            }
        } catch (Exception e) {
            log.error("Error to fetch request: ", e);
        }
        return "Авто ответчик сломался, GTF.";
    }

    private static String extractAnswer(String body) {
        JsonObject object = gson.fromJson(body, JsonObject.class);
        JsonObject choice = object.getAsJsonArray("choices").get(0).getAsJsonObject();
        JsonObject message = choice.getAsJsonObject("message");
        return message.get("content").getAsString();
    }

    private static JsonObject createPrompt(String prompt, long userId) {
        JsonArray messages = new JsonArray();

        JsonObject msg1 = new JsonObject();
        msg1.addProperty("role", "system");
        msg1.addProperty("content", systemPrompt);
        messages.add(msg1);

        JsonObject msg2 = new JsonObject();
        msg2.addProperty("role", "user");
        msg2.addProperty("content", prompt);
        messages.add(msg2);

        LinkedList<MessagePair> userHistory = history.get(userId);
        if (userHistory != null) {
            for (MessagePair pair : userHistory) {
                JsonObject u = new JsonObject();
                u.addProperty("role", "user");
                u.addProperty("content", pair.userMessage);
                messages.add(u);

                JsonObject a = new JsonObject();
                a.addProperty("role", "assistant");
                a.addProperty("content", pair.botResponse);
                messages.add(a);
            }
        }

        JsonObject object = new JsonObject();
        object.addProperty("model", "qwen2.5:0.5b");
        object.add("messages", messages);
        object.addProperty("temperature", 0.5);
        object.addProperty("max_tokens", 200);
        object.addProperty("top_p", 0.9);
        object.addProperty("repeat_penalty", 1.1);

        return object;
    }

    /**
     * Добавляет сообщение в историю пользователя
     */
    private static void addToHistory(long userId, String userMessage, String botResponse) {
        LinkedList<MessagePair> userHistory = history.computeIfAbsent(userId, k -> new LinkedList<>());

        // Создаем пару сообщений
        MessagePair pair = new MessagePair(userMessage, botResponse);
        userHistory.add(pair);

        // Ограничиваем историю последними 10 записями
        while (userHistory.size() > MAX_HISTORY_SIZE) {
            userHistory.removeFirst(); // Удаляем самую старую запись
        }
    }

    /**
     * Класс для хранения пары сообщений (запрос пользователя - ответ бота)
     */
    private static class MessagePair {
        private final String userMessage;
        private String botResponse;

        public MessagePair(String userMessage, String botResponse) {
            this.userMessage = userMessage;
            this.botResponse = botResponse;
        }

        public String getUserMessage() {
            return userMessage;
        }

        public String getBotResponse() {
            return botResponse;
        }

        public void setBotResponse(String botResponse) {
            this.botResponse = botResponse;
        }
    }
}