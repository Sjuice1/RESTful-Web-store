package com.example.RESTftulSN.DTO;

import java.io.Serializable;
import java.time.LocalDateTime;

public class VerificationTokenDTO {
    private String token;
    private LocalDateTime expirationTime;
    private Long user_id;

    public VerificationTokenDTO(String token, LocalDateTime expirationTime) {
        this.token = token;
        this.expirationTime = expirationTime;
    }

    public VerificationTokenDTO() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(LocalDateTime expirationTime) {
        this.expirationTime = expirationTime;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }
}
