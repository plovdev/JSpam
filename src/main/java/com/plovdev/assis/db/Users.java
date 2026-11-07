package com.plovdev.assis.db;

import com.plovdev.assis.db.models.UserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Users {
    private static final Logger logger = LoggerFactory.getLogger("Bot User Base");

    public static void update(String key, String newVal, String id) {
        try (Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/autoresp", "plov", "774774");
             PreparedStatement stt = con.prepareStatement("UPDATE Users SET " + key + " = ? WHERE id = ?")) {

            stt.setString(1, newVal);
            stt.setString(2, id);

            stt.executeUpdate();
        } catch (Exception e) {
            logger.error("Errpr updating user: ", e);
        }
    }

    public static UserEntity get(String id) {
        try (Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/autoresp", "plov", "774774");
             PreparedStatement stt = con.prepareStatement("SELECT * FROM Users WHERE id = ?")) {
            stt.setString(1, id);

            try (ResultSet set = stt.executeQuery()) {
                if (set.next()) {
                    return new UserEntity(set.getString("id"), set.getString("name"), set.getString("state"), set.getString("tarif"), set.getString("balance"));
                }
            }
        } catch (Exception e) {
            logger.error("Произошла ошибка получения пользователя: ", e);
        }
        return null;
    }

    public static List<UserEntity> getAll() {
        List<UserEntity> entities = new ArrayList<>();
        try (Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/autoresp", "plov", "774774");
             PreparedStatement stt = con.prepareStatement("SELECT * FROM Users")) {

            try (ResultSet set = stt.executeQuery()) {
                if (set.next()) {
                    entities.add(new UserEntity(set.getString("id"), set.getString("name"), set.getString("state"), set.getString("tarif"), set.getString("balance")));
                }
            }
        } catch (Exception e) {
            logger.error("Произошла ошибка получения всех пользователей: ", e);
        }
        return entities;
    }

    public static void remove(String id) {
        try (Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/autoresp", "plov", "774774");
             PreparedStatement stt = con.prepareStatement("DELETE FROM Users WHERE id = ?")) {
            stt.setString(1, id);
            stt.executeUpdate();
        } catch (Exception e) {
            logger.error("Произошла ошибка удаления пользователя: ", e);
        }
    }

    public static boolean has(String id) {
        try (Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/autoresp", "plov", "774774");
             PreparedStatement stt = con.prepareStatement("SELECT * FROM Users WHERE id = ?")) {
            stt.setString(1, id);

            return stt.executeQuery().next();
        } catch (Exception e) {
            logger.error("Произошла ошибка проверки на налицие пользователя: ", e);
        }
        return false;
    }

    public static void add(UserEntity repository) {
        if (!has(repository.getTgId())) {
            logger.info("Пытаемся добавить нового пользователя...");
            try (Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/autoresp", "plov", "774774");
                 PreparedStatement stt = con.prepareStatement("INSERT INTO Users (id, name, state, tarif, balance) VALUES (?,?,?,?,?)")) {

                stt.setString(1, repository.getTgId().trim());
                stt.setString(2, repository.getTgName().trim());
                stt.setString(3, repository.getState().trim());
                stt.setString(4, repository.getTarif().trim());
                stt.setString(5, repository.getBalance().trim());

                stt.executeUpdate();
                logger.info("Пользователь успешно добавлен!");
            } catch (Exception e) {
                logger.error("Произошла ошибка добавления пользователя: {}", e.getMessage());
            }
        } else {
            logger.warn("Данный пользовватель уже существует.");
        }
    }
}