package com.example.RESTftulSN.DTO;

import com.example.RESTftulSN.enums.Order.SHIPPING_STATUS;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.time.LocalDateTime;

public class OrderDTO {
    private interface id {
        @NotNull
        Long getId();
    }
    private interface orderDate {
        LocalDateTime getOrderDate();
    }
    private interface status {
        SHIPPING_STATUS getStatus();
    }
    private interface userId {
        Long getUserId();
    }
    public enum Request {;
        @Value
        @NoArgsConstructor(force = true)
        @AllArgsConstructor
        public static class UserId implements userId {
            Long userId;
        }
        @Value
        @NoArgsConstructor(force = true)
        @AllArgsConstructor
        public static class Id implements id {
            Long id;
        }
        @Value
        public static class Status implements id,status {
            Long id;
            SHIPPING_STATUS status;
        }
    }

    public enum Response{;
        @Value
        public static class Create implements id,orderDate,status,userId{
            Long id;
            LocalDateTime orderDate;
            SHIPPING_STATUS status;
            Long userId;
        }
    }

}
