package com.plovdev.assis.db;

import com.plovdev.assis.db.models.AutoResponser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Responsers {
    private static final Logger logger = LoggerFactory.getLogger("Bot resp Base");

    public static void update(String key, String newVal, String id) {
        try (Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/autoresp", "plov", "774774");
             PreparedStatement stt = con.prepareStatement("UPDATE Responers SET " + key + " = ? WHERE id = ?")) {

            stt.setString(1, newVal);
            stt.setString(2, id);

            stt.executeUpdate();
        } catch (Exception e) {
            logger.error("Errpr updating responser: ", e);
        }
    }

    public AutoResponser get(String userId) {
        try (Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/autoresp", "plov", "774774");
             PreparedStatement stt = con.prepareStatement("SELECT * FROM Responers WHERE id = ?")) {
            stt.setString(1, userId);

            try (ResultSet set = stt.executeQuery()) {
                if (set.next()) {
                    return new AutoResponser(set.getString("userId"), set.getFloat("temeprature"), set.getInt("maxTokens"), set.getFloat("top_p"), set.getFloat("repeat_penalty"), set.getString("systemPrompt"), set.getObject("history", HashMap.class));
                }
            }
        } catch (Exception e) {
            logger.error("Произошла ошибка получения автоответчика: ", e);
        }
        return null;
    }

    public List<AutoResponser> getAll() {
        List<AutoResponser> entities = new ArrayList<>();
        try (Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/autoresp", "plov", "774774");
             PreparedStatement stt = con.prepareStatement("SELECT * FROM Responers")) {

            try (ResultSet set = stt.executeQuery()) {
                if (set.next()) {
                    entities.add(new AutoResponser(set.getString("userId"), set.getFloat("temeprature"), set.getInt("maxTokens"), set.getFloat("top_p"), set.getFloat("repeat_penalty"), set.getString("systemPrompt"), set.getObject("history", HashMap.class)));
                }
            }
        } catch (Exception e) {
            logger.error("Произошла ошибка получения всех автоответчиков: ", e);
        }
        return entities;
    }

    public void remove(String id) {
        try (Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/autoresp", "plov", "774774");
             PreparedStatement stt = con.prepareStatement("DELETE FROM Responers WHERE userId = ?")) {
            stt.setString(1, id);
            stt.executeUpdate();
        } catch (Exception e) {
            logger.error("Произошла ошибка удаления автоответчика: ", e);
        }
    }

    public boolean has(String id) {
        try (Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/autoresp", "plov", "774774");
             PreparedStatement stt = con.prepareStatement("SELECT * FROM Responers WHERE userId = ?")) {
            stt.setString(1, id);

            return stt.executeQuery().next();
        } catch (Exception e) {
            logger.error("Произошла ошибка проверки на наличие автоответчика: ", e);
        }
        return false;
    }

    public void add(AutoResponser responser) {
        if (!has(responser.getUserId())) {
            logger.info("Пытаемся добавить новый автоответчик...");
            try (Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/autoresp", "plov", "774774");
                 PreparedStatement stt = con.prepareStatement("INSERT INTO Responers (userId, temeprature, maxTokens, top_p, repeat_penalty, systemPrompt, history) VALUES (?,?,?,?,?,?,?)")) {

                stt.setString(1, responser.getUserId().trim());
                stt.setFloat(2, responser.getTemeprature());
                stt.setInt(3, responser.getMaxTokens());
                stt.setFloat(4, responser.getTop_p());
                stt.setFloat(5, responser.getRepeat_penalty());
                stt.setString(6, responser.getSystemPrompt());
                stt.setObject(7, responser.getHistory());

                stt.executeUpdate();
                logger.info("автоответчик успешно добавлен!");
            } catch (Exception e) {
                logger.error("Произошла ошибка добавления автоответчика: {}", e.getMessage());
            }
        } else {
            logger.warn("Данный автоответчик уже существует.");
        }
    }
}