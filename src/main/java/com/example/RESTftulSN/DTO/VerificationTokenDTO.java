package com.example.RESTftulSN.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.time.LocalDateTime;

public class VerificationTokenDTO {
    private interface id {
        @NotNull
        Long getId();
    }

    private  interface token {
         String getToken();
    }
    private interface expirationTime {
        LocalDateTime getExpirationTime();
    }
    private interface userId {
        Long getUserId();
    }
    public enum Request{;
        @Value
        @NoArgsConstructor(force = true)
        @AllArgsConstructor
        public static class Id implements id {
            Long id;
        }
        @Value
        @NoArgsConstructor(force = true)
        @AllArgsConstructor
        public static class Token implements token {
            String token;
        }
    }

    public enum Response{;
        @Value
        public static class Create implements token,expirationTime,userId{
            String token;
            LocalDateTime expirationTime;
            Long userId;
        }
    }

}
