package com.example.application.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Пользователь
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {
    public User(String username) {
        this.username = username;
        this.password = "";
        this.role = "user";
        this.isBlocked = false;
        this.passwordRestriction = false;
    }

    private String username;
    private String password;
    private String role;
    private Boolean isBlocked;
    private Boolean passwordRestriction;


}
