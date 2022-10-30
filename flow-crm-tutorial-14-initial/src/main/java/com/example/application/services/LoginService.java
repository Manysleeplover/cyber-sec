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

@Service
public class LoginService {

    public String isDetected(String username, String password) {
        String json = getJson();

        JSONObject mainObject = new JSONObject(json);
        JSONArray jsonArray = mainObject.getJSONArray("users");
        for (int i = 0; i < jsonArray.length(); i++) {
            if (jsonArray.getJSONObject(i).get("username").equals(username)
                    && jsonArray.getJSONObject(i).get("password").equals(password)) {
                if (jsonArray.getJSONObject(i).get("role").equals("admin")) {
                    return "admin";
                }
                return "user";
            }
        }
        return "none";
    }

    public boolean changeAdminPassword(String username, String oldPassword, String newPassword) {
        String json = getJson();

        JSONObject mainObject = new JSONObject(json);
        JSONArray jsonArray = mainObject.getJSONArray("users");
        for (int i = 0; i < jsonArray.length(); i++) {
            if (jsonArray.getJSONObject(i).get("username").equals(username)
                    && jsonArray.getJSONObject(i).get("password").equals(oldPassword)) {
                jsonArray.getJSONObject(i).put("password", newPassword);
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

    public User getUser(String username, String password){
        String json = getJson();
        User user = new User();
        JSONObject mainObject = new JSONObject(json);
        JSONArray jsonArray = mainObject.getJSONArray("users");
        for (int i = 0; i < jsonArray.length(); i++) {
            if (jsonArray.getJSONObject(i).get("username").equals(username)
                    && jsonArray.getJSONObject(i).get("password").equals(password)) {
                user.setRole(jsonArray.getJSONObject(i).get("role").toString());
                user.setPassword(jsonArray.getJSONObject(i).get("password").toString());
                user.setIsBlocked((Boolean) jsonArray.getJSONObject(i).get("isBlocked"));
                user.setPasswordRestriction(Boolean.valueOf(jsonArray.getJSONObject(i).get("passwordRestriction").toString()));
                user.setUsername(jsonArray.getJSONObject(i).get("username").toString());
            }
        }
        return user;
    }





    private String getJson(){
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

