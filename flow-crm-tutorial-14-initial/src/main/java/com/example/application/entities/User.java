package com.example.application.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {

    private String username;
    private String password;
    private String email;
    private String birthDate;
    private String role;
    private Boolean isBlocked;
    private Boolean passwordRestriction;

}
