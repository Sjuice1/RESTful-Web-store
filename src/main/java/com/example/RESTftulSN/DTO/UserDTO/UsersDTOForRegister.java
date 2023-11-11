package com.example.RESTftulSN.DTO.UserDTO;

import jakarta.validation.constraints.*;

public class UsersDTOForRegister {
    @Size(min = 3,message = "Username must be from 3 letters")
    @Size(max = 30,message = "Username must be from 30 letters")
    private String username;
    @Size(min = 6,message = "Password must be from 6 symbols")
    @Size(max = 30,message = "Password must be less than 30 symbols")
    private String password;
    @NotEmpty(message = "You must repeat password")
    private String repeatPassword;
    @Email(message = "Wrong email")
    @NotEmpty(message = "You must enter email")
    private String email;

    public UsersDTOForRegister() {
    }

    public UsersDTOForRegister(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRepeatPassword() {
        return repeatPassword;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}

