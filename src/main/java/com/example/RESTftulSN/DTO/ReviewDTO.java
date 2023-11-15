package com.example.RESTftulSN.DTO;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

public class ReviewDTO {

    private interface id {
        @NotNull
        Long getId();
    }

    private interface mark {
        @Min(value = 1, message = "Indalid mark")
        @Max(value = 10, message = "Invalid mark")
        Long getMark();
    }

    private interface description {
        @Size(max = 250, message = "Description too big")
        String getDescription();
    }
    private interface itemId {
        @NotNull(message = "Item id must be written")
        Long getItemId();
    }
    private interface userId {
        @NotNull(message = "User id must be written")
         Long getUserId();
    }
    public enum Request{;
        @Value
        public static class Create implements mark,description,itemId,userId{
            Long mark;
            String description;
            Long itemId;
            Long userId;
        }
        @Value
        @NoArgsConstructor(force = true)
        @AllArgsConstructor
        public static class Id implements id {
            Long id;
        }

    }

}
