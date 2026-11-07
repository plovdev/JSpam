package com.plovdev.assis.utils;

import java.util.UUID;

public class Utils {
    public static String getBotToken() {
        return EnvReader.getEnv("BOT_TOKEN");
    }
    public static String getBotName() {
        return EnvReader.getEnv("BOT_NAME");
    }

    /**
     * Самописный метод, который проверяет, что строка содержит ТОЛЬКО числа.
     * @param input - строка на проверку.
     * @return true - если строка являеться числом, false - если нет.
     */
    public static boolean isOnlyNumbers(String input) {
        try {
            Long.parseLong(input);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isOnlyFloats(String input) {
        try {
            Double.parseDouble(input);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String randomId() {
        return String.valueOf((UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE));
    }

    public static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}