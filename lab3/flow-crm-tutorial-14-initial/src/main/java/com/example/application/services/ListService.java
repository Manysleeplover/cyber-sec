package com.example.application.services;

import com.example.application.entities.User;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Сервис для получения данных о пользователе
 */
@Service
public class ListService {

    public ListService() {
    }

    /**
     * Получение списка пользователей из файла
     * @return - список пользователей
     */
    public List<User> getListWithUsers() {
        String json = getJson();

        List<User> users = new ArrayList<>();

        JSONObject mainObject = new JSONObject(json);
        JSONArray jsonArray = mainObject.getJSONArray("users");
        for (int i = 0; i < jsonArray.length(); i++) {
            User user = new User();
            user.setUsername(jsonArray.getJSONObject(i).get("username").toString());
            user.setRole(jsonArray.getJSONObject(i).get("role").toString());
            user.setIsBlocked(Boolean.valueOf(jsonArray.getJSONObject(i).get("isBlocked").toString()));
            user.setPasswordRestriction(Boolean.valueOf(jsonArray.getJSONObject(i).get("passwordRestriction").toString()));
            users.add(user);
        }

        return users;
    }

    /**
     * Метод изменения параметров пользователей
     * @param user - пользователь, для которого нужно изменить параметры
     * @param isBl - флаг для определения пользователя
     * @param resrtPswrd - флаг для ограничения на проль
     * @return - получилось ли выполнить операцию
     */
    public boolean changeParams(User user, String isBl, String resrtPswrd) {
        String json = getJson();

        JSONObject mainObject = new JSONObject(json);
        JSONArray jsonArray = mainObject.getJSONArray("users");
        for (int i = 0; i < jsonArray.length(); i++) {
            if (jsonArray.getJSONObject(i).get("username").equals(user.getUsername())) {

                jsonArray.getJSONObject(i).put("isBlocked", Boolean.valueOf(isBl));
                jsonArray.getJSONObject(i).put("passwordRestriction", Boolean.valueOf(resrtPswrd));

                try (PrintWriter out = new PrintWriter(new FileWriter("src/main/resources/users.json"))) {
                    out.write(mainObject.toString(4));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Метод добавления нового пользователя
     * @param username - имя пользователя, пароль затается по умолчанию строкой с нелувым значением
     * @return
     */
    public boolean addUser(String username) {
        String json = getJson();

        JSONObject mainObject = new JSONObject(json);
        JSONArray jsonArray = mainObject.getJSONArray("users");
        jsonArray.put(new JSONObject(new User(username)));

        try (PrintWriter out = new PrintWriter(new FileWriter("src/main/resources/users.json"))) {
            out.write(mainObject.toString(4));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }


    /**
     * Метод, определяющий, существует ли пользователь
     * @param username - имя пользователя
     * @return
     */
    public Boolean isDetected(String username) {
        String json = getJson();

        JSONObject mainObject = new JSONObject(json);
        JSONArray jsonArray = mainObject.getJSONArray("users");
        for (int i = 0; i < jsonArray.length(); i++) {
            if (jsonArray.getJSONObject(i).get("username").equals(username)) {
                return true;
            }
        }
        return false;
    }


    /**
     * Метод получения json из файла
     * @return - строку с json
     */
    private String getJson() {
        String json = null;
        try {
            json = String.join(" ",
                    Files.readAllLines(
                            Paths.get("src/main/resources/users.json"),
                            StandardCharsets.UTF_8)
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return json;
    }
}
