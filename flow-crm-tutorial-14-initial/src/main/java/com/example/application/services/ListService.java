package com.example.application.services;

import com.example.application.entities.User;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class ListService {

    public ListService() {
    }


    public List<User> getListWithUsers(){
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

        List<User> users = new ArrayList<>();

        JSONObject mainObject = new JSONObject(json);
        JSONArray jsonArray = mainObject.getJSONArray("users");
        for (int i = 0; i < jsonArray.length(); i++) {
            User user = new User();
            user.setUsername(jsonArray.getJSONObject(i).get("username").toString());
            user.setEmail(jsonArray.getJSONObject(i).get("email").toString());
            user.setRole(jsonArray.getJSONObject(i).get("role").toString());
            user.setBirthDate(jsonArray.getJSONObject(i).get("birthDate").toString());
            user.setIsBlocked(Boolean.valueOf(jsonArray.getJSONObject(i).get("isBlocked").toString()));
            user.setPasswordRestriction(Boolean.valueOf(jsonArray.getJSONObject(i).get("passwordRestriction").toString()));
            users.add(user);
        }

        return users;
    }

    public boolean changeParams(User user, String isBl, String resrtPswrd) {
        String json = null;
        File file = new File("src/main/resources/users.json");
        try {
            json = String.join(" ",
                    Files.readAllLines(
                            Paths.get("src/main/resources/users.json"),
                            StandardCharsets.UTF_8)
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        JSONObject mainObject = new JSONObject(json);
        JSONArray jsonArray = mainObject.getJSONArray("users");
        for (int i = 0; i < jsonArray.length(); i++) {
            if (jsonArray.getJSONObject(i).get("username").equals(user.getUsername())
                    && jsonArray.getJSONObject(i).get("email").equals(user.getEmail())) {

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
}
