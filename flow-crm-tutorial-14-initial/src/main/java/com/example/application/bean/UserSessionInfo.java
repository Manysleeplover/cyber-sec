package com.example.application.bean;

import com.example.application.entities.User;
import org.springframework.context.annotation.Bean;


public class UserSessionInfo {

    private static UserSessionInfo userSessionInfo;

    private  User currentUser;

    private UserSessionInfo() {

    }

    @Bean
    public static synchronized UserSessionInfo getInstance() {
        if (userSessionInfo == null)
            userSessionInfo = new UserSessionInfo();
        return userSessionInfo;
    }

    public void setCurrentUser(User user) {
        currentUser = user;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    private void cleanCurrentUser() {
        currentUser = null;
    }

    @Override
    public String toString() {
        return "UserSessionInfo{" +
                "currentUser=" + currentUser +
                '}';
    }
}
