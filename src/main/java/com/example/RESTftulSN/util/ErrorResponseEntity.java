package com.example.RESTftulSN.util;

import java.time.LocalDateTime;

public class ErrorResponseEntity {
    private String message;
    private LocalDateTime localDateTime;

    public ErrorResponseEntity(String message, LocalDateTime localDateTime) {
        this.message = message;
        this.localDateTime = localDateTime;

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }
}
