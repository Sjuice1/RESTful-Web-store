package com.example.RESTftulSN.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

public class CartDTO {
    private interface userId {
        @NotNull
        Long getUserId();
    }
    private interface itemId {
        @NotNull
        Long getItemId();
    }
    public enum Request{;
        @Value
        public static class Create implements userId,itemId{
            Long userId;
            Long itemId;
        }
        @Value
        @NoArgsConstructor(force = true)
        @AllArgsConstructor
        public static class UserCart implements userId{
            Long userId;
        }
    }

}
